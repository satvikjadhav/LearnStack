public class ControlFlowExample {
    public static void main(String[] args) {
        // If-else statement
        int x = 10;
        if (x > 5) {
            System.out.println("x is greater than 5");
        } else if (x == 5) {
            System.out.println("x is equal to 5");
        } else {
            System.out.println("x is less than 5");
        }

        // Switch statement
        String day = "Wednesday";
        switch (day) {
            case "Monday":
                System.out.println("Start of the work week");
                break;
            case "Wednesday":
                System.out.println("Middle of the work week");
                break;
            case "Friday":
                System.out.println("End of the work week");
                break;
            default:
                System.out.println("Another day");
        }

        // For loop
        for (int i = 0; i < 5; i++) {
            System.out.println("For loop iteration: " + i);
        }

        // While loop
        int count = 0;
        while (count < 3) {
            System.out.println("While loop iteration: " + count);
            count++;
        }

        // Do-while loop
        int j = 0;
        do {
            System.out.println("Do-while loop iteration: " + j);
            j++;
        } while (j < 2);

        // For-each loop (enhanced for loop)
        int[] numbers = {1, 2, 3, 4, 5};
        for (int num : numbers) {
            System.out.println("For-each loop: " + num);
        }

        // Break and continue
        for (int k = 0; k < 5; k++) {
            if (k == 2) {
                continue; // Skip the rest of this iteration
            }
            if (k == 4) {
                break; // Exit the loop
            }
            System.out.println("Break/continue example: " + k);
        }
    }
}