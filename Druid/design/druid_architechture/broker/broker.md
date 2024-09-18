# Broker service

The Broker service routes queries in a distributed cluster setup. It interprets the metadata published to ZooKeeper about segment distribution across services and routes queries accordingly. Additionally, the Broker service consolidates result sets from individual services.

## Forwarding queries


Druid efficiently manages query distribution by leveraging ZooKeeper and the Broker service to ensure that queries are directed to the appropriate services, enabling seamless access to distributed data across segments and services in the cluster.

### Query Forwarding in Druid

- **Interval Object**: Most Druid queries specify an interval object that defines the time span for which data is requested.

- **Segment Partitioning**: Druid partitions segments by time intervals, with each segment typically containing data for a specific period (e.g., a day of the week). Queries that span multiple days will thus access multiple segments.

- **Service Distribution**: These segments may be distributed across various services in the cluster. Consequently, a query may need to access data from multiple services.

### Role of the Broker Service

1. **Information Gathering**: The Broker service collects information from ZooKeeper, which maintains details about Historical and streaming ingestion Peon services and the segments they are serving.

2. **Building a Timeline**: For each datasource in ZooKeeper, the Broker constructs a timeline that maps segments to the services responsible for them.

3. **Query Lookup**:
   - When a query is received for a specific datasource and interval, the Broker looks up the associated timeline.
   - It retrieves the services that have the relevant data for the requested interval.

4. **Query Forwarding**: The Broker then forwards the query to the identified services that contain the necessary segments.

## Caching

Druid’s Broker caching mechanism enhances query performance by storing segment results while ensuring that real-time data is directly accessed, maintaining the integrity and accuracy of frequently changing information.

### Caching in Druid Broker Services

- **Cache Strategy**: 
  - Brokers use an LRU (Least Recently Used) cache invalidation strategy to manage cached data.

- **Cache Content**: 
  - The cache stores results on a per-segment basis.
  - It can be implemented as a local cache for each Broker service or as a shared external distributed cache (e.g., using memcached).

### Query Handling Process

1. **Query Reception**: When a Broker service receives a query, it first maps the query to the relevant set of segments.

2. **Cache Lookup**:
   - The Broker checks if any results for the identified segments are already available in the cache.
   - If cached results exist, they are retrieved directly.

3. **Forwarding to Historical Services**:
   - For segment results not found in the cache, the Broker forwards the query to the Historical services.
   - Once the Historical services return the results, the Broker stores these results in the cache for future queries.

### Real-Time Data Handling

- **No Caching for Real-Time Segments**: 
  - Real-time segments are not cached. Requests for real-time data are always forwarded to real-time services.
  - This approach is due to the constantly changing nature of real-time data, making caching unreliable.
