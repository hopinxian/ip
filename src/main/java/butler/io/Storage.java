package butler.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import butler.command.AddCommand;
import butler.exception.ButlerException;
import butler.task.DeadlineTask;
import butler.task.EventTask;
import butler.task.Task;
import butler.task.TaskList;
import butler.task.TaskType;

/**
 * Represents a storage responsible for reading and writing into the hard disk.
 * Reads a list of tasks from a given location in the hard disk and
 * stores the list of tasks into the same location within the hard disk.
 */
public class Storage {
    private String filePath;

    /**
     * Constructs a new storage with the given <code>filePath</code>.
     * The storage will read and write into the file at the given <code>filePath</code>.
     *
     * @param filePath File path of the file to be read from and written into.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Stores the given <code>taskList</code> within the <code>filePath</code>.
     *
     * @param taskList List of tasks to be written into the hard disk.
     * @throws ButlerException if file is not detected.
     */
    public void storeTaskList(TaskList taskList) throws ButlerException {
        try {
            FileWriter fw = new FileWriter(filePath);
            String fileText = "";

            int size = taskList.getSize();
            for (int i = 0; i < size; i++) {
                Task task = taskList.getTask(i);
                String taskDetails;
                TaskType taskType = task.getTaskType();

                if (task.isComplete()) {
                    taskDetails = "complete";
                } else {
                    taskDetails = "incomplete";
                }

                switch (taskType) {
                case TODO:
                    taskDetails += " todo " + task.getSummary();
                    break;

                case DEADLINE:
                    DeadlineTask deadlineTask = (DeadlineTask) task;
                    taskDetails += " deadline " + deadlineTask.getSummary()
                            + " /by " + deadlineTask.getDeadline();
                    break;

                case EVENT:
                    EventTask eventTask = (EventTask) task;
                    taskDetails += " event " + eventTask.getSummary()
                            + " /at " + eventTask.getStartDate() + " "
                            + eventTask.getEndDate();
                    break;

                default:
                    assert false : "Something is wrong. There is an unidentified task type.";
                }

                fileText += taskDetails + System.lineSeparator();
            }

            fw.write(fileText);
            fw.close();

        } catch (IOException e) {
            assert false : "There is an error with writing to the path. File is not detected.";

        } catch (ClassCastException e) {
            assert false : "Something is wrong. There is a ClassCastException.";
        }
    }

    /**
     * Loads the list of tasks from the <code>filePath</code>.
     *
     * @return A list of tasks read from the <code>filePath</code>.
     * @throws ButlerException if there is an error within the content of the file.
     */
    public ArrayList<Task> load() throws ButlerException {
        try {
            File f = new File(filePath);
            Scanner s = new Scanner(f);
            ArrayList<Task> taskList = new ArrayList<>();

            while (s.hasNext()) {
                String input = s.nextLine();

                String completionStatus = input.split(" ", 2)[0];
                String details = input.split(" ", 2)[1];

                AddCommand c = (AddCommand) Parser.parse(details);
                Task task = c.getTask();

                if (completionStatus.equals("complete")) {
                    task.markComplete();
                }
                taskList.add(task);
            }
            return taskList;

        } catch (FileNotFoundException e) {
            try {
                Files.createDirectory(Paths.get("./data/"));
                Files.createFile(Paths.get(filePath));
            } catch (IOException f) {
                assert false : "Code should never reach here." + f.getMessage();
            }
            throw new ButlerException("There is no file to access.");

        } catch (IndexOutOfBoundsException e) {
            throw new ButlerException("There is an error inside the file. The task has incomplete details.");

        } catch (ClassCastException e) {
            throw new ButlerException("Instead of tasks, a command was written into the file.");

        } catch (ButlerException e) {
            throw new ButlerException("There is an error within the task in the file.");
        }
    }
}
