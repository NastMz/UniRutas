package com.unirutas.commands.interfaces;

/**
 * Interface for command objects that encapsulate actions and support undo operations.
 */
public interface ICommand {

    /**
     * Executes the command.
     */
    void execute();

    /**
     * Undoes the previously executed command.
     */
    void undo();
}
