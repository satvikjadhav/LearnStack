public class DataTypesExample {
    public static void main(String[] args) {
        // Primitive Data Types
        
        // Integer types
        byte byteVar = 127;                // 8-bit signed two's complement integer
        short shortVar = 32767;            // 16-bit signed two's complement integer
        int intVar = 2147483647;           // 32-bit signed two's complement integer
        long longVar = 9223372036854775807L; // 64-bit signed two's complement integer
        
        // Floating-point types
        float floatVar = 3.14f;            // 32-bit IEEE 754 floating-point
        double doubleVar = 3.14159265359;  // 64-bit IEEE 754 floating-point
        
        // Boolean type
        boolean booleanVar = true;         // true or false
        
        // Character type
        char charVar = 'A';                // 16-bit Unicode character
        
        // Reference Data Types
        
        // String (not a primitive type in Java)
        String stringVar = "Hello, Java!";
        
        // Arrays
        int[] intArray = {1, 2, 3, 4, 5};
        String[] stringArray = {"Java", "Python", "C++"};
        
        // Printing variables
        System.out.println("byte: " + byteVar);
        System.out.println("short: " + shortVar);
        System.out.println("int: " + intVar);
        System.out.println("long: " + longVar);
        System.out.println("float: " + floatVar);
        System.out.println("double: " + doubleVar);
        System.out.println("boolean: " + booleanVar);
        System.out.println("char: " + charVar);
        System.out.println("String: " + stringVar);
        System.out.println("int array: " + intArray[0] + ", " + intArray[1] + ", ...");
        System.out.println("String array: " + stringArray[0] + ", " + stringArray[1] + ", ...");
        
        // Type inference (Java 10+)
        var inferredInt = 100;    // Compiler infers this as int
        var inferredString = "Type inference";  // Compiler infers this as String
        
        System.out.println("Inferred int: " + inferredInt);
        System.out.println("Inferred String: " + inferredString);
    }
}