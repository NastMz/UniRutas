package com.unirutas.commands.implementations;

import com.unirutas.commands.interfaces.ICommand;
import com.unirutas.models.User;

/**
 * Command implementation for changing the password of a user.
 */
public class ChangePasswordCommand implements ICommand {

    private final User user;
    private final String newPassword;
    private String oldPassword;

    /**
     * Creates a new instance of ChangePasswordCommand.
     *
     * @param user        The user for whom the password should be changed.
     * @param newPassword The new password to be set for the user.
     */
    public ChangePasswordCommand(User user, String newPassword) {
        this.user = user;
        this.newPassword = newPassword;
    }

    /**
     * Executes the command by changing the user's password and storing the old password for undo.
     */
    @Override
    public void execute() {
        oldPassword = user.getPassword();
        user.changePassword(newPassword);
    }

    /**
     * Undoes the command by reverting the user's password to the old password.
     */
    @Override
    public void undo() {
        user.changePassword(oldPassword);
    }
}
