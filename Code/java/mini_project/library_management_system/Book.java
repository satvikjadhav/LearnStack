package mini_project.library_management_system;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private Boolean isAvailable;

    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.isAvailable = true;
    }

    //getters
    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getISBN() {
        return this.ISBN;
    }

    public Boolean isAvailable() {
        return this.isAvailable;
    }

    public void borrow() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Sorry, this book is not available.");
        }
    }

    public void returnBook() {
        if (!isAvailable) {
            isAvailable = true;
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("This book is already in the library.");
        }
    }

    @Override
    public String toString() {
        return "Book{" +
        "title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", ISBN='" + ISBN + '\'' +
        ", isAvailable=" + isAvailable +
        '}';
    }
    
}
