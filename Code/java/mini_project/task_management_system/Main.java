package mini_project.task_management_system;

import java.util.Scanner;
import java.util.List;

public class Main {
    private static TaskManager taskManager = new TaskManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    markTaskAsCompleted();
                    break;
                case 3:
                    removeTask();
                    break;
                case 4: 
                    viewAllTasks();
                    break;
                case 5:
                    viewCompletedTasks();
                    break;
                case 6:
                    viewPendingTasks();
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    private static void printMenu() {
        System.out.println("\n--- Task Management System ---");
        System.out.println("1. Add a new task");
        System.out.println("2. Mark a task as completed");
        System.out.println("3. Remove a task");
        System.out.println("4. View all tasks");
        System.out.println("5. View completed tasks");
        System.out.println("6. View pending tasks");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addTask() {
        System.out.println("Enter Task Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        Task task = new Task(title, description);
        taskManager.addTask(task);
        System.out.println("Task added successfully!");
    }

    private static void markTaskAsCompleted() {
        List<Task> pendingTasks = taskManager.getPendingTasks();
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks to mark as completed.");
            return;
        }

        System.out.println("Pending tasks:");
        for (int i = 0; i < pendingTasks.size(); i++) {
            System.out.println((i + 1) + ". " + pendingTasks.get(i).getTitle());
        }

        System.out.print("Enter the number of the task to mark as completed: ");
        int taskNumber = scanner.nextInt();
        scanner.nextLine();

        if (taskNumber > 0 && taskNumber <= pendingTasks.size()) {
            Task task = pendingTasks.get(taskNumber -1);
            taskManager.markTaskAsCompleted(task);
            System.out.println("Task marked as completed!");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    private static void removeTask() {
        List<Task> allTasks = taskManager.getAllTasks();
        if (allTasks.isEmpty()) {
            System.out.println("No tasks to remove.");
            return;
        }

        System.out.println("All tasks:");
        for (int i = 0; i < allTasks.size(); i++) {
            System.out.println((i + 1) + ". " + allTasks.get(i).getTitle());
        }

        System.out.print("Enter the number of the task to remove: ");
        int taskNumber = scanner.nextInt();
        scanner.nextLine();

        if (taskNumber > 0 && taskNumber <= allTasks.size()) {
            Task task = allTasks.get(taskNumber - 1);
            taskManager.removeTask(task);
            System.out.println("Task removed successfully!");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    private static void viewAllTasks() {
        List<Task> allTasks = taskManager.getAllTasks();
        if (allTasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("All tasks:");
            for (Task task : allTasks) {
                System.out.println(task);
            }
        }
    }

    private static void viewCompletedTasks() {
        List<Task> completedTasks  = taskManager.getCompletedTasks();
        if (completedTasks.isEmpty()) {
            System.out.println("No completed tasks found.");
        } else {
            System.out.println("Completed tasks:");
            for (Task task : completedTasks) {
                System.out.println(task);
            }
        }
    }

    private static void viewPendingTasks() {
        List<Task> pendingTasks = taskManager.getPendingTasks();
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks found.");
        } else {
            System.out.println("Pending tasks:");
            for (Task task : pendingTasks) {
                System.out.println(task);
            }
        }
    }
}
