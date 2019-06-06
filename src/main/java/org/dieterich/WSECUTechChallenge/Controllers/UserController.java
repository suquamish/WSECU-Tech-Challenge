package org.dieterich.WSECUTechChallenge.Controllers;

import org.dieterich.WSECUTechChallenge.DataAccess.UserService;
import org.dieterich.WSECUTechChallenge.Exceptions.DuplicateUserException;
import org.dieterich.WSECUTechChallenge.Exceptions.NothingFoundException;
import org.dieterich.WSECUTechChallenge.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    public User createUser(@RequestBody User userData) throws DuplicateUserException {
        return userService.createUser(userData.getUsername(), userData.getName(), userData.getEmail());
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) throws NothingFoundException {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/id/{userId}")
    public User getUserById(@PathVariable String userId) throws NothingFoundException {
        return userService.getUserById(userId);
    }

    @PostMapping("/id/{userId}")
    public User updateUser(@RequestBody User userData, @PathVariable String userId) throws NothingFoundException {
        userData.setId(userId);
        userService.updateUser(userData);
        return userService.getUserById(userData.getId());
    }

    @DeleteMapping("/id/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUserById(userId);
    }

    @ExceptionHandler(NothingFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserError handleNotFoundException(NothingFoundException nfe) {
        UserError error = new UserError();
        error.setErrorMessage("Cannot find any matching user");
        error.setStatusString("Not Found");
        return error;
    }

    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserError handleDuplicateUserException(DuplicateUserException nfe) {
        UserError error = new UserError();
        error.setErrorMessage("User data provided must be unique");
        error.setStatusString("Unable to complete");
        return error;
    }

    protected class UserError {
        private String statusString;
        private String errorMessage;

        protected UserError() {}

        public UserError setStatusString(String statusString) {
            this.statusString = statusString;
            return this;
        }

        public String getStatusString() {
            return statusString;
        }

        public UserError setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
