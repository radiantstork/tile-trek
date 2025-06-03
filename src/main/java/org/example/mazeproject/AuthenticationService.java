package org.example.mazeproject;

public class AuthenticationService {
    private final UserService userService = UserService.getInstance();

    public AuthenticationService() {}

    public User login(String username, String password) {
        if (username.isBlank()) throw new IllegalArgumentException("Username missing");
        if (password.isBlank()) throw new IllegalArgumentException("Password missing");

        User user = userService.getByUsername(username);
        if (user == null) throw new IllegalArgumentException("User not found");
        if (!user.getPassword().equals(password)) throw new IllegalArgumentException("Incorrect password");

        return user;
    }

    public User register(String username, String password, String confirmPassword) {
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank())
            throw new IllegalArgumentException("All fields required");

        if (!password.equals(confirmPassword))
            throw new IllegalArgumentException("Passwords do not match");

        if (userService.getByUsername(username) != null)
            throw new IllegalArgumentException("Username already exists");

        User user = new User(username, password);
        userService.create(user);
        return userService.getByUsername(username);
    }
}

