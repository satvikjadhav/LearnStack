# Segments
In Apache Druid, data and indexes are organized into segment files based on time intervals.

### **Segment Management in Druid**

1. **Segment Creation**:
   - **Time-Based Partitioning**: Druid creates segments for each time interval that contains data. If a time interval has no data, no segment is created for that interval.
   - **Multiple Segments**: There might be multiple segments for the same time interval if data for that interval is ingested through different jobs.

2. **Compaction**:
   - **Purpose**: The compaction process aims to merge multiple segments from the same time interval into a single segment. This helps optimize query performance by reducing the number of segments that need to be read.
   
3. **Segment Granularity**:
   - **Configurable Interval**: The time interval for segment creation is controlled by the `segmentGranularity` parameter in the `granularitySpec` configuration.
   
4. **Optimal Segment Size**:
   - **Recommended Range**: For efficient operation under heavy query loads, segment files should ideally be between 300 and 700 MB.
   - **Adjustments**: If segments are larger than this range:
     - Consider adjusting the `segmentGranularity` to change the time interval.
     - Use data partitioning or modify the `targetRowsPerSegment` in `partitionsSpec` to manage segment sizes. A common starting point for `targetRowsPerSegment` is around 5 million rows.
   - **Resources**: Refer to the Sharding and Partitioning documentation for detailed guidance on tuning these parameters.

## Segment File Structure
Segment files are columnar: the data for each column is laid out in separate data structures. By storing each column separately, Druid decreases query latency by scanning only those columns actually needed for a query. There are three basic column types: timestamp, dimensions, and metrics. 

### Column Types

1. **Timestamp and Metrics**:
   - Stored as arrays of integers or floating-point values.
   - Compressed using LZ4.
   - Upon a query, Druid decompresses only the required rows and applies aggregations.

2. **Dimension Columns**:
   - Used for filter and group-by operations and require three distinct data structures:
     - **Dictionary**: Maps string values to integer IDs, allowing for compact representation.
     - **List**: Encodes column values using the dictionary, necessary for GroupBy and TopN queries.
     - **Bitmap**: One bitmap per distinct value, indicating which rows contain that value, enabling fast filtering.

### Example: "Page" Column
```
1: Dictionary
   {
    "Justin Bieber": 0,
    "Ke$ha":         1
   }

2: List of column data
   [0,
   0,
   1,
   1]

3: Bitmaps
   value="Justin Bieber": [1,1,0,0]
   value="Ke$ha":         [0,0,1,1]
```
- **Dictionary**:
  - Maps names to IDs (e.g., "Justin Bieber" = 0, "Ke$ha" = 1).
  
- **List of Column Data**:
  - Encodes values using the dictionary (e.g., [0, 0, 1, 1]).

- **Bitmaps**:
  - Indicates presence of values in rows (e.g., for "Justin Bieber": [1, 1, 0, 0]; for "Ke$ha": [0, 0, 1, 1]).

### Bitmap Characteristics

- **Sparse Representation**: High cardinality columns result in sparse, compressible bitmaps.
- **Compression**: Druid uses specialized bitmap compression algorithms like Roaring bitmap compression to efficiently manage storage.

This structure optimizes both space and query performance by focusing on relevant data while leveraging efficient data representations.

## Handling Null Values

### Default Null Handling Mode

- **Storage Mechanism**:
  - String columns store null values as ID 0 in the dictionary, with a corresponding bitmap index for filtering nulls.
  - Numeric columns also have a null value bitmap to indicate rows with null values, aiding in null checks and filtering.

### Legacy Mode (Deprecated)

- **Characteristics**:
  - Introduced prior to Druid 28.0.0, legacy mode can still be enabled using `druid.generic.useDefaultValueForNull=true`.
  - **String Columns**: Cannot distinguish between empty strings (`''`) and nulls; both are treated as the same value.
  - **Numeric Columns**: Do not support null values; nulls are stored as 0, and thus do not have a null value bitmap.

### Performance Implications

- In legacy mode, numeric columns may have slightly reduced segment sizes and improved query performance in some cases, as there’s no need to check for a null value bitmap.

### Future Changes

- Legacy mode is deprecated and will be removed in future releases, encouraging users to adopt the default null handling approach for better compatibility and functionality.

## Segments with different schemas

**Different Schemas Allowed**: Segments for the same datasource can have varying schemas, which enables flexibility in data ingestion.

### Behavior of Queries

1. **String Columns (Dimensions)**:
   - If a string column exists in one segment but not in another:
     - **Default Mode**: Queries treat the missing dimension as containing blank values.
     - **SQL-Compatible Mode**: Queries treat the missing dimension as containing null values.

2. **Numeric Columns (Metrics)**:
   - If a numeric column exists in one segment but not in another:
     - Queries on the segment without the metric operate as if the metric does not exist. Aggregations will simply ignore the missing metric.

## Column Format

Each column in Druid is stored as two components:

1. **ColumnDescriptor**:
   - A Jackson-serialized instance of the internal Druid `ColumnDescriptor` class.
   - Utilizes Jackson's polymorphic deserialization, allowing for flexible serialization methods with minimal code changes.
   - Contains metadata about the column, such as its type and whether it supports multi-value entries.

2. **Binary Data**:
   - The actual binary data for the column follows the serialized `ColumnDescriptor`.
   - This data is deserialized using the logic defined in the `ColumnDescriptor`.

This structure allows Druid to efficiently manage column metadata and data, facilitating extensibility in serialization and deserialization processes while maintaining performance.

## Multi-value Columns
A multi-value column allows a single row to store multiple strings, similar to an array of strings. When a datasource uses multi-value columns, the data structures within the segment files are adjusted as follows:
```
1: Dictionary
   {
    "Justin Bieber": 0,
    "Ke$ha":         1
   }

2: List of column data
   [0,
   [0,1],  <--Row value in a multi-value column can contain an array of values
   1,
   1]

3: Bitmaps
   value="Justin Bieber": [1,1,0,0]
   value="Ke$ha":         [0,1,1,1]
                            ^
                            |
                            |
   Multi-value column contains multiple non-zero entries
```

Let's imagine that in the example above, the second row is tagged with both the `Ke$ha` and `Justin Bieber` topics. 

For example:
   - Bitmap for "Justin Bieber": [1, 1, 0, 0]
   - Bitmap for "Ke$ha": [0, 1, 1, 1]

The second entry in the bitmap for "Ke$ha" indicates that this row contains the value.

In multi-value columns, rows can hold multiple entries, leading to changes in both the list of column data and the associated bitmaps. Each row's data is stored as an array, and the bitmap accurately represents the presence of multiple values, allowing Druid to efficiently handle complex data structures.

## Compression

### Default Compression Methods

- **LZ4 Compression**:
  - Used by default for compressing blocks of values in string, long, float, and double columns.

- **Roaring Bitmap Compression**:
  - Employed for compressing bitmaps associated with string columns and numeric null values.
  - Generally recommended due to its performance benefits unless specific data and query patterns suggest otherwise.

### Alternative Compression Option

- **Concise Bitmap Compression**:
  - An alternative to Roaring, particularly effective for high cardinality string column bitmaps.
  - **Performance Characteristics**:
    - Roaring is faster for filters that match many values.
    - Concise may offer a smaller footprint due to Roaring's overhead but tends to be slower when many values are matched.

### Configuration

- Compression settings are applied at the segment level rather than on individual columns, allowing for consistent management of compression strategies across segments.

## Segment Identification

A Druid segment identifier typically includes the following components:

1. **Datasource**: The name of the datasource the segment belongs to.
2. **Interval Start Time**: The start time of the data interval, formatted in ISO 8601.
3. **Interval End Time**: The end time of the data interval, also in ISO 8601 format.
4. **Version Information**: Indicates the version of the segment.

If the data is sharded beyond the time range, the identifier includes:

5. **Partition Number**: A number representing the specific partition of the data.

### Format

The complete format for a segment identifier is as follows:
```
datasource_intervalStart_intervalEnd_version_partitionNum
```

This structure ensures clarity and uniqueness for segments, facilitating efficient data management and querying.

When multiple segments are created for the same time interval, the partition numbers increase, indicating the presence of multiple segments. For example,

```
foo_2015-01-01/2015-01-02_v1_0
foo_2015-01-01/2015-01-02_v1_1
foo_2015-01-01/2015-01-02_v1_2
```
In this case, v1 indicates that these segments share the same version and are part of the same dataset.

When the data is reindexed with a new schema, Druid generates new segments with a new version ID. For example,
```
foo_2015-01-01/2015-01-02_v2_0
foo_2015-01-01/2015-01-02_v2_1
foo_2015-01-01/2015-01-02_v2_2
```
Here, v2 indicates that these segments are based on the updated schema, while the partition numbers continue to reflect multiple segments within the same interval.

## Sharding

### Segments and Blocks

- **Multiple Segments**: Druid allows multiple segments for a single time interval and datasource, which together form a "block" for that interval.
  
- **Completion Requirement**: For certain types of shard specifications (shardSpecs), all segments in a block must be fully loaded for a query to complete. For example:
  - Segments for the interval:
    - `sampleData_2011-01-01T02:00:00:00Z_2011-01-01T03:00:00:00Z_v1_0`
    - `sampleData_2011-01-01T02:00:00:00Z_2011-01-01T03:00:00:00Z_v1_1`
    - `sampleData_2011-01-01T02:00:00:00Z_2011-01-01T03:00:00:00Z_v1_2`
  - All three segments must be loaded for queries on this interval to return results.

### Linear Shard Specs

- **Exception to Completeness Rule**: Linear shard specs do not require all segments to be loaded for a query to complete. This means:
  - If real-time ingestion creates three segments with a linear shard spec and only two are loaded, queries will still return results based on those two segments.

## Segment Components

Here’s a summary of the structure and components of a Druid segment:

### Segment File Structure

A Druid segment contains several key files:

1. **version.bin**:
   - Contains 4 bytes representing the current segment version as an integer.
   - For example, the version for v9 segments is represented as `0x0, 0x0, 0x0, 0x9`.

2. **meta.smoosh**:
   - This file contains metadata about the contents of the smoosh files, including filenames and offsets.

3. **XXXXX.smoosh**:
   - Smoosh files store concatenated binary data to reduce the number of open file descriptors needed for data access.
   - Each smoosh file is capped at 2 GB to comply with the memory-mapped ByteBuffer limit in Java.
   - Contains:
     - Individual files for each column in the data, including one for the `__time` column (timestamp).
     - An `index.drd` file that holds additional segment metadata.

### Segment Format Version

- Segments have an internal format version, with the current version being **v9**.

Druid segments are structured to optimize data access and management, utilizing smoosh files for efficient storage and organization while maintaining versioning for format compatibility.

## Implications of Updating Segments

Druid’s MVCC approach allows for efficient updates and concurrent access, ensuring that data remains consistent within intervals while supporting gradual transitions to new segment versions. This flexibility is particularly useful in environments with real-time data ingestion and querying.

### Multi-Version Concurrency Control (MVCC)

- **Versioning for Updates**: Druid employs versioning to manage updates, enabling MVCC. This allows different versions of segments to coexist, facilitating concurrent read and write operations.

- **Distinct from Segment Format Version**: The MVCC versions are separate from the segment format version (like v9 discussed earlier).

### Atomicity of Updates

- **Interval Atomicity**: Updates that span multiple segment intervals are atomic only within each interval, meaning:
  - An update is fully applied to a segment for a specific time interval, but not necessarily across multiple intervals.

### Segment Loading Behavior

- **Loading New Segments**: When new segments (e.g., v2) are built, they are loaded into the cluster immediately and can replace existing segments (e.g., v1) for overlapping time periods.
  
- **Mixed Versions**: During the loading process, it's possible to have a mix of v1 and v2 segments in the cluster. For example:
  - Existing segments:
    - `foo_2015-01-01/2015-01-02_v1_0`
    - `foo_2015-01-02/2015-01-03_v2_1`
    - `foo_2015-01-03/2015-01-04_v1_2`
  
  - In this scenario, queries may access both v1 and v2 segments simultaneously, depending on the data availability.

