# Metadata Store vs ZooKeeper in Apache Druid

While both the Metadata Store and ZooKeeper store metadata in Druid, they serve different purposes and have distinct characteristics.

## Metadata Store

### Purpose:
The Metadata Store is used for persistent, long-term storage of metadata that is essential for the functioning of Druid.

### Characteristics:
- Persistent storage
- Supports complex queries
- Handles larger volumes of data
- Typically implemented using a relational database (MySQL, PostgreSQL, Derby)

### Data Stored:
- Segment metadata (locations, timestamps, dimensions, metrics)
- Datasource configurations
- Ingestion task metadata
- Retention policies
- Load rules
- Audit history

### Services that Interact with Metadata Store:
1. Coordinator: Reads and writes segment metadata, load rules
2. Overlord: Manages task metadata
3. Historical Nodes: Read segment metadata during startup
4. MiddleManager Nodes: Update segment metadata after ingestion
5. Broker Nodes: May query for segment metadata

## ZooKeeper

### Purpose:
ZooKeeper is used for distributed coordination, real-time status updates, and ephemeral metadata storage.

### Characteristics:
- Provides real-time notifications
- Handles smaller volumes of data
- Optimized for read-heavy workloads
- Stores ephemeral (temporary) data

### Data Stored:
- Cluster topology (active nodes)
- Leader election status
- Real-time node status updates
- Load queue information (segments being loaded/dropped)
- Some configuration data

### Services that Interact with ZooKeeper:
1. Coordinator: Leader election, Historical node discovery
2. Overlord: Leader election, MiddleManager discovery
3. Broker: Service discovery, real-time segment information
4. Historical Nodes: Register availability, update segment serving status
5. MiddleManager Nodes: Register availability, update task slot status
6. Router (if used): Broker discovery

## Key Differences

1. **Persistence**: Metadata Store is for long-term, persistent storage, while ZooKeeper often holds more transient, operational data.

2. **Update Frequency**: ZooKeeper data is updated more frequently and is used for real-time coordination. Metadata Store updates are less frequent and often tied to batch operations.

3. **Data Volume**: Metadata Store can handle larger volumes of data, while ZooKeeper is designed for smaller amounts of frequently accessed data.

4. **Query Complexity**: Metadata Store supports complex queries, while ZooKeeper is optimized for simple read/write operations.

5. **Notification Mechanism**: ZooKeeper provides a built-in watch mechanism for real-time updates, which the Metadata Store does not.

## Workflow Example

Let's consider the lifecycle of a segment to illustrate how both systems are used:

1. **Segment Creation**:
   - MiddleManager creates a new segment
   - Updates Metadata Store with segment information
   - Updates ZooKeeper with its current task status

2. **Segment Distribution**:
   - Coordinator reads new segment info from Metadata Store
   - Queries ZooKeeper for available Historical nodes
   - Updates ZooKeeper's load queue with distribution decision

3. **Segment Loading**:
   - Historical node watches ZooKeeper's load queue
   - Loads segment based on ZooKeeper notification
   - Updates ZooKeeper with its current segment serving status
   - May read additional segment metadata from Metadata Store

4. **Query Handling**:
   - Broker reads segment topology from ZooKeeper
   - May query Metadata Store for additional segment information
   - Routes queries to appropriate Historical nodes based on ZooKeeper data

5. **Segment Dropout**:
   - Coordinator decides to drop a segment based on rules in Metadata Store
   - Updates ZooKeeper's load queue
   - Historical node drops segment and updates ZooKeeper
   - Coordinator updates Metadata Store to reflect segment dropout