A simple project to demonstrate my understanding of:

- Classes and objects
- Data types and variables
- Control flow (if-else statements, switch statements, loops)
- Object-Oriented Programming concepts (encapsulation, method creation)
- Java Collections (ArrayList)
- Basic input/output operations

### Task class

1. Class structure:
   - We define the Task class with three private fields: title, description, and isCompleted.
   - Private fields ensure encapsulation, a key OOP principle. This prevents direct access to the fields from outside the class.

2. Constructor:
   - The constructor takes title and description as parameters.
   - We initialize isCompleted to false by default, assuming new tasks are not completed when created.

3. Getters and setters:
   - We provide public methods to access and modify the private fields.
   - This approach allows us to control how the fields are accessed and modified, potentially adding validation or additional logic in the future.

4. toString() method:
   - We override the toString() method to provide a custom string representation of a Task object.
   - This is useful for displaying task information in a user-friendly format.

Reasoning:
- The Task class is designed to be a simple, self-contained representation of a task.
- By using private fields with public getters and setters, we follow the encapsulation principle, allowing for future modifications without affecting other parts of the code.
- The toString() method makes it easy to display task information, which is particularly useful in our console-based interface.

### TaskManager class

1. Class structure:
   - We use a List<Task> to store the tasks, allowing for dynamic size and easy manipulation.
   - We choose ArrayList as the implementation for its efficient random access and dynamic resizing.

2. Constructor:
   - Initializes the tasks list as an empty ArrayList.

3. addTask() and removeTask() methods:
   - Simple methods to add and remove tasks from the list.

4. markTaskAsCompleted() method:
   - Changes the completion status of a task to true.

5. getAllTasks() method:
   - Returns a new ArrayList containing all tasks.
   - We return a new list instead of the original to prevent external code from modifying our internal list (encapsulation).

6. getCompletedTasks() and getPendingTasks() methods:
   - These methods filter the tasks based on their completion status.
   - We use for-each loops to iterate through the tasks and create new lists with the filtered results.

Reasoning:
- The TaskManager class acts as a central point for managing tasks, following the Single Responsibility Principle.
- By using a List, we allow for an unlimited number of tasks and easy iteration.
- The methods are designed to be simple and focused, each performing a specific operation on the task list.
- Returning new lists in get methods ensures that the internal list cannot be modified directly, maintaining encapsulation.

### Main class:

1. Class structure:
   - We use static fields (also known as a static variable) for `taskManager` and `scanner` to make them accessible across all methods in the class.


`taskManager` and `scanner` are `static`, so they are shared across all potential instances of the Main class

#### **Shared Resource Across Methods**

- **`static TaskManager taskManager`**: 
  - The `TaskManager` instance is used to manage the tasks throughout the application. By making it static, we ensure that there is a single shared instance of `TaskManager` that all methods in the `Main` class can access. This allows us to maintain a consistent state of tasks across different operations (like adding, removing, or marking tasks).

- **`static Scanner scanner`**:
  - The `Scanner` instance is used to read input from the user. By making it static, we ensure that there is only one instance of `Scanner` for the entire lifecycle of the application. This prevents the overhead of creating and closing multiple `Scanner` instances and ensures consistent input handling.

#### **Simplicity in Access**

Static variables can be accessed directly through the class name (e.g., `Main.taskManager`) or through instance methods without needing to create an instance of the `Main` class. This simplifies the code because you don't need to pass these variables around as method parameters or manage their scope beyond the `Main` class.

#### **Consistency Across Method Calls**

Having static variables ensures that any changes made to these variables are visible across all static methods in the class. For example, if we add a task in one method, it will be available in another method because they all operate on the same `taskManager` instance.

#### **Avoiding Redundant Instantiations**

- **`static TaskManager taskManager`**:
  - If `taskManager` were not static, each time we call a method, we would need to create a new instance of `TaskManager`, which would not be ideal as you want to maintain a single, consistent list of tasks.

- **`static Scanner scanner`**:
  - If `Scanner` were not static, we would need to create a new `Scanner` object every time we need to read user input, which is both unnecessary and inefficient.

#### **Main Method Context**

The `main` method in Java is static, meaning it belongs to the class itself rather than an instance of the class. Since `main` is static, it cannot directly access instance variables or methods. Therefore, any resources or utilities that are used within `main` need to be static as well.


2. main() method:
   - Uses a while loop to keep the program running until the user chooses to exit.
   - Utilizes a switch statement to handle different user choices.

3. Helper methods (addTask(), markTaskAsCompleted(), etc.):
   - Each method handles a specific functionality of the task management system.
   - These methods interact with the user via console input/output and use the TaskManager to perform operations.

   

Reasoning:
- The Main class serves as both the entry point and the user interface for our application.
- We use a loop-based menu system for simplicity and ease of use in a console application.
- Helper methods are used to break down the functionality into manageable, focused pieces of code.
- Static methods and fields are used because this class is not intended to be instantiated; it's just a runner for our application.

Overall Project Structure:
1. Separation of Concerns: Each class has a distinct responsibility (Task represents a task, TaskManager manages tasks, Main handles user interaction).
2. Encapsulation: We use private fields and public methods to control access to data.
3. Single Responsibility Principle: Each class and method has a single, well-defined purpose.
4. Ease of Extension: This structure allows for easy addition of new features or modification of existing ones.

