package user;


public class UserSession {
    private static UserSession instance; // Singleton instance
    private int userId; // Logged-in user's ID
    private String username; // Logged-in user's name
    private String userEmail; // Logged-in user's email

    // Private constructor to enforce singleton pattern
    private UserSession() {}

    // Get the singleton instance
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Set the logged-in user's information
    public void setUser(int userId, String username, String userEmail) {
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
    }

    // Clear the session (for logout)
    public void clearUser() {
        this.userId = 0;
        this.username = null;
        this.userEmail = null;
    }

    // Getters for user information
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    // Check if a user is logged in
    public boolean isLoggedIn() {
        return userId != 0;
    }
}