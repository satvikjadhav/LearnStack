# Partitioning
egment partitioning and sorting enhance performance and reduce data size in Druid datasources.

we can partition data within a single datasource; using multiple datasources is covered under multitenancy considerations.

## Time Chunk Partitioning
Druid partitions datasources into _time chunks_, each containing one or more segments.
- **Importance**:
  1. Time-based queries can effectively prune segments.
  2. Exclusive write locks on time partitions facilitate data management tasks (e.g., overwriting, compacting).
  3. Segments are contained within a time partition; avoid excessive fine-grained partitioning to maintain performance.
- **Common Granularity**: `hour` or `day` is typically used; `hour` is preferred for streaming ingestion to reduce delays.

## Secondary Partitioning
Further partitions segments within a time chunk based on specific dimensions.
- **Benefits**: Improves data locality, meaning rows with the same dimension value are stored together, enhancing access speed and reducing storage footprint.
- **Best Practices**: Partition on a dimension frequently used as a filter to improve performance and compression.

## Partitioning and Sorting
- **Configuration**: If a "natural" partitioning dimension exists, place it first in the `dimensions` list of your `dimensionsSpec` for effective sorting.
- **Timestamp Sorting**: Druid always sorts by timestamp first; to improve dimension sorting, set `queryGranularity` equal to `segmentGranularity`.

## Configuration Methods
- Not all ingestion methods allow explicit partitioning configurations. Options vary based on the ingestion type:
  - **Native Batch**: Configured with `partitionsSpec` in `tuningConfig`.
  - **SQL**: Uses `PARTITIONED BY` and `CLUSTERED BY`.
  - **Hadoop**: Similar to Native Batch with `partitionsSpec`.
  - **Kafka**: Partitions based on Kafka topic; can also use reindexing or compaction post-ingestion.
  - **Kinesis**: Similar to Kafka; partitions based on stream sharding.

