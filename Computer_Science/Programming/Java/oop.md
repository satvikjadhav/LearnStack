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


### Reference Type in Inheritance
**Reference Type**: A variable of a superclass type can reference an object of a subclass type. Consequently, the variable can only access methods and properties defined in the superclass, unless those methods are overridden in the subclass.

```java
class User {
    private String id;

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class Student extends User {
    private String major;

    public Student(String id, String major) {
        super(id);
        this.major = major;
    }

    @Override
    public String getId() {
        return "Student ID: " + super.getId();
    }

    public String getMajor() {
        return major;
    }
}

public class Main {
    public static void main(String[] args) {
        User std = new Student("S123", "Computer Science");

        // Accessing overridden method in Student
        System.out.println("ID: " + std.getId());

        // Downcasting to access Student-specific method
        if (std instanceof Student student) {
            System.out.println("Major: " + student.getMajor());
        }
    }
}
```
**Polymorphism**: Reference types play a crucial role in polymorphism, allowing objects of different subclasses to be treated as instances of a superclass.

**What is Upcasting?**: Upcasting occurs when a subclass object is assigned to a variable of its superclass type. This allows for more general code that can operate on various subclasses.
```java
User std = new Student("S123", "Computer Science");
```
**Behavior**: The variable `std` can only access methods defined in `User`, but if methods are overridden in `Student`, those overridden methods will be called.
- We can call `std.getId()`, which will invoke the overridden method in the `Student` class, outputting `"Student ID: S123"`.
- We **cannot** call `std.getMajor()` because `std` is of type `User`, which does not have a `getMajor()` method. This results in a compile-time error if you try to uncomment that line.

**What is Downcasting?**: Downcasting is when a superclass reference is converted back to a subclass reference, allowing access to subclass-specific methods.
```java
if (std instanceof Student) {
    Student student = (Student) std; // Downcasting
    System.out.println(student.getMajor());
}
```
So in order to access the methods of the Student class, we can use downcasting. 
- The `instanceof` check ensures that `std` is indeed a `Student`.
- We then downcast `std` to `Student` using `(Student) std`, allowing you to call `getMajor()`.

**Importance of Safety Checks**: Always use the `instanceof` operator to check the actual object type before downcasting to prevent `ClassCastException`.


## Polymorphism:
   - Allows objects of different types to be treated as objects of a common superclass.
   - Method overriding (like `startEngine()` in `ElectricCar`) is a form of polymorphism.
   - The `@Override` annotation ensures we're correctly overriding a superclass method.

### Method Overriding
Method overriding is the ability of subtypes to redefine (override) the behavior of their supertypes.

In Java, this translates to subclasses overriding the methods defined in the super class. In Java, all non-primitive variables are actually `references`, which are akin to pointers to the location of the actual object in memory. The `references` only have one type, which is the type they were declared with. However, they can point to an object of either their declared type or any of its subtypes.

When a method is called on a `reference`, the corresponding method of the actual object being pointed to is invoked.

Different types of overriding:
1. Base class provides no implementation and sub-class has to override complete method - (abstract)
2. Base class provides default implementation and sub-class can change the behaviour
3. Sub-class adds extension to base class implementation by calling `super.methodName()` as first statement
4. Base class defines structure of the algorithm (Template method) and sub-class will override a part of algorithm

Code example:

```java
import java.util.HashMap;

abstract class Game implements Runnable{

    protected boolean runGame = true;
    protected Player player1 = null;
    protected Player player2 = null;
    protected Player currentPlayer = null;
    
    public Game(){
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        currentPlayer = player1;
        initializeGame();
    }

    /* Type 1: Let subclass define own implementation. Base class defines abstract method to force
        sub-classes to define implementation    
    */
    
    protected abstract void initializeGame();
    
    /* Type 2: Sub-class can change the behaviour. If not, base class behaviour is applicable */
    protected void logTimeBetweenMoves(Player player){
        System.out.println("Base class: Move Duration: player.PlayerActTime - player.MoveShownTime");
    }
    
    /* Type 3: Base class provides implementation. Sub-class can enhance base class implementation by calling
        super.methodName() in first line of the child class method and specific implementation later */
    protected void logGameStatistics(){
        System.out.println("Base class: logGameStatistics:");
    }
    /* Type 4: Template method: Structure of base class can't be changed but sub-class can some part of behaviour */
    protected void runGame() throws Exception{
        System.out.println("Base class: Defining the flow for Game:");    
        while (runGame) {
            /*
            1. Set current player
            2. Get Player Move
            */
            validatePlayerMove(currentPlayer);    
            logTimeBetweenMoves(currentPlayer);
            Thread.sleep(500);
            setNextPlayer();
        }
        logGameStatistics();
    }
    /* sub-part of the template method, which define child class behaviour */
    protected abstract void validatePlayerMove(Player p);
    
    protected void setRunGame(boolean status){
        this.runGame = status;
    }
    public void setCurrentPlayer(Player p){
        this.currentPlayer = p;
    }
    public void setNextPlayer(){
        if (currentPlayer == player1) {
            currentPlayer = player2;
        }else{
            currentPlayer = player1;
        }
    }
    public void run(){
        try{
            runGame();
        }catch(Exception err){
            err.printStackTrace();
        }
    }
}

class Player{
    String name;
    Player(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}

/* Concrete Game implementation  */
class Chess extends Game{
    public Chess(){
        super();
    }
    public void initializeGame(){
        System.out.println("Child class: Initialized Chess game");
    }
    protected void validatePlayerMove(Player p){
        System.out.println("Child class: Validate Chess move:" + p.getName());
    }
    protected void logGameStatistics(){
        super.logGameStatistics();
        System.out.println("Child class: Add Chess specific logGameStatistics:");
    }
}
class TicTacToe extends Game{
    public TicTacToe(){
        super();
    }
    public void initializeGame(){
        System.out.println("Child class: Initialized TicTacToe game");
    }
    protected void validatePlayerMove(Player p){
        System.out.println("Child class: Validate TicTacToe move:" + p.getName());
    }
}

public class Polymorphism{
    public static void main(String args[]){
        try{
        
            Game game = new Chess();
            Thread t1 = new Thread(game);
            t1.start();
            Thread.sleep(1000);
            game.setRunGame(false);
            Thread.sleep(1000);
                        
            game = new TicTacToe();
            Thread t2 = new Thread(game);
            t2.start();
            Thread.sleep(1000);
            game.setRunGame(false);
        
        }catch(Exception err){
            err.printStackTrace();
        }        
    }
}
```

#### Rules to keep in mind

To override a method in the subclass, the overriding method (i.e. the one in the subclass) MUST HAVE:

    - same name
    - same return type in case of primitives (a subclass is allowed for classes, this is also known as covariant return types).
    - same type and order of parameters
    - it may throw only those exceptions that are declared in the throws clause of the superclass's method or exceptions that are subclasses of the declared exceptions. It may also choose NOT to throw any exception. The names of the parameter types do not matter. For example, void methodX(int i) is same as void methodX(int k)
    - We are unable to Override final or Static methods. Only thing that we can do change only method body

### Method Overloading
Method overloading, also known as function overloading, is the ability of a class to have multiple methods with the same name, granted that they differ in either number or type of arguments. This type of polymorphism is called static or compile time polymorphism 

Compiler checks **method signature** for method overloading.

Overloaded methods may be static or non-static. This also does not effect method overloading.

Also if we change the return type of method, we are unable to get it as method overloading.

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
