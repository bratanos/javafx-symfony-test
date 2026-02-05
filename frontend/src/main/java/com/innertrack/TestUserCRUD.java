package com.innertrack;

import com.innertrack.models.User;
import com.innertrack.services.UserService;
import com.innertrack.utils.DBConnection;

import java.util.List;

public class TestUserCRUD {
    public static void main(String[] args) {
        System.out.println("Starting User CRUD Test...");

        UserService userService = new UserService();

        // 1. Create a User
        User newUser = new User();
        newUser.setEmail("test_" + System.currentTimeMillis() + "@example.com");
        newUser.setPassword("password123");
        newUser.setStatus("PENDING");
        newUser.setVerified(false);

        System.out.println("Creating user: " + newUser.getEmail());
        if (userService.create(newUser)) {
            System.out.println("User created successfully with ID: " + newUser.getId());
        } else {
            System.err.println("Failed to create user.");
            return;
        }

        // 2. Read User
        int userId = newUser.getId();
        User readUser = userService.read(userId);
        System.out.println("Read user: " + readUser);

        // 3. Update User
        if (readUser != null) {
            readUser.setStatus("ACTIVE");
            readUser.setVerified(true);
            System.out.println("Updating user status to ACTIVE...");
            if (userService.update(readUser)) {
                System.out.println("User updated successfully.");
            } else {
                System.err.println("Failed to update user.");
            }
        }

        // 4. Find All
        List<User> users = userService.findAll();
        System.out.println("Total users: " + users.size());

        // 5. Delete User (Optional - commenting out to see the change in DB if needed)
        // System.out.println("Deleting user ID: " + userId);
        // if (userService.delete(userId)) {
        // System.out.println("User deleted successfully.");
        // } else {
        // System.err.println("Failed to delete user.");
        // }
    }
}
