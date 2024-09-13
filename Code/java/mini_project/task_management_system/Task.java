package mini_project.task_management_system;

public class Task {
    private String title;
    private String description;
    private boolean isCompleted;

    public Task (String title, String description) {
        this.title = title;
        this.description = description;
        this.isCompleted = false;
    }

    // getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    // setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(Boolean completed) {
        this.isCompleted = completed;
    }

    @Override
    public String toString() {
        return "Task: " + title + " (Completed: " + isCompleted + ")";
    }

}
