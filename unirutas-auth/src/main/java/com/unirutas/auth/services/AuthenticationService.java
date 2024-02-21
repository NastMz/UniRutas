package com.unirutas.auth.services;

import com.flexcore.dependency.annotations.Inject;
import com.unirutas.commands.implementations.ChangePasswordCommand;
import com.unirutas.commands.interfaces.ICommand;
import com.unirutas.models.User;
import com.unirutas.services.implementation.UserServices;

/**
 * Service responsible for authentication-related operations, including password changes.
 */
public class AuthenticationService {

    @Inject
    private UserServices userServices;

    /**
     * Changes the password for the given user.
     *
     * @param user         The user for whom the password should be changed.
     * @param newPassword  The new password to be set for the user.
     */
    public void changePassword(User user, String newPassword) {
        // Create an instance of the concrete class implementing the ICommand interface
        ICommand changePasswordCommand = new ChangePasswordCommand(user, newPassword);
        // Execute the command
        userServices.executeCommand(changePasswordCommand);
    }
}
