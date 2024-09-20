# MiddleManager and Peon Services

#### MiddleManager Service
- **Function**: Worker service that executes submitted tasks.
- **Task Management**: Forwards tasks to Peons running in separate JVMs.
- **Resource Isolation**: Uses separate JVMs for tasks to isolate resources and logs.
- **Concurrency**: Can manage multiple Peons, but each Peon runs only one task at a time.

#### Peon Service
- **Function**: Task execution engine spawned by the MiddleManager.
- **JVM Usage**: Each Peon runs in a separate JVM and executes a single task.
- **Hosting**: Peons run on the same host as the MiddleManager that created them.
- **Operational Context**: Peons are generally not run independently and rely on the MiddleManager for task execution.

This structure ensures efficient task execution while maintaining resource isolation and management.