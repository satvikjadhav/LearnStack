## Coordinator Service

The Coordinator service is primarily responsible for segment management and distribution. More specifically, the Coordinator service communicates to Historical services to load or drop segments based on configurations.
  
- **Key Responsibilities**:
  - Loads new segments and drops outdated ones.
  - Ensures segments are replicated across multiple Historical nodes according to configured requirements.
  - Balances segments between Historical nodes to maintain even load.

- **Operation Frequency**: 
  - Runs periodically; the interval between runs is configurable.
  - Assesses the cluster's current state before executing actions.

- **Connections**:
  - Maintains a connection to a ZooKeeper cluster for up-to-date cluster information.
  - Connects to a database that tracks "used" segments and loading rules.

- **Segment Assignment Process**:
  - Historical services are sorted by capacity before assigning unassigned segments; those with the least capacity are prioritized.
  - Unassigned segments are assigned to the least capacity services to promote balance.
  - The Coordinator does not directly communicate with Historical services for assignments; instead, it creates temporary information in the load queue path for the Historical service.
  - Historical services load the segment upon receiving the request and start servicing it.

## Cleaning Up Overshadowed Segments

- **Process Overview**: 
  - On each run, the Coordinator compares the set of used segments in the database with the segments served by some Historical nodes in the cluster.

- **Actions Taken**:
  - Sends requests to Historical nodes to unload:
    - Unused segments
    - Segments that are removed from the database

- **Overshadowed Segments**:
  - Segments are marked as unused if their versions are outdated and have been replaced by newer segments.
  - During the next Coordinator run, overshadowed segments are unloaded from Historical nodes.

## Segment Availability

- **Handling Service Restarts**:
  - If a Historical service restarts or becomes unavailable, the Coordinator identifies it as missing and treats all its served segments as dropped.

- **Reassignment Process**:
  - Over time, dropped segments may be reassigned to other Historical services in the cluster.
  - If the Historical service becomes available again within the lifetime period, it will resume serving segments from its cache without reassignment of those segments across the cluster.

- **Transitional Data Structure**:
  - A data structure retains information on dropped segments along with an associated lifetime.
  - The lifetime indicates a period during which the Coordinator will not reassign the dropped segment.

## Balancing Segment Load

Ensures even distribution of segments across Historical services in the cluster.

- **Utilization Assessment**:
  - The Coordinator calculates the total size of segments served by each Historical service during each run.
  - Identifies the Historical service with the highest utilization and the one with the lowest utilization.

- **Utilization Threshold**:
  - Computes the percentage difference in utilization between the highest and lowest services.
  - If the difference exceeds a specified threshold, segments will be moved to balance the load.

- **Segment Movement**:
  - A configurable limit exists on the number of segments that can be moved in each run.
  - Segments are selected at random for movement, and the move occurs only if it decreases the utilization percentage difference between the two services.

## Automatic Compaction

The Coordinator manages automatic segment compaction to optimize segment sizes, improving query performance.

- **Compaction Process**:
  - Each run, the Coordinator identifies segments to compact based on the segment search policy.
  - It issues a compaction task to merge small segments or split large ones.

- **Task Submission Limit**:
  - The maximum number of concurrent compaction tasks is determined by `min(sum of worker capacity * slotRatio, maxSlots)`.
  - At least one compaction task is submitted if compaction is enabled for a dataSource, even if the limit equals zero.

- **Reasons for Compaction Task Failure**:
  - **Input Segments Removed**: If segments are overshadowed or removed before the task starts, it fails immediately.
  - **Time Chunk Lock Conflicts**: If a higher-priority task locks an overlapping time interval, the compaction task fails.

- **Failure Handling**:
  - Upon failure, the Coordinator re-evaluates the segments in the affected interval and issues another compaction task in the next run.

- **Configuration Options**:
  - **Default Operation**: Compacting Segments Coordinator Duty is automatically enabled as part of the Indexing Service Duties group.
  - **Isolated Configuration**: Can be run as a separate duty group to change its period without affecting other duties.
  - **Properties to Set**:
    - `druid.coordinator.dutyGroups=[<SOME_GROUP_NAME>]`
    - `druid.coordinator.<SOME_GROUP_NAME>.duties=["compactSegments"]`
    - `druid.coordinator.<SOME_GROUP_NAME>.period=<PERIOD_TO_RUN_COMPACTING_SEGMENTS_DUTY>` 

## Segment Search Policy in Automatic Compaction

The segment search policy determines which segments need compaction during each Coordinator run, examining time chunks from newest to oldest.

- **Conditions for Compaction**:
  A set of segments qualifies for compaction if:
  1. The total size of segments in the time chunk is less than or equal to the configured `inputSegmentSizeBytes` (default = 100 TB).
  2. Segments have not been compacted yet or their compaction spec has been updated since the last compaction (e.g., `maxTotalRows` or `indexSpec`).

- **Example Scenario**:
  - DataSources:
    - **foo**:
      - `foo_2017-11-01T00:00:00.000Z_2017-12-01T00:00:00.000Z_VERSION`
      - `foo_2017-11-01T00:00:00.000Z_2017-12-01T00:00:00.000Z_VERSION_1`
      - `foo_2017-09-01T00:00:00.000Z_2017-10-01T00:00:00.000Z_VERSION`
    - **bar**:
      - `bar_2017-10-01T00:00:00.000Z_2017-11-01T00:00:00.000Z_VERSION`
      - `bar_2017-10-01T00:00:00.000Z_2017-11-01T00:00:00.000Z_VERSION_1`
  - **Assumption**: Each segment is 10 MB and has not been compacted yet.
  - **Compaction Order**:
    - First, the two most recent `foo` segments are selected for compaction.
    - If task slots are available, the two `bar` segments are selected next.
    - Lastly, the single `foo` segment from an older time chunk is picked up.

- **Skip Offset Configuration**:
  - The search starting point can be adjusted using `skipOffsetFromLatest`. 
  - This setting allows the policy to ignore segments within the time chunk defined as (end time of the most recent segment - `skipOffsetFromLatest`).
  - This is useful to avoid conflicts with real-time tasks, which have higher priority and can terminate compaction tasks if their intervals overlap.

## FAQ

1. Do clients ever contact the Coordinator service?

        The Coordinator is not involved in a query.

        Historical services never directly contact the Coordinator service. The Coordinator tells the Historical services to load/drop data via ZooKeeper, but the Historical services are completely unaware of the Coordinator.

        Brokers also never contact the Coordinator. Brokers base their understanding of the data topology on metadata exposed by the Historical services via ZooKeeper and are completely unaware of the Coordinator.

2. Does it matter if the Coordinator service starts up before or after other services?

        No. If the Coordinator is not started up, no new segments will be loaded in the cluster and outdated segments will not be dropped. However, the Coordinator service can be started up at any time, and after a configurable delay, will start running Coordinator tasks.

        This also means that if you have a working cluster and all of your Coordinators die, the cluster will continue to function, it just won’t experience any changes to its data topology.