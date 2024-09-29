package mini_project.library_management_system;

import java.util.List;
import java.util.Scanner;

public class LibraryManager {
    private Library library;
    private Scanner scanner;

    public LibraryManager() {
        this.library = new Library("Learn Java");
        this.scanner = new Scanner(System.in);
    }
    
    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int input = getIntInput("Enter your choics: ");

            switch (input) {
                case 1 -> addBook();
                case 2 -> removeBook();
                case 3 -> searchBooks();
                case 4 -> addUser();
                case 5 -> removeUser();
                case 6 -> borrowBook();
                case 7 -> returnBook();
                case 8 -> listAvaliableBooks();
                case 9 -> listBorrowedBooks();
                case 10 -> searchUser();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void printMenu() {
        System.out.println("\n--- Library Management System ---");
        System.out.println("1. Add a book");
        System.out.println("2. Remove a book");
        System.out.println("3. Search for books");
        System.out.println("4. Add a user");
        System.out.println("5. Remove a user");
        System.out.println("6. Borrow a book");
        System.out.println("7. Return a book");
        System.out.println("8. List available books");
        System.out.println("9. List borrowed books");
        System.out.println("10. Search for Users");
        System.out.println("0. Exit");
    }

    private String getStringInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void addBook() {
        String title = getStringInput("Enter Book Title: ");
        String author = getStringInput("Enter Book Author: ");
        String isbn = getStringInput("Enter Book ISBN: ");

        Book book = new Book(title, author, isbn);
        library.addBook(book);
    }

    private void removeBook() {
        String isbn = getStringInput("Enter ISBN of the Book to remove: ");
        Book book = library.findBook(isbn);

        //remove book
        if (book != null) {
            library.removeBook(book);
        } else {
            System.out.println("Book not found.");
        }
    }

    private void searchBooks() {
        String query = getStringInput("Enter search query: ");
        List<Book> results = library.searchBooks(query);
        if (results.isEmpty()) {
            System.out.println("No books found matching the query.");
        } else {
            System.out.println("Search results:");
            for (Book book : results) {
                System.out.println(book);
            }
        }
    }

    private void searchUser() {
        String query = getStringInput("Enter the User Id: ");
        User user = library.findUser(query);

        if (user != null) {
            System.out.println("Search results: ");
            System.out.println(user.toString());
        } else {
            System.out.println("No user with this Id exists.");
        }
    }

    private void addUser() {
        String name = getStringInput("Enter User name: ");
        String id = getStringInput("Enter User id: ");

        User user = new User(name, id);

        library.addUser(user);
    }

    private void removeUser() {
        String id = getStringInput("Enter Id of the user to remove: ");

        User user = library.findUser(id);

        if (user != null) {
            library.removeUser(user);
        } else {
            System.out.println("User not found.");
        }
    }

    private void borrowBook() {
        String userId = getStringInput("Enter Id of the user who wants to borrow a book: ");
        User user = library.findUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;         
        }

        String bookISBN = getStringInput("Enter the ISBN of the book to borrow: ");
        Book book = library.findBook(bookISBN);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        user.borrowBook(book);
    }

    private void returnBook() {
        String userId = getStringInput("Enter Id of the user who wants to return a book: ");
        User user = library.findUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;         
        }

        String bookISBN = getStringInput("Enter the ISBN of the book to return: ");
        Book book = library.findBook(bookISBN);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        user.returnBook(book);
    }

    private void listAvaliableBooks() {
        System.out.println("Here are the avaliable books: ");
        
        List<Book> avaliableBooks = library.getAvaliableBooks();

        for (Book book : avaliableBooks) {
            System.out.println(book);
        }
    }

    private void listBorrowedBooks() {
        System.out.println("Here are the borrowed books: ");

        List<Book> borroweBooks = library.getBorrowBooks();

        for (Book book : borroweBooks) {
            System.out.println(book);
        }
    }

    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();
        manager.run();
    }

}
