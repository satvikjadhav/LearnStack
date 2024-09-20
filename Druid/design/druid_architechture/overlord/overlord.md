# Overlord Service

- **Primary Responsibilities**:
  - Accepts tasks and coordinates their distribution.
  - Creates locks around tasks to manage execution.
  - Returns task statuses to callers.

- **Operating Modes**:
  - **Local Mode** (default):
    - The Overlord also creates Peons for task execution.
    - Requires all MiddleManager and Peon configurations to be specified.
    - Suitable for simple workflows.
  
  - **Remote Mode**:
    - Overlord and MiddleManager run as separate services, allowing them to be hosted on different servers.
    - Recommended for using the indexing service as a single endpoint for all Druid indexing tasks.
