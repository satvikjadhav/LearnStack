# Java Collections Framework
A crucial part of Java that provides a set of interfaces and classes for storing and manipulating groups of objects. It's somewhat analogous to Python's built-in data structures and the collections module, but with some important differences.

### **List Interface** 
A **List** is an ordered collection that allows duplicates. It maintains the insertion order and provides positional access. Lists can grow or shrink dynamically, and elements can be accessed by their index.

```java
// List example
List<String> arrayList = new ArrayList<>();
arrayList.add("Apple");
arrayList.add("Banana");
arrayList.add("Cherry");
System.out.println("ArrayList: " + arrayList);

List<String> linkedList = new LinkedList<>(arrayList);
linkedList.add("Date");
System.out.println("LinkedList: " + linkedList);
```
- **ArrayList**: 
  - Internally backed by a dynamic array.
  - Good for random access (O(1) time complexity for `get()` and `set()`).
  - Slower for adding/removing elements in the middle of the list because elements need to be shifted (O(n) time complexity for `add()` in the middle).
  - Amortized O(1) for adding elements to the end.

- **LinkedList**:
  - Doubly-linked list implementation.
  - A linked list consists of nodes, where each node contains two main parts:
    - Data: The value or data held by the node (in this case, a `String`).
    - Pointer (or Reference): A reference to the next node in the list.
  - Fast insertions and deletions (O(1) at the beginning or end).
  - Sequential Access: To access elements, you need to traverse the list from the head node. This is typically O(n)O(n) time complexity for accessing an element by index.
  - Useful when frequent insertions and deletions are required and when we do not need random access by index.

**Key Operations**:
- `add()`, `remove()`, `get()`, `set()`, `contains()`.
  
**Notes**:
- Similar to Python's `list`.
- Use `ArrayList` for most cases where random access is needed, and `LinkedList` when frequent insertions and deletions at the beginning or middle are expected.

---

### **Set Interface**
A **Set** is a collection that does not allow duplicates. It can be unordered or sorted.

```java
// Set example
Set<Integer> hashSet = new HashSet<>();
hashSet.add(1);
hashSet.add(2);
hashSet.add(2);  // Duplicate, won't be added
System.out.println("HashSet: " + hashSet);

Set<Integer> treeSet = new TreeSet<>();
treeSet.addAll(Arrays.asList(3, 1, 2, 4, 2));
System.out.println("TreeSet (sorted): " + treeSet);
```

- **HashSet**: 
  - Backed by a **HashMap** internally.
  - Unordered and provides constant time performance for basic operations like `add()`, `remove()`, and `contains()` (O(1) on average).
  - Best choice when order is not important and you want high performance.

- **TreeSet**: 
  - Backed by a **Red-Black tree** (a balanced binary search tree).
  - Sorted in natural order (ascending), or can use a custom `Comparator`.
  - Operations like `add()`, `remove()`, and `contains()` take O(log n) time.
  - Use when sorted order is required.

- **LinkedHashSet**: 
  - Maintains insertion order.
  - Slightly slower than `HashSet` due to maintaining the linked list of entries.
  - Use when the order of insertion is important.

**Key Operations**:
- `add()`, `remove()`, `contains()`.

**Notes**:
- Similar to Python's `set`.
- Best choice depends on whether you need sorting (`TreeSet`), insertion order (`LinkedHashSet`), or just fast lookups (`HashSet`).

---

### **Map Interface**
A **Map** stores key-value pairs, where each key is unique. Keys cannot contain duplicates, but values can.

```java
// Map example
Map<String, Integer> hashMap = new HashMap<>();
hashMap.put("One", 1);
hashMap.put("Two", 2);
hashMap.put("Three", 3);
System.out.println("HashMap: " + hashMap);
```
- **HashMap**: 
  - Backed by a **Hash table**.
    - a data structure that implements an associative array, allowing for fast data retrieval
    - 
  - **Hashing**: The keys are hashed using the hash function, which determines the index in an internal array where the key and value pair will be stored. This makes lookups very efficient.
  - **Null Values**: A HashMap allows one null key and multiple null values.
  - Provides O(1) time complexity for basic operations (`put()`, `get()`, `remove()`) on average.
  - Unordered.
  - Suitable for most use cases where order doesn’t matter.

This is an example of a `Hash Table`: 
```yaml
| HashMap                 |
|-------------------------|
| Index 0: (key1, value1) |
| Index 1: (key2, value2) |
| Index 2: null           |
| Index 3: (key3, value3) |
| Index 4: (key4, value4) |
```

- **TreeMap**: 
  - Implements a **NavigableMap** using a **Red-Black tree**.
  - Sorted by the keys in natural order, or by a custom `Comparator`.
  - Operations like `put()`, `get()`, and `remove()` take O(log n) time.
  - Useful when you need sorted keys.

- **LinkedHashMap**: 
  - Maintains the order of insertion.
  - Slightly slower than `HashMap` because it maintains a linked list of entries.
  - Can be used when the order of elements needs to be preserved or when you want to implement a cache (using `removeEldestEntry`).

**Key Operations**:
- `put()`, `get()`, `remove()`, `containsKey()`.

**Notes**:
- Similar to Python's `dict`.
- Use `HashMap` when performance is key and order is not important, `TreeMap` for sorted keys, and `LinkedHashMap` when insertion order matters.

---

### **Queue Interface**
A **Queue** is used to hold elements prior to processing and typically follows **FIFO (First-In-First-Out)** ordering.

```java
// Queue example
Queue<String> queue = new LinkedList<>();
queue.offer("First");
queue.offer("Second");
queue.offer("Third");
System.out.println("Queue: " + queue);
System.out.println("Queue poll: " + queue.poll());
System.out.println("Queue after poll: " + queue);
```
- **LinkedList**: 
  - Implements both the `Queue` and `Deque` interfaces.
  - Can be used as a **FIFO** queue or **LIFO** stack.
  - Good when frequent insertions and removals are needed at both ends.

- **PriorityQueue**: 
  - A queue where elements are ordered based on their **natural ordering** or by a provided `Comparator`.
  - Typically used in algorithms where elements are processed based on priority (e.g., Dijkstra’s algorithm).

**Key Operations**:
- `offer()`, `poll()`, `peek()`.

**Notes**:
- Python’s `queue.Queue` is similar, but Java offers more specialized queue implementations such as `PriorityQueue` and `Deque`.

---

### **Deque Interface**
A **Deque** (Double-Ended Queue) allows elements to be added or removed from both ends.

```java
// Deque example
Deque<String> deque = new ArrayDeque<>();
deque.addFirst("Front");
deque.addLast("Back");
System.out.println("Deque: " + deque);
```

- **ArrayDeque**: 
  - Resizable array-based implementation.
  - No capacity restrictions, but it may grow as needed.
  - More efficient than `LinkedList` for adding and removing elements at both ends (O(1)).
  - Does not allow null elements.

**Key Operations**:
- `addFirst()`, `addLast()`, `removeFirst()`, `removeLast()`.

**Notes**:
- Similar to Python’s `collections.deque`.

---

### **Generics**
Generics provide compile-time type safety and allow you to create classes, interfaces, and methods with a placeholder for types and that operate on types specified by the user.

- Prevents `ClassCastException` at runtime by catching incorrect types at compile time.

Example:
```java
public class Box<T> {
    private T item;
    public void setItem(T item) {
        this.item = item;
    }
    public T getItem() {
        return item;
    }
}
```
In this example, `T` is a type parameter that can be replaced with any object type when an instance of `Box` is created.

---

### **Iterator**
An **Iterator** provides a way to traverse a collection, removing elements safely during iteration.

```java
// Using Iterator
Iterator<Map.Entry<String, Integer>> iterator = hashMap.entrySet().iterator();
while (iterator.hasNext()) {
    Map.Entry<String, Integer> entry = iterator.next();
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```
- **Iterator**:
  - Provides `hasNext()`, `next()`, and `remove()` methods.
  - Allows safe removal during traversal.

- **Enhanced for-loop**:
  - Simplified syntax for iterating over collections.
  - Does not allow element removal.

**Notes**:
- In Python, you use iterators or the `for` loop, which can work with any iterable object.

---

### **Collections Utility Class**
The `Collections` class provides static utility methods for common operations on collections like sorting, searching, and shuffling.

- **Collections.sort()**:
  - Sorts a list in-place based on the natural order or by a custom `Comparator`.

- **Collections.reverse()**, **Collections.shuffle()**, **Collections.binarySearch()**:
  - Various utility methods for working with collections.

**Notes**:
- Similar to Python’s built-in `sorted()` and `reversed()` methods but modifies the collection in-place instead of returning a new one.

---

###  Differences Between Java and Python Collections:
1. **Type Safety**: Java collections enforce static typing through generics, while Python collections are dynamically typed.
2. **Mutability**: Java collections (e.g., `List`, `Set`, `Map`) are mutable by default, whereas Python has immutable types like `tuple`.
3. **Interfaces and Implementations**: Java uses interfaces (`List`, `Set`, `Map`) with multiple implementations, providing flexibility based on the use case, while Python uses built-in types.
4. **Sorting**: Java’s `Collections.sort()` sorts in-place, while Python’s `sorted()` returns a new sorted list.
