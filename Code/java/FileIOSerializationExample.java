import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

// Serializable class
// Since we don't define public or private, the class will have 
// package-private access by default. This means that the class is accessible only to 
// other classes within the same package.

class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Persons{name='" + name + "', age=" + age + "}";
    }
}

public class FileIOSerializationExample {
    public static void main(String[] args) {
        // File I/O using java.io
        fileIOExample();

        // File I/O using java.nio.file -- for Java 7+
        nioFileIOExample();

        // Object Serialization and Deserialization
        serializationExample();
    }

    private static void fileIOExample() {
        // Writing to a file
        try (FileWriter writer = new FileWriter("example.txt")) {
            writer.write("Hello, File I/O!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reading from a file
        try (BufferedReader reader = new BufferedReader(new FileReader("example.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Read: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void nioFileIOExample() {
        Path path = Paths.get("nio_example.txt");

        // code to write the file if it doesn't exist
        try {
            Files.createFile(path);
            System.out.println("File created: " + path);
        } catch (IOException e) {
            System.out.println("File already exists!");
        }

        // Writing to a file
        try {
            Files.write(path, "Hello, NIO File I/O".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reading from a file
        try {
            List<String> lines = Files.readAllLines(path);
            for (String string : lines) {
                System.out.println("NIO Read: " + string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void serializationExample() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Bob", 26));
        people.add(new Person("Joe", 27));

        // Serialization
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("people.ser"))) {
            oos.writeObject(people);
            System.out.println("Serialized people to file");            
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialization
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("people.ser"))) {
            @SuppressWarnings("unchecked")
            List<Person> deserializedPeople = (List<Person>) ois.readObject();
            System.out.println("Deserialized people from file");

            for (Person person : deserializedPeople) {
                System.out.println(person);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
