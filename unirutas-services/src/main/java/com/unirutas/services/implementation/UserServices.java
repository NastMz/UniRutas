package com.unirutas.services.implementation;

import com.unirutas.commands.interfaces.ICommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing user-related operations and command history.
 */
public class UserServices {

    private final List<ICommand> commandHistory = new ArrayList<>();

    /**
     * Executes the given command and adds it to the command history.
     *
     * @param command The command to be executed.
     */
    public void executeCommand(ICommand command) {
        command.execute();
        commandHistory.add(command);
    }

    /**
     * Undoes the last executed command by removing it from the command history and invoking its undo method.
     */
    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            ICommand lastCommand = commandHistory.remove(commandHistory.size() - 1);
            lastCommand.undo();
        }
    }
}
