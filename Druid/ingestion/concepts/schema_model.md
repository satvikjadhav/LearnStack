# Druid Schema Model
Druid stores data in datasources, which are similar to tables in a traditional relational database management system (RDBMS). Druid's data model shares similarities with both relational and timeseries data models.

## Primary Timestamp
Druid schemas must always include a primary timestamp. Druid uses the primary timestamp to partition and sort the data.

- **Purpose**:
  - Quickly identifies and retrieves data for time-based queries.
  - Facilitates time-based data management (e.g., dropping, overwriting, retention).
- **Storage**: Always stored in the `__time` column, regardless of source.
- **Configuration**: Set via `timestampSpec` at ingestion; control operations with `granularitySpec`.
- **Secondary Timestamps**: Additional timestamp columns can be stored as secondary timestamps.

## Dimensions
Columns stored "as-is" for grouping, filtering, and aggregating.
- **Rollup**: If disabled, dimensions function like standard database columns.
- **Configuration**: Defined in the `dimensionsSpec` at ingestion.

## Metrics
Columns stored in an aggregated form; useful with rollup enabled.
- **Rollup**: Aggregates multiple rows with the same timestamp and dimension values, retaining summary info.
  - Example: Collapsing netflow data to a single row per `(minute, srcIP, dstIP)` tuple while aggregating packet and byte counts.
- **Performance**: Enables faster computation of certain aggregators at query time.
- **Configuration**: Specified in the `metricsSpec` at ingestion.

### Key Concepts
- **Rollup**: Aggregation method that collapses rows, enhancing performance and reducing data volume.
- **Timestamps**: Central to data retrieval and management in Druid; must include primary timestamp for schema validity.