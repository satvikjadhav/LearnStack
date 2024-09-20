# Router Service
Router services provide a unified API gateway in front of Brokers, Overlords, and Coordinators and routes the relevant requests to them.

- **Routing Mechanism**:
  - Based on preconfigured data retention rules.
  - Recent data (e.g., last month) is routed to a dedicated set of Brokers.
  - Older data is routed to a different set, ensuring query isolation.
- **Use Case**: Necessary primarily for Druid clusters in the terabyte range.
- **Additional Role**: Hosts a web console for:
  - Loading data
  - Managing datasources and tasks
  - Viewing server status and segment information

This structure helps maintain performance by isolating queries based on data importance.