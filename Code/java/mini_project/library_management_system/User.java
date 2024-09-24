package mini_project.library_management_system;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String id;
    private List<Book> borrowedBooks;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<Book> getBorrowedBooks() {
        return new ArrayList<>(borrowedBooks); // Return a copy to preserve encapsulation
    }

    public void borrowBook(Book book) {
        if (book.isAvailable()) {
            book.borrow();
            borrowedBooks.add(book);
            System.out.println(name + " has borrowed: " + book.getTitle());
        } else {
            System.out.println("Sorry, " + book.getTitle() + " is not available for borrowing.");
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook();
            borrowedBooks.remove(book);
            System.out.println(name + " has returned: " + book.getTitle());
        } else {
            System.out.println(name + " did not borrow: " + book.getTitle());
        }
    }

    
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }
}
