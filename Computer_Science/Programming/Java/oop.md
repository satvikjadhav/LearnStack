1. Classes and Objects:
   - A class (like `Car`) is a blueprint for creating objects.
   - Objects are instances of classes, created using the `new` keyword.
   - Classes encapsulate data (fields) and behavior (methods).
   - The `Car` class demonstrates encapsulation by using private fields and public methods.

2. Inheritance:
   - Allows a class to inherit properties and methods from another class.
   - `ElectricCar extends Car` means `ElectricCar` inherits from `Car`.
   - The `super()` call in the constructor invokes the superclass constructor.
   - Inheritance promotes code reuse and establishes an "is-a" relationship.

3. Polymorphism:
   - Allows objects of different types to be treated as objects of a common superclass.
   - Method overriding (like `startEngine()` in `ElectricCar`) is a form of polymorphism.
   - The `@Override` annotation ensures we're correctly overriding a superclass method.

4. Interfaces:
   - Define a contract of methods that implementing classes must provide.
   - Interfaces only declare method signatures, not implementations.
   - A class can implement multiple interfaces (like `BicycleSharing implements Drivable, Recyclable`).
   - Interfaces are used to achieve abstraction and define common behavior across unrelated classes.

   Why use interfaces?
   - They allow you to define a common behavior for unrelated classes.
   - They enable you to achieve multiple inheritance of type (not implementation).
   - They help in designing loosely coupled systems, making it easier to change implementations.

5. Abstract Classes:
   - Cannot be instantiated, serve as base classes for other classes.
   - Can have both abstract methods (without implementation) and concrete methods (with implementation).
   - Abstract classes are used when you want to provide a common base implementation for a group of related classes.

6. Encapsulation:
   - Bundling of data and methods that operate on that data within a single unit (class).
   - Implemented using access modifiers (private, protected, public).
   - Getter and setter methods provide controlled access to private fields.

Interfaces vs Abstract Classes: 
  - Use interfaces when you want to define a contract for unrelated classes.
  - Use abstract classes when you have a group of related classes that should share some common implementation.
- Polymorphism allows you to write more flexible and reusable code.
- Encapsulation helps in hiding implementation details and protecting data integrity.
