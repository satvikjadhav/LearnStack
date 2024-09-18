# Storage overview

Druid stores data in datasources, which are similar to tables in a traditional RDBMS. Each datasource is partitioned by time and, optionally, further partitioned by other attributes. Each time range is called a chunk (for example, a single month, if the datasource is partitioned by month). Within a chunk, data is partitioned into one or more segments. Each segment is a single file, typically comprising up to a few million rows of data. Since segments are organized into time chunks, it's sometimes helpful to think of segments as living on a timeline like the following. 

A datasource can be made up of just a few segments or even tens or hundreds of thousands of segements. Each segment is created by a MiddleManager as mutable and uncommitted. Data is queryable as soon as it is added to an uncommitted segment

The proper segement building process occurs later which accelerates queries by producing data files that are compact and indexed: 

- Conversion to columnar format
- Indexing with bitmap indexes
- Compression
    - Dictionary encoding with id storage minimization for String columns
    - Bitmap compression for bitmap indexes
    - Type-aware compression for all columns

Segments are also periodically commited and pushed to the [deep storage](https://druid.apache.org/docs/latest/design/deep-storage), where they become immutable, and move from `MiddleManagers` to the `Historical` services. This entry is a self-describing bit of metadata about the segment, including things like the schema of the segment, its size, and its location on deep storage. These entries tell the Coordinator what data is available on the cluster.

## Indexing and handoff
Indexing is the mechanism by which new segments are created, and handoff is the mechanism by which they are published and served by `Historical` services. 

In Apache Druid, indexing tasks follow a specific process based on their type:

1. **Segment Identification:** Before building a segment, the task determines its identifier. This is done through:
   - **Appending Tasks:** Such as `Kafka` tasks or index tasks in append mode, which use the "allocate" API on the Overlord to add a new partition if necessary.
   - **Overwriting Tasks:** Such as Hadoop tasks or index tasks not in append mode, which lock an interval, create a new version number, and generate a new set of segments.

2. **Real-time Tasks:** For real-time indexing tasks (like `Kafka` tasks), the segment is immediately available for queries but remains unpublished initially.

3. **Completion:** Once data reading is finished, the segment is pushed to deep storage and published by updating the metadata store.

4. **Post-Completion:** Real-time tasks wait for a `Historical` service to load the segment to ensure continuous query availability, while non-real-time tasks exit immediately after publishing.

On the `Coordinator` / `Historical` side of Apache Druid:

1. **Metadata Polling:** The `Coordinator` periodically checks the metadata store (default interval is every 1 minute) for any newly published segments.

2. **Loading Segments:** If the `Coordinator` finds a segment that is published and required but not yet available, it selects a `Historical` service to load the segment and sends instructions for it to be loaded.

3. **Serving Segments:** The `Historical` service loads the segment and begins serving it for queries.

4. **Task Exit:** If the indexing task was waiting for this handoff, it will exit once the segment is successfully loaded and available.

## Segment identifiers

Segments all have a four-part identifier with the following components:
- Datasource name.
- Time interval (for the time chunk containing the segment; this corresponds to the `segmentGranularity` specified at ingestion time).
- Version number (generally an ISO8601 timestamp corresponding to when the segment set was first started).
- Partition number (an integer, unique within a datasource+interval+version; may not necessarily be contiguous).

For example, this is the identifier for a segment in datasource `clarity-cloud0`, time chunk `2018-05-21T16:00:00.000Z/2018-05-21T17:00:00.000Z`, version `2018-05-21T15:56:09.909Z`, and partition number 1:

```
clarity-cloud0_2018-05-21T16:00:00.000Z_2018-05-21T17:00:00.000Z_2018-05-21T15:56:09.909Z_1
```

## Segment versioning
In Apache Druid, version numbers enable multi-version concurrency control (MVCC) for managing data updates. Here’s how it works:

- **Single Version for Appends:** If only append operation is done, there’s a single version per time chunk, as no overwriting occurs.

- **Handling Overwrites:** When data is overwritten, Druid creates a new set of segments with a higher version number for the same datasource and time interval. This indicates to Druid that the new version should replace the old one.

- **Seamless Transition:** To ensure a smooth transition for users:
  - **New Data Loading:** Druid first loads the new segments but doesn’t make them available for queries immediately.
  - **Switching Queries:** Once the new data is fully loaded, Druid switches all new queries to the new segments instantaneously.
  - **Removing Old Segments:** The old segments are dropped a few minutes after the switch to the new version.

This approach ensures that users experience no disruption, as queries seamlessly transition from old to new data versions.

## Segment lifecycle

1. **Segment Construction and Metadata Store**:
   - **Construction**: A segment is built and, once complete, metadata for the segment (a small JSON file) is created.
   - **Publishing**: This metadata is then published to the metadata store. The segment's metadata includes a boolean flag called "used," which indicates if the segment is intended to be queryable.

2. **Deep Storage**:
   - After construction, segment data files are pushed to deep storage before the metadata is published.

3. **Query Availability**:
   - Once published, the segment becomes available for querying on a Druid data server, which could be a realtime task, directly from deep storage, or a Historical service.

**Flags to Check Segment Status**:
- **is_published**: Indicates if the segment's metadata is published and the "used" flag is set to true.
- **is_available**: Shows if the segment is currently available for querying, either from a realtime task or Historical service.
- **is_realtime**: True if the segment is only available on realtime tasks (usually true initially and then becomes false once the segment is published).
- **is_overshadowed**: True if the segment is published and fully overshadowed by other published segments. This is usually a temporary state before the segment's "used" flag is set to false.

## Availability and consistency
In Apache Druid, ingestion and querying are architecturally separate, affecting how availability and consistency are managed.

### **Ingestion Methods and Guarantees**

1. **Transactional Guarantees**:
   - **Supervised "Seekable-Stream" Ingestion (e.g., Kafka, Kinesis)**:
     - **All-or-Nothing Publishing**: Stream offsets and segment metadata are committed in a single transaction. If ingestion fails, partially ingested data is discarded, and Druid resumes from the last committed offsets. This ensures exactly-once publishing.
   
   - **Hadoop-Based Batch Ingestion**:
     - **All-or-Nothing Publishing**: Metadata for all segments is published in one transaction.

   - **Native Batch Ingestion**:
     - **Parallel Mode**: Metadata is published in a single transaction after all subtasks are complete.
     - **Simple Mode**: A single task publishes all segment metadata in one transaction upon completion.

2. **Idempotency Guarantees**:
   - **Supervised "Seekable-Stream" Ingestion (e.g., Kafka, Kinesis)**:
     - **Idempotent**: Repeated executions do not cause duplicate data due to synchronized updates of stream offsets and segment metadata.

   - **Hadoop-Based Batch Ingestion**:
     - **Idempotent**: If input sources are not the same Druid datasource being ingested into. Running the same task twice without overwriting ensures no duplicates.

   - **Native Batch Ingestion**:
     - **Idempotent**: Unless `appendToExisting` is true or if input sources include the same Druid datasource being ingested into. If either condition is true, re-running the task could result in data duplication.

On the query side, Apache Druid ensures consistency and performance through its handling of segment availability and atomic replacement:

### **Druid Query Consistency and Atomic Replacement**

1. **Broker's Role**:
   - **Consistency**: The Druid Broker manages which set of segment versions is used for a query. It ensures that queries use a consistent set of segments based on their availability at the time the query starts.

2. **Atomic Replacement**:
   - **Definition**: Atomic replacement ensures seamless updates from an older to a newer version of data without impacting query performance or consistency. This process makes the switch between segment versions appear instantaneous to users.
   - **Application**: Atomic replacement applies to Hadoop-based batch ingestion, native batch ingestion (with `appendToExisting` set to false), and compaction.
   - **Time Chunk Basis**: Atomic replacement occurs individually for each time chunk. If a task or compaction affects multiple time chunks, the replacement for each chunk happens sequentially, not simultaneously.

3. **Core Set Concept**:
   - **Core Set**: Each time chunk is associated with a core set of segments. When data is overwritten, a new core set with a higher version number is created. The Broker waits until all segments of this new core set are available before using them.
   - **Versioning**: Only one core set per version is used per time chunk at any given time. This setup ensures consistency and atomicity in segment replacement.

4. **Experimental Segment Locking Mode**:
   - **Activation**: By setting `forceTimeChunkLock` to false, Druid uses an experimental segment locking mode.
   - **Atomic Update Group**: Instead of creating a new core set, an atomic update group is formed using the existing version for a time chunk. Multiple atomic update groups with the same version number can exist per time chunk, each replacing a specific subset of earlier segments.
   - **Advantages**: This mode allows for atomic replacement of subsets of data within a time chunk and supports simultaneous atomic replacement and appending.

5. **Handling Unavailable Segments**:
   - **Impact of Historicals Going Offline**: If multiple Historical nodes go offline and segments become unavailable beyond the replication factor, queries will only include the segments that are still accessible.
   - **Recovery**: Druid re-loads the unavailable segments onto other Historical nodes as soon as possible, and once reloaded, these segments become available for queries again.

