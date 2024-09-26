package mini_project.library_management_system;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Library {
    private String name;
    private List<User> users;
    private List<Book> books;

    public Library(String name) {
        this.name = name;
        this.users = new ArrayList<>();
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Added book: " + book.getTitle());
    }

    public void removeBook(Book book) {
        if (books.remove(book)) {
            System.out.println("Removed book: " + book.getTitle());
        } else {
            System.out.println("Book not found: " + book.getTitle());
        }
    }

    public void addUser(User user) {
        users.add(user);
        System.out.println("Added user: " + user.getName());
    }

    public void removeUser(User user) {
        if (users.remove(user)) {
            System.out.println("Removed user: " + user.getName());
        } else {
            System.out.println("User not found: " + user.getName());
        }
    }

    public List<Book> searchBooks(String query) {
        return books.stream().filter(
            book -> book.getTitle().toLowerCase().contains(query.toLowerCase()) || 
                    book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                    book.getISBN().contains(query)).collect(Collectors.toList());
    }

    // public Book findBook (String isbn) {
    //     for (Book book : books) {
    //         if (book.getISBN().equals(isbn)) {
    //             return book;
    //         } else {
    //             System.out.println("This book does not exist");
    //         }
    //     }
    //     return null;
    // }

    public Book findBook (String isbn) {
        return books.stream()
                .filter(book -> book.getISBN().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    public User findUser (String id) {
        return users.stream()
                    .filter(user -> user.getId().equals(id))
                    .findFirst()
                    .orElse(null);
    }

    public List<Book> getAvaliableBooks() {
        return books.stream()
                    .filter(Book::isAvailable) // book -> book.isAvailable()
                    .collect(Collectors.toList());
    }

    public List<Book> getBorrowBooks() {
        return books.stream()
                    .filter(book -> !book.isAvailable())
                    .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Library{" +
                "books=" + books.size() +
                ", users=" + users.size() +
                '}';
    }
}
