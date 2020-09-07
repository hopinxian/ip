package butler.task;

import butler.exception.ButlerException;

import java.util.ArrayList;

/**
 * Represents a list of tasks.
 */
public class TaskList {
    private ArrayList<Task> taskList;

    /**
     * Constructs a pre-filled list of tasks.
     * The list of tasks is given by <code>taskList</code>.
     *
     * @param taskList List of tasks to populate list with.
     */
    public TaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Constructs an empty list of tasks.
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Gets the number of tasks in this list of tasks.
     *
     * @return Number of tasks in this list of tasks.
     */
    public int getSize() {
        return taskList.size();
    }

    /**
     * Gets the task at the specified <code>index</code>.
     *
     * @param taskIndex Index of the task.
     * @return Task at the specified <code>index</code>.
     */
    public Task getTask(int taskIndex) {
        return taskList.get(taskIndex);
    }

    /**
     * Adds the given <code>task</code> to this list of tasks.
     *
     * @param task Task to be added to this list.
     */
    public void addTask(Task task) {
        taskList.add(task);
    }

    /**
     * Marks the task at the specified <code>index</code> as complete.
     * Valid indexes range from 1 to the size of this list of tasks.
     *
     * @param taskIndex Index of the task to mark as completed.
     * @throws ButlerException if the index is out of range.
     */
    public void completeTask(int taskIndex) throws ButlerException {
        try {
            taskList.get(taskIndex - 1).markComplete();
        } catch (IndexOutOfBoundsException e) {
            throw new ButlerException("Please give a valid index. \""
                    + taskIndex + "\" is not a valid index.");
        }
    }

    /**
     * Removes the task at the specified <code>index</code> from this list.
     *
     * @param taskIndex Index of the task to be removed.
     * @throws ButlerException if the index is out of range.
     */
    public void deleteTask(int taskIndex) throws ButlerException {
        try {
            taskList.remove(taskIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new ButlerException("Please give a valid index. \""
                    + taskIndex + "\" is not a valid index.");
        }
    }

    /**
     * Produces a sub list of tasks that contains the given <code>keyword</code>.
     *
     * @param keyword Keyword by which tasks are filtered by.
     * @return A list of tasks that contains the given <code>keyword</code>.
     */
    public TaskList findTasks(String keyword) {
        ArrayList<Task> filteredList = new ArrayList<>();
        for (Task t : taskList) {
            if (t.getSummary().contains(keyword)) {
                filteredList.add(t);
            }
        }
        return new TaskList(filteredList);
    }
}
