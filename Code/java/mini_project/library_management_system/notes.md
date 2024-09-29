# Library Management System

## Project Overview

The Library Management System is a Java-based application that simulates the basic operations of a library. It allows users to manage books and library members, handle book borrowing and returning, and perform searches on the book inventory.

## Class Structure

### Book
Represents a book in the library.
- Attributes: title, author, ISBN, availability status
- Methods: borrow(), returnBook()

### User
Represents a library member.
- Attributes: name, ID, list of borrowed books
- Methods: borrowBook(), returnBook()

### Library
Manages the collection of books and users.
- Attributes: list of books, list of users
- Methods: addBook(), removeBook(), addUser(), removeUser(), searchBooks(), findBook(), findUser(), getAvailableBooks(), getBorrowedBooks()

### LibraryManager
Main class that handles user interactions and program flow.
- Methods: run(), various methods for menu options

## Key Concepts and Their Implementation

### Object-Oriented Programming (OOP)

#### Encapsulation
- All classes use private attributes with public getter methods.
- Example: In the `Book` class, `title`, `author`, `ISBN`, and `isAvailable` are private, with public getter methods.

#### Inheritance
- While not explicitly used in this basic version, the project structure allows for easy implementation of inheritance.
- Potential extension: Create subclasses of `Book` like `FictionBook` or `ReferenceBook`.

#### Polymorphism
- Not explicitly implemented in this version but can be added.
- Potential implementation: Different types of users (e.g., `Student`, `Faculty`) with overridden `borrowBook()` methods.

#### Abstraction
- Each class represents an abstract concept (Book, User, Library) with well-defined interfaces.
- The internal workings of methods like `borrowBook()` are hidden from other classes.

### SOLID Principles

#### Single Responsibility Principle
- Each class has a single, well-defined responsibility.
- Example: `Library` manages the collection, while `LibraryManager` handles user interactions.

#### Open/Closed Principle
- The design allows for extension without modifying existing code.
- Example: New book types or user types can be added without changing the `Library` class.

#### Liskov Substitution Principle
- While not explicitly demonstrated, the design allows for it.
- Potential implementation: Subclasses of `Book` or `User` could be used interchangeably with their base classes.

#### Interface Segregation Principle
- Not explicitly implemented but considered in the design.
- The classes have focused methods that aren't forced to implement unnecessary functionality.

#### Dependency Inversion Principle
- The `LibraryManager` depends on abstractions (`Library`, `Book`, `User`) rather than concrete implementations.

### Java-Specific Concepts

#### Collections Framework
- `ArrayList` is used in the `Library` class to store books and users.
- `List` interface is used for method parameters and return types, allowing for flexibility.

#### Stream API
- Used in the `Library` class for searching and filtering books.
- Example: `searchBooks()` method uses streams to filter books based on a query.

#### Lambda Expressions
- Used in conjunction with streams for concise function definitions.
- Example: In `searchBooks()` method for defining filter criteria.

#### Exception Handling
- Basic exception handling is implemented in `LibraryManager` for user input.
- Example: `getIntInput()` method catches `NumberFormatException`.

### Software Engineering Best Practices

#### Modular Design
- Each class has a specific role, and functionalities are divided into methods.
- `LibraryManager` implements each menu option as a separate method.

#### User Input Validation
- `LibraryManager` includes methods for validating and sanitizing user input.

#### Separation of Concerns
- UI logic (`LibraryManager`) is separated from business logic (`Library`, `Book`, `User`).

#### Defensive Programming
- Null checks and input validation are used throughout the code.
- Example: Checking if a book or user exists before performing operations.

## Potential Improvements and Extensions

1. Implement data persistence (file I/O or database integration).
2. Add more advanced features like due dates, fines, and reservations.
3. Implement a graphical user interface (GUI).
4. Add unit tests for all classes and methods.
5. Implement logging for better debugging and monitoring.
6. Enhance search functionality with more advanced algorithms.
7. Add authentication and authorization features.

## Conclusion

This Library Management System demonstrates fundamental OOP concepts, SOLID principles, and Java-specific features. It provides a solid foundation for learning software engineering best practices and can be extended to include more advanced features and robust error handling.