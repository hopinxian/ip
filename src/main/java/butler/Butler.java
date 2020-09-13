package butler;

import butler.command.Command;
import butler.exception.ButlerException;
import butler.io.Parser;
import butler.io.Storage;
import butler.io.Ui;
import butler.task.TaskList;

/**
 * Represents a butler that manages a list of tasks for the user.
 *
 * Butler maintains a list of tasks across different sessions.
 * Data of the list of tasks are saved in hard disk within <code>filePath</code>
 * relative to the program file location.
 */
public class Butler {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a butler that stores tasks in the <code>filePath</code>.
     *
     * @param filePath Location where tasks is stored.
     */
    public Butler(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (ButlerException e) {
            tasks = new TaskList();
        }
    }

    /**
     * Gets a response message.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            return c.execute(tasks, ui, storage);
        } catch (ButlerException e) {
            return ui.showError(e.getMessage());
        }
    }
}
