In Apache Druid, task management between the Overlord and MiddleManager happens through coordination facilitated by Apache ZooKeeper. ZooKeeper acts as a distributed coordination service that helps in maintaining the cluster's state, such as which tasks are running, where they're running, and their current status.

### How Task Management Happens:

1. **Task Assignment**:
   - When a new ingestion task is submitted to the Druid Overlord (e.g., a task to ingest data into a Druid datasource), the Overlord decides which MiddleManager should execute the task.
   - The Overlord writes the task assignment information into ZooKeeper. This includes details like the task ID, the assigned MiddleManager, and other metadata required for the task execution.

2. **Task Execution**:
   - The MiddleManager nodes continuously watch ZooKeeper for new tasks assigned to them. When a MiddleManager sees a task assigned to it, it pulls the task information from ZooKeeper and starts executing it.
   - The MiddleManager also updates ZooKeeper with the status of the task (e.g., running, completed, failed) at various stages of execution.

3. **Task Coordination and Monitoring**:
   - Throughout the task's lifecycle, the Overlord monitors the status of tasks by reading the updates from ZooKeeper. If the task fails or the MiddleManager node crashes, the Overlord can reassign the task to another MiddleManager.
   - Once a task is completed, the MiddleManager updates ZooKeeper with the final status, and the Overlord can then mark the task as complete in its internal database.

### Example of Task Management:

Suppose you submit a task to ingest log data from a Kafka topic into Druid:

1. **Task Submission**:
   - You submit a Kafka indexing task to the Overlord via Druid's API.

2. **Task Assignment**:
   - The Overlord determines which MiddleManager is best suited to handle the task and writes this assignment to ZooKeeper. For instance, it assigns the task with ID `task_123` to `MiddleManager_1`.

3. **Task Execution**:
   - `MiddleManager_1` notices that a new task (task_123) has been assigned to it by watching ZooKeeper. It retrieves the task details and begins executing the ingestion process, reading data from Kafka and storing it into Druid segments.

4. **Status Updates**:
   - During execution, `MiddleManager_1` periodically updates ZooKeeper with the task status, such as "task_123 is running, 50% complete."

5. **Task Completion**:
   - Once the task is done, `MiddleManager_1` writes a final update to ZooKeeper, marking `task_123` as complete. The Overlord reads this update and finalizes the task, making the new data available for querying.

6. **Handling Failures**:
   - If `MiddleManager_1` crashes, ZooKeeper will detect the loss of the node. The Overlord, upon seeing that `MiddleManager_1` is no longer reporting, can reassign `task_123` to another MiddleManager node to restart the task.

This mechanism ensures that tasks are reliably assigned, monitored, and completed, with ZooKeeper serving as the backbone for coordination and state management.