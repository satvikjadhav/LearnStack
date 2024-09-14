# Detailed Apache Druid Component Functions and Interactions

## 1. Historical Nodes

### Functions:
- Store and serve queryable data segments
- Announce their online status and load status to the Coordinator
- Download segments from deep storage and load them into memory or memory-mapped storage
- Serve queries for the segments they've loaded

### Interactions:
- With Coordinator: Receive instructions on which segments to load or drop
- With Broker: Serve query requests for loaded segments
- With Deep Storage: Download segments as instructed by the Coordinator
- With ZooKeeper: Announce availability and segment serving status

### Key Concepts:
- Segment: The core data structure in Druid, containing timestamped data. 
- Segments on disk for Historical services provide the low latency querying performance Druid is known for.
- Tier: Historical nodes are grouped into tiers, often based on hardware capabilities

## 2. Broker Nodes

### Functions:
- Accept queries from clients and coordinate query execution
- Determine which Historical and MiddleManager nodes are serving relevant segments
- Merge partial results from multiple nodes into a final result set
- Maintain a segment timeline view to route queries efficiently
- Optionally cache query results to improve performance

### Interactions:
- With Clients: Receive queries and return merged results
- With Historical Nodes: Forward relevant parts of queries and receive partial results
- With MiddleManager Nodes: Query for real-time data when necessary
- With ZooKeeper: Discover available Historical and MiddleManager nodes

### Key Concepts:
- Query merging: Combining partial results from multiple nodes
- Segment timeline: A view of which segments are available and where they're located

## 3. Coordinator Nodes

### Functions:
- Manage the configuration and coordination of Historical nodes
- Load and drop segments on Historical nodes based on configured rules
- Manage segment replication and distribution across the cluster
- Balance query load across Historical nodes
- Manage segment compaction and retention policies

### Interactions:
- With Historical Nodes: Instruct to load or drop segments
- With Metadata Store: Read and write segment metadata and load rules
- With ZooKeeper: Perform leader election and track Historical node status
- With Overlord: Coordinate to handle tasks that affect segment distribution

### Key Concepts:
- Load rules: Configurations that determine how segments should be distributed
- Segment lifecycle: The process of loading, serving, and eventually dropping segments

## 4. MiddleManager Nodes

### Functions:
- Manage ingestion tasks for both streaming and batch data
- Fork task processes called "Peons" to perform ingestion
- Create new segments from ingested data
- Serve queries on real-time data before it's handed off to Historical nodes

### Interactions:
- With Overlord: Receive task assignments and report task status
- With Broker: Serve queries for real-time data
- With Deep Storage: Push newly created segments
- With Metadata Store: Update metadata for newly created segments

### Key Concepts:
- Peon: A JVM process forked by MiddleManager to run a single task
- Handoff: The process of transitioning a completed segment from real-time to historical storage

## 5. Overlord Nodes

### Functions:
- Manage the assignment of ingestion tasks to MiddleManagers
- Monitor the progress of running tasks
- Coordinate the handoff of completed segments to Historical nodes
- Handle task locking to prevent data overwrites

### Interactions:
- With MiddleManager Nodes: Assign tasks and monitor their progress
- With Metadata Store: Manage task-related metadata
- With ZooKeeper: Perform leader election and service discovery
- With Coordinator: Coordinate segment handoff and compaction tasks

### Key Concepts:
- Task: A unit of work for data ingestion or management
- Locking: Mechanism to ensure data consistency during ingestion

## 6. Router (Optional)

### Functions:
- Provide a unified entry point for queries to the Druid cluster
- Route queries to appropriate Broker nodes based on configured rules
- Perform query-level load balancing across multiple Brokers
- Optionally handle authentication and authorization

### Interactions:
- With Clients: Receive all incoming queries
- With Broker Nodes: Forward queries based on routing rules
- With ZooKeeper: Discover available Broker nodes

### Key Concepts:
- Routing rule: Configuration that determines how queries are distributed to Brokers

## 7. Deep Storage

### Functions:
- Provide permanent backup storage for all segments
- Support for various backends (S3, HDFS, local filesystem, etc.)

### Interactions:
- With MiddleManager Nodes: Receive newly created segments
- With Historical Nodes: Provide segments for loading
- With Peons: Receive segments from batch ingestion tasks

## 8. Metadata Store
### What is Stored in the Metadata Database?

1. **Datasource Metadata**:
   - Information about datasources (tables) including their schemas, dimensions, and metrics.

2. **Segment Metadata**:
   - Details about segments, including their locations, intervals, and indices.

3. **Task Metadata**:
   - Information about tasks that have been scheduled or executed, including their status and progress.

4. **Supervisors Metadata**:
   - Metadata about data ingestion tasks, including configurations and statuses for real-time ingestion supervisors.

5. **Coordinator Metadata**:
   - Information related to data distribution, including segment assignments and load statuses.

6. **Rules and Policies**:
   - Data related to data retention policies, compaction rules, and other operational policies.

### Which Services Access the Metadata Database and For What Purpose?

1. **Coordinator**:
   - **Purpose**: Manages the distribution of data and balancing of segments across the Druid cluster. It reads and writes to the metadata database to track segment availability, data load status, and to enforce data retention policies.

2. **Overlord**:
   - **Purpose**: Oversees task management and execution. It interacts with the metadata database to track the status of tasks, their configurations, and their progress.

3. **Broker**:
   - **Purpose**: Handles query routing and aggregation. While the broker service doesn't typically perform direct writes to the metadata database, it queries metadata to understand datasource schemas and segment locations for efficient query execution.

4. **Historical**:
   - **Purpose**: Serves historical data segments. Historical nodes use metadata to determine which segments they should serve and their configurations.

5. **Middle Manager**:
   - **Purpose**: Manages real-time ingestion tasks. It interacts with the metadata database to track the status of tasks and their progress.

6. **Supervisor**:
   - **Purpose**: Manages real-time ingestion jobs. Supervisors use metadata to understand the state of their tasks and to coordinate ingestion operations.

### How Metadata is Utilized:

- **Coordination and Balancing**: The Coordinator uses metadata to manage and balance segment distribution across the cluster, ensuring that the data is evenly spread and meets the retention policies.
- **Task Management**: The Overlord and Supervisors use metadata to track and manage the lifecycle of tasks, including scheduling, execution, and completion.
- **Query Execution**: Brokers use metadata to route queries to the appropriate segments and to understand the schema of the datasources involved.

## 9. ZooKeeper

### Functions:
- Provide distributed coordination for the Druid cluster
- Manage leader election for Coordinator and Overlord
- Track node status and availability
- Store certain types of cluster-wide configuration

### Interactions:
- With all nodes: Provide service discovery and cluster state information
- With Coordinator and Overlord: Manage leader election
- With Broker: Provide information about available Historical and MiddleManager nodes

These components work together to create a highly scalable and efficient real-time analytics platform. The separation of concerns allows each component to be optimized for its specific role, while the interactions between components ensure data consistency and query performance across the entire cluster.