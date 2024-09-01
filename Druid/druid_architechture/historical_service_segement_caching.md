## [Loading and serving segments from cache](https://druid.apache.org/docs/latest/design/historical/#loading-and-serving-segments-from-cache)

1. Segment Cache and Memory Mapping:
   The Historical service in Apache Druid uses a technique called memory mapping for its segment cache. This means it can load parts of data files (segments) directly into the computer's memory, making it faster to access this data when needed.

2. Memory Usage:
   This cache uses the operating system's memory, not just the memory allocated to the Java program (JVM) running Druid. It's affected by various factors like the size of the Historical JVM, memory buffers, and other programs running on the same computer.

3. Query Process:
   When a query comes in, the Historical service checks if the required data is already in the memory-mapped cache. If it is, great! It can read it directly from memory, which is very fast. If not, it has to read from the disk, which is slower.

4. Memory Management:
   There's a constant juggling act happening with the memory. When new data is read from disk, it might push out older data from the memory cache. The more free memory your system has (up to a certain limit set by `druid.server.maxSize`), the more likely it is that the data you need will already be in memory, leading to faster queries.

5. Additional Caching:
   This memory-mapped cache is just one layer of caching. Druid also has other caches at the query level for further performance improvements.

##Practical Example

Let's say you're running a large e-commerce website and using Druid to analyze sales data. You have a Historical node with 64GB of RAM, and you've set `druid.server.maxSize` to 48GB.

1. Your sales data is divided into hourly segments. Each segment is about 1GB in size.

2. A user runs a query to analyze sales from the last 24 hours. The Historical service needs to access 24 segments (24GB of data).

3. If your system has been running for a while and has enough free memory, most or all of these 24 segments might already be in the memory-mapped cache. In this case, the query will run very quickly because all data is read from memory.

4. Now, let's say another user runs a query for sales from 3 days ago. This data isn't in the memory cache, so the Historical has to read it from disk. As it reads this older data into memory, it might push out some of the more recent data.

5. If the same user (or another user) then queries the last 24 hours again, some of that data might now have to be re-read from disk, making this query slightly slower than the first time.

6. Over time, frequently accessed segments will tend to stay in memory, while less frequently accessed ones will be read from disk when needed.

This system allows Druid to balance between serving recent/frequent queries very quickly (by keeping that data in memory) and still being able to handle queries for older data, albeit with a potential performance trade-off.