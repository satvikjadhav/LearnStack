# Metadata Storage
Apache Druid relies on an external dependency for metadata storage. Druid uses the metadata store to house various metadata about the system, but not to store the actual data. The metadata store retains all metadata essential for a Druid cluster to work.

- **Key Components of Metadata Store**:
  - Segment records
  - Rule records
  - Configuration records
  - Task-related tables
  - Audit records

- **Default Metadata Store**: Derby
  - **Note**: Not suitable for production environments.
  
- **Recommended Production Stores**: MySQL and PostgreSQL.


This service is crucial for the operational integrity of a Druid cluster, providing necessary metadata for its functions.

## Metadata Storage Tables

### Segments Table

Controlled by `druid.metadata.storage.tables.segments`.
- **Function**: Stores metadata about segments in the Druid system, referred to as "used segments."
- **Polling**: The Coordinator polls this table to identify which segments are available for querying.

#### Key Columns:
- **Used Column**:
  - **Value 1**: Segment is "used" (loaded and available).
  - **Value 0**: Segment is not loaded; allows unloading without deleting metadata (facilitates rollback).
  
- **Used Status Last Updated Column**: Records the last update time of the segment’s used status, aiding the Coordinator in identifying segments for deletion if automated segment killing is enabled.

- **Payload Column**: Contains a JSON blob with all segment metadata; may duplicate information from other columns for redundancy.

As an example, the `payload` column may take the following form:
```json
{
 "dataSource":"wikipedia",
 "interval":"2012-05-23T00:00:00.000Z/2012-05-24T00:00:00.000Z",
 "version":"2012-05-24T00:10:00.046Z",
 "loadSpec":{
    "type":"s3_zip",
    "bucket":"bucket_for_segment",
    "key":"path/to/segment/on/s3"
 },
 "dimensions":"comma-delimited-list-of-dimension-names",
 "metrics":"comma-delimited-list-of-metric-names",
 "shardSpec":{"type":"none"},
 "binaryVersion":9,
 "size":size_of_segment,
 "identifier":"wikipedia_2012-05-23T00:00:00.000Z_2012-05-24T00:00:00.000Z_2012-05-23T00:10:00.046Z"
}
```

This table is essential for managing segment availability and operational efficiency in the Druid cluster.

### Rule Table
- **Purpose**: Stores rules for segment allocation and reallocation decisions made by the Coordinator.

### Config Table
- **Function**: Stores runtime configuration objects.
- **Current Status**: Limited usage; future viability of this mechanism is uncertain.
- **Goal**: Allows for changing some configuration parameters across the cluster at runtime.

### Task-Related Tables
- **Usage**: Created and used by the Overlord and MiddleManager for task management.

### Audit Table
- **Purpose**: Tracks audit history for configuration changes, including rule modifications by the Coordinator.

## Metadata Storage Access
- **Accessed By**:
  - Indexing service processes
  - Realtime processes
  - Coordinator processes
- **Security Note**: Ensure that only these processes have permissions to access the metadata storage (e.g., through AWS security groups).

These tables and access controls are vital for maintaining operational integrity and security in the Druid ecosystem.