# Data Partitioning and Bucketing

## Data Partitioning - Spark Partitioning
Partitioning is a technique used to divide large datasets into smaller, more manageable pieces based on one or more columns. Each partition is stored as a separate file or directory.
How it helps with big data:

Improves query performance by allowing Spark to skip irrelevant partitions
Enables parallel processing across multiple nodes
Facilitates data lifecycle management (e.g., dropping old partitions)

In Spark, the different types of partitioning include:

- **hash partitioning**, which distributes data based on a hash function, ensuring equal distribution.
- **range partitioning**, which involves organizing data within specific ranges.
- **custom partitioning**, which allows users to define their partitioning logic, offering flexibility.

## Data Bucketing - Spark Bucketing
Bucketing is a technique that divides data into a fixed number of buckets based on the hash value of a column. It's particularly useful for optimizing join operations.
How it helps with big data:

- Improves join performance by pre-sorting data within each bucket
- Reduces data shuffling during joins
- Enables more efficient data sampling


How Bucketing Works in Joins:

- **Co-located joins**: When tables are bucketed on the join key, the system knows that all matching records are in the corresponding buckets. This allows for "co-located" joins, where each bucket from one table only needs to be joined with the corresponding bucket from the other table.
- **Reduced shuffling**: Without bucketing, a join operation might require shuffling all data across the network to ensure that matching records end up on the same node. With bucketing, this shuffling is minimized or eliminated.
- **Parallelism**: Each bucket can be processed independently and in parallel, improving overall join performance.
- **Skew handling**: Bucketing can help mitigate data skew issues by distributing data more evenly across buckets.



## Partitioning vs. Bucketing in Spark

Use partitioning for:

- Filtering large datasets (e.g., time-based queries)
- Managing data lifecycle (e.g., dropping old data)


Use bucketing for:

- Optimizing joins between large tables
- Improving data sampling efficiency

Example of a table with partitioning and bucketing used: 

```sql
CREATE TABLE sales (
    sale_id BIGINT,
    date DATE,
    customer_id BIGINT,
    product_id BIGINT,
    quantity INT,
    amount DECIMAL(10,2)
)
USING DELTA
PARTITIONED BY (date)
CLUSTERED BY (customer_id)
INTO 128 BUCKETS

```

Partitioning:

- Partitioning by date, which is common for time-series data like sales.
- This creates a directory structure based on the date.


Bucketing:

- Bucketing by customer_id into 128 buckets.
- This creates 128 files within each partition.

```
/sales_table
  /date=2023-09-10
    /part-00000-{hash}.snappy.parquet
    /part-00001-{hash}.snappy.parquet
    ...
    /part-00127-{hash}.snappy.parquet
  /date=2023-09-11
    /part-00000-{hash}.snappy.parquet
    /part-00001-{hash}.snappy.parquet
    ...
    /part-00127-{hash}.snappy.parquet
  ...
```

Each partition (date) contains 128 files, one for each bucket. The files within each partition are bucketed based on the hash of `customer_id`.

Additional Scala code: 

When writing:

```scala
salesDF.write
  .format("delta")
  .partitionBy("date")
  .bucketBy(128, "customer_id")
  .saveAsTable("sales")
```

When reading/quering: 
```scala
spark.table("sales")
  .where("date >= '2023-09-01' AND date <= '2023-09-30'")
  .groupBy("customer_id")
  .sum("amount")
```