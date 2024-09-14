# ZooKeeper's Role and Interactions in Apache Druid

## Overview of ZooKeeper in Druid

ZooKeeper is a critical component in Apache Druid's architecture, serving as a distributed coordination service. It provides several key functions that are essential for maintaining the health and operational status of the Druid cluster.

## Key Functions of ZooKeeper in Druid

1. **Service Discovery**: ZooKeeper maintains an up-to-date view of which nodes are currently active in the cluster.

2. **Leader Election**: For components that require a leader (like Coordinator and Overlord), ZooKeeper manages the election process.

3. **Cluster Metadata**: Some cluster-wide configuration and status information is stored in ZooKeeper.

4. **Node Status Tracking**: ZooKeeper keeps track of the health and status of various Druid nodes.

## Detailed Interactions with Druid Components

### 1. Coordinator Nodes
- **Leader Election**: ZooKeeper ensures only one Coordinator is active at a time.
- **Historical Node Discovery**: Coordinators use ZooKeeper to discover available Historical nodes.
- **Load Queue**: ZooKeeper stores information about segments being loaded or dropped by Historical nodes.

### 2. Overlord Nodes
- **Leader Election**: Similar to Coordinators, ZooKeeper manages Overlord leader election.
- **MiddleManager Discovery**: Overlords use ZooKeeper to find available MiddleManagers for task assignment.
- **Task Status Updates**: ZooKeeper may be used to track the status of running tasks.

### 3. Broker Nodes
- **Query Routing**: Brokers use ZooKeeper to discover which Historical and MiddleManager nodes are available to serve queries.
- **Segment Discovery**: ZooKeeper helps Brokers maintain an up-to-date view of which segments are being served by which nodes.

### 4. Historical Nodes
- **Node Registration**: Historical nodes register themselves in ZooKeeper when they join the cluster.
- **Segment Serving Information**: They update ZooKeeper with information about which segments they are currently serving.

### 5. MiddleManager Nodes
- **Node Registration**: MiddleManagers register their availability in ZooKeeper.
- **Task Slot Availability**: They update ZooKeeper with information about available task slots.

### 6. Router Nodes (if used)
- **Broker Discovery**: Routers use ZooKeeper to discover available Broker nodes for query routing.

## Example Workflow: Segment Loading Process

1. **Segment Creation**:
   - A MiddleManager creates a new segment and uploads it to deep storage.
   - The MiddleManager updates the metadata store with information about the new segment.

2. **Coordinator Action**:
   - The Coordinator (leader) periodically checks the metadata store for new segments.
   - Upon discovering a new segment, it decides which Historical node should load it.

3. **ZooKeeper's Role in Historical Node Selection**:
   - The Coordinator queries ZooKeeper to get a list of currently available Historical nodes.
   - ZooKeeper provides up-to-date information on which Historical nodes are active and their current load.

4. **Load Queue Update**:
   - The Coordinator updates the load queue in ZooKeeper, indicating which Historical node should load the new segment.

5. **Historical Node Action**:
   - The chosen Historical node, which is watching its ZooKeeper load queue, sees the new load request.
   - It downloads the segment from deep storage and begins serving it.

6. **Status Update**:
   - The Historical node updates its status in ZooKeeper, indicating it's now serving the new segment.

7. **Broker Discovery**:
   - Broker nodes, which are watching ZooKeeper for changes, see the update.
   - They update their internal view of which Historical nodes are serving which segments.

8. **Query Routing**:
   - When a query comes in that involves the new segment, Brokers now know which Historical node to route that part of the query to.