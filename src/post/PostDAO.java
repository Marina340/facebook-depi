package post;

import facebook_depi.DatabaseConnection;
import facebook_depi.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    // Method to create a new post
    public boolean createPost(String content, String imagePath) {
        if (!UserSession.getInstance().isLoggedIn()) {
            System.err.println("User is not logged in.");
            return false;
        }

        // Ensure that either content or imagePath is provided
        if ((content == null || content.trim().isEmpty()) && (imagePath == null || imagePath.trim().isEmpty())) {
            System.err.println("Post cannot be empty. Provide either content or an image.");
            return false;
        }

        int userId = UserSession.getInstance().getUserId(); // Get the logged-in user's ID
        LocalDateTime timestamp = LocalDateTime.now();

        String query = "INSERT INTO Post (Content, Timestamp, UserID, ImagePath) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, content);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(timestamp));
            preparedStatement.setInt(3, userId);
            preparedStatement.setString(4, imagePath);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; // Return true if the post was successfully created

        } catch (SQLException e) {
            System.err.println("Error creating post: " + e.getMessage());
            return false;
        }
    }

    // Method to retrieve all posts for the logged-in user
    public List<Post> getPostsForCurrentUser() {
        List<Post> posts = new ArrayList<>();

        if (!UserSession.getInstance().isLoggedIn()) {
            System.err.println("User is not logged in.");
            return posts;
        }

        int userId = UserSession.getInstance().getUserId(); // Get the logged-in user's ID

        String query = "SELECT * FROM Post WHERE UserID = ? ORDER BY Timestamp DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int postId = resultSet.getInt("PostID");
                String content = resultSet.getString("Content");
                LocalDateTime timestamp = resultSet.getTimestamp("Timestamp").toLocalDateTime();
                String imagePath = resultSet.getString("ImagePath");

                Post post = new Post.Builder(postId, userId, timestamp)
                        .setContent(content)
                        .setImagePath(imagePath)
                        .build();
                posts.add(post);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving posts: " + e.getMessage());
        }

        return posts;
    }

    // Method to retrieve a specific post by its ID
    public Post getPostById(int postId) {
        Post post = null;

        if (!UserSession.getInstance().isLoggedIn()) {
            System.err.println("User is not logged in.");
            return null;
        }

        String query = "SELECT * FROM Post WHERE PostID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("UserID");
                String content = resultSet.getString("Content");
                LocalDateTime timestamp = resultSet.getTimestamp("Timestamp").toLocalDateTime();
                String imagePath = resultSet.getString("ImagePath");

                post = new Post.Builder(postId, userId, timestamp)
                        .setContent(content)
                        .setImagePath(imagePath)
                        .build();
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving post: " + e.getMessage());
        }

        return post;
    }

    // Method to update a post
    public boolean updatePost(int postId, String content, String imagePath) {
        if (!UserSession.getInstance().isLoggedIn()) {
            System.err.println("User is not logged in.");
            return false;
        }

        // Ensure that either content or imagePath is provided
        if ((content == null || content.trim().isEmpty()) && (imagePath == null || imagePath.trim().isEmpty())) {
            System.err.println("Post cannot be empty. Provide either content or an image.");
            return false;
        }

        int userId = UserSession.getInstance().getUserId(); // Get the logged-in user's ID

        String query = "UPDATE Post SET Content = ?, ImagePath = ? WHERE PostID = ? AND UserID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, content);
            preparedStatement.setString(2, imagePath);
            preparedStatement.setInt(3, postId);
            preparedStatement.setInt(4, userId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; // Return true if the post was successfully updated

        } catch (SQLException e) {
            System.err.println("Error updating post: " + e.getMessage());
            return false;
        }
    }

    // Method to delete a post
    public boolean deletePost(int postId) {
        if (!UserSession.getInstance().isLoggedIn()) {
            System.err.println("User is not logged in.");
            return false;
        }

        int userId = UserSession.getInstance().getUserId(); // Get the logged-in user's ID

        String query = "DELETE FROM Post WHERE PostID = ? AND UserID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, postId);
            preparedStatement.setInt(2, userId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; // Return true if the post was successfully deleted

        } catch (SQLException e) {
            System.err.println("Error deleting post: " + e.getMessage());
            return false;
        }
    }
}