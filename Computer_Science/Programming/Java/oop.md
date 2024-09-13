## Classes and Objects:
   - A class (like `Car`) is a blueprint for creating objects.
   - Objects are instances of classes, created using the `new` keyword.
   - Classes encapsulate data (fields) and behavior (methods).
   - The `Car` class demonstrates encapsulation by using private fields and public methods.

## Inheritance
- Allows a class to inherit properties and methods from another class.
- `ElectricCar extends Car` means `ElectricCar` inherits from `Car`.
- The `super()` call in the constructor invokes the superclass constructor.
- Inheritance promotes code reuse and establishes an "is-a" relationship.

With the use of the `extends` keyword among classes, all the properties of the superclass (also known as the **Parent Class** or **Base Class**) are present in the subclass (also known as the Child Class or Derived Class)

Fields are also inherited. 

`private` fields and methods still exist within the subclass, but are not accessible:

In Java, each class may extend at most one other class.
```java
public class A{}
public class B{}
public class ExtendsTwoClasses extends A, B {} //Illegal
```

#### The Liskov Substitution Principle
Substitutability is a principle in object-oriented programming introduced by Barbara Liskov in a 1987 conference keynote stating that, if class B is a subclass of class A, then wherever A is expected, B can be used instead. 

For example,
```java
class A {...}
class B extends A {...}

public void method(A obj) {...}

A a = new B(); // Assignment OK
method(new B()); // Passing as parameter OK
```

#### Static Inheritance
Static method can be inherited similar to normal methods, however unlike normal methods it is impossible to create "abstract" methods in order to force static method overriding. Writing a method with the same signature as a static method in a super class appears to be a form of overriding, but really this simply creates a new function hides the other.

Unlike normal inheritance, in static inheritance methods are not hidden. But classes do inherit static methods if no methods with the same signature are found in the subclass. If two method's signatures vary, both methods can be run from the subclass, even if the name is the same.

Static fields hide each other in a similar way

#### Abstract Classes
An abstract class is a class marked with the abstract keyword. It, contrary to non-abstract class, may contain abstract - implementation-less - methods. It is, however, valid to create an abstract class without abstract methods.

An abstract class cannot be instantiated. It can be sub-classed (extended) as long as the sub-class is either also abstract, or implements all methods marked as abstract by super classes. However a class that extends an abstract class, and provides an implementation for all of its abstract methods and can be instantiated.

An abstract method is a method that has no implementation. Other methods can be declared within an abstract class that have implementation in order to provide common code for any sub-classes.

Example of an abstract class: 
```java
public abstract class Component {
    private int x, y;
    
    public setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void render();
}
```

### Abstract classes vs Interfaces
Abstract classes and interfaces both provide a way to define method signatures while requiring the extending/implementing class to provide the implementation.

There are two key differences between abstract classes and interfaces:

   - A class may only extend a single class, but may implement many interfaces.
   - An abstract class can contain instance (non-static) fields, but interfaces may only contain static fields.
   - Methods declared in interfaces could not contain implementations.

### Using 'final' to restrict inheritance and overridin
**Final Classes** - When used in a `class` declaration, the `final` modifier prevents other classes from being declared that `extend` the class. A `final` class is a "leaf" class in the inheritance class hierarchy.

```java
// This declares a final class
final class MyFinalClass {
    /* some code */
}

// Compilation error: cannot inherit from final MyFinalClass
class MySubClass extends MyFinalClass {
    /* more code */
}

```
Immutable classes should also be declared as `final`. An immutable class is one whose instances cannot be changed after they have been created.

Final methods are typically used when you want to restrict what a subclass can change in a class without forbidding subclasses entirely



## Polymorphism:
   - Allows objects of different types to be treated as objects of a common superclass.
   - Method overriding (like `startEngine()` in `ElectricCar`) is a form of polymorphism.
   - The `@Override` annotation ensures we're correctly overriding a superclass method.

## Interfaces:
   - Define a contract of methods that implementing classes must provide.
   - Interfaces only declare method signatures, not implementations.
   - A class can implement multiple interfaces (like `BicycleSharing implements Drivable, Recyclable`).
   - Interfaces are used to achieve abstraction and define common behavior across unrelated classes.

   Why use interfaces?
   - They allow you to define a common behavior for unrelated classes.
   - They enable you to achieve multiple inheritance of type (not implementation).
   - They help in designing loosely coupled systems, making it easier to change implementations.

A Java class can implement multiple interfaces.
```java
public interface NoiseMaker {
    String noise = "Making Noise"; // interface variables are public static final by default

    String makeNoise(); //interface methods are public abstract by default
}

public interface FoodEater {
    void eat(Food food);
}

public class Cat implements NoiseMaker, FoodEater { 
    @Override
    public String makeNoise() {
        return "meow";
    }

    @Override //This forces the compiler to check that we are overriding and prevents the program from defining a new method or messing up the method signature.
    public void eat(Food food) {
        System.out.println("meows appreciatively");
    }
}

NoiseMaker noiseMaker = new Cat(); // Valid
FoodEater foodEater = new Cat(); // Valid
Cat cat = new Cat(); // valid

Cat invalid1 = new NoiseMaker(); // Invalid
Cat invalid2 = new FoodEater(); // Invalid
```

1. All variables declared in an interface are `public static final`.
2. All methods declared in an interface methods are `public abstract` (This statement is valid only through Java 7. From Java 8, you are allowed to have methods in an interface, which need not be abstract; such methods are known as default methods.
3. Interfaces cannot be declared as `final`.
4. If more than one interface declares a method that has identical signature, then effectively it is treated as only one method and you cannot distinguish from which interface method is implemented. 
5. A corresponding InterfaceName.class file would be generated for each interface, upon compilation.

### Extending an interface
```java
public interface BasicResourceService {
    Resource getResource();
}

public interface ExtendedResourceService extends BasicResourceService {
    void updateResource(Resource resource);
}
```
Now a class implementing `ExtendedResourceService` will need to implement both `getResource()` and `updateResource()`.

## Encapsulation:
   - Bundling of data and methods that operate on that data within a single unit (class).
   - Implemented using access modifiers (private, protected, public).
   - Getter and setter methods provide controlled access to private fields.

Interfaces vs Abstract Classes: 
  - Use interfaces when you want to define a contract for unrelated classes.
  - Use abstract classes when you have a group of related classes that should share some common implementation.
- Polymorphism allows you to write more flexible and reusable code.
- Encapsulation helps in hiding implementation details and protecting data integrity.
