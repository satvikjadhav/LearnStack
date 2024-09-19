# Deep Storage

Deep storage in Apache Druid is a crucial mechanism for storing segments, but it's not provided by Druid itself. It ensures data durability; as long as Druid processes can access it, data remains safe regardless of node failures. However, if segments are lost from deep storage, the corresponding data is also lost. Deep storage can also be queried directly, and how segments are stored—either primarily in deep storage or in combination with Historical processes—depends on your configured load rules.

## Deep Storage Options

- **Supported Options**: Druid supports multiple deep storage options, including:
  - Cloud blob storage (Amazon S3, Google Cloud Storage, Azure Blob Storage)
  - S3-compatible storage (e.g., Minio)
  - HDFS

- **Local Storage Use Cases**: 
  - Suitable for:
    - Single server environments.
    - Multiple servers with access to a shared filesystem (e.g., NFS).
  - **Recommendation**: For multi-server production clusters, prefer cloud-based or HDFS options for better scalability and robustness.

- **Local Storage Configuration** (in `common.runtime.properties`):
  - **Properties**:
    - `druid.storage.type`: Must be set to `local`.
    - `druid.storage.storageDirectory`: Specify any local directory for segment storage (must differ from `druid.segmentCache.locations` and `druid.segmentCache.infoDir`).
    - `druid.storage.zip`: Indicates whether segments are stored as directories (`false`) or zip files (`true`).
  - **Defaults**:
    - `druid.storage.storageDirectory`: `/tmp/druid/localStorage`
    - `druid.storage.zip`: `false`

- **Example Configuration**:
  ```properties
  druid.storage.type=local
  druid.storage.storageDirectory=/tmp/druid/localStorage
  ```

- **Note**: Ensure `druid.storage.storageDirectory` is distinct from `druid.segmentCache.locations` and `druid.segmentCache.infoDir`.