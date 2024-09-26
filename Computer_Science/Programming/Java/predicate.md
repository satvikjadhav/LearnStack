When you define a `Predicate` using a lambda expression, you are essentially providing an implementation for the `test()` method of the `Predicate<T>` interface.

### How It Works

The `Predicate` interface looks like this:

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}
```

When you create a `Predicate` with a lambda expression, you're defining the behavior of the `test()` method. For example:

```java
Predicate<Book> matchesQuery = book -> 
    book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
    book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
    book.getISBN().contains(query);
```

In this case, the lambda expression `book -> ...` serves as the implementation for the `test(Book book)` method of the `Predicate` interface. Whenever the `matchesQuery.test(book)` is called, it executes the logic defined in the lambda.

### Implicit Overriding

In Java, when you use a lambda expression for a functional interface (like `Predicate`), the compiler automatically generates an implementation of that interface. So, you're not explicitly writing out the `test()` method, but you're providing its behavior through the lambda, which the compiler translates into an instance of the `Predicate` interface.

The lambda expression provides a concrete implementation of the single abstract method defined in a functional interface (`Predicate` in this case).

When you define a `Predicate` using a lambda expression, the Java compiler recognizes that the lambda corresponds to the single abstract method (`test`) in the `Predicate` interface. Here’s how this happens step-by-step:

1. **Defining the Predicate**: When you write a lambda expression like this:

   ```java
   Predicate<Book> matchesQuery = book -> 
       book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
       book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
       book.getISBN().contains(query);
   ```

   You're essentially saying: "Create an instance of `Predicate<Book>` where the behavior of the `test` method is defined by this lambda."

2. **Lambda as Implementation**: The lambda expression `book -> ...` is treated as the body of the `test` method. Whenever you call `matchesQuery.test(book)`, it executes the logic defined in the lambda.

### Behind the Scenes

Internally, the Java compiler does a few things when it sees a lambda expression:

- It creates a class that implements the `Predicate` interface.
- It automatically provides the implementation for the `test` method with the logic you defined in the lambda.

So, you don’t see this code directly, but it’s happening under the hood. Essentially, your lambda expression is shorthand for a full implementation of the `test` method.

### Example of Explicit Implementation

If you wanted to implement the `Predicate` interface explicitly (without using a lambda), it would look something like this:

```java
Predicate<Book> matchesQuery = new Predicate<Book>() {
    @Override
    public boolean test(Book book) {
        return book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
               book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
               book.getISBN().contains(query);
    }
};
```

In this case, you're directly overriding the `test()` method. However, using a lambda expression is much cleaner and more concise.

### Summary

So, when you define a `Predicate` with a lambda, you are indeed providing the implementation for the `test()` method implicitly. This is one of the benefits of using functional programming features in Java—making your code more concise and readable! If you have more questions, feel free to ask!