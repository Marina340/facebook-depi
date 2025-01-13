package facebook_depi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;

public class UserDAO {
	public User login(String userEmail, String password) {

        User user = null;

        String query = "SELECT * FROM user WHERE Email = ? AND Password = ?";
        String hashedPassword = PasswordUtil.hashPassword(password);
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set values for the prepared statement
            preparedStatement.setString(1, userEmail);
            preparedStatement.setString(2, hashedPassword);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.print(resultSet);
            // Process the result set
            if (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getInt("UserID"));
                user.setUsername(resultSet.getString("Name"));
                user.setUserEmail(resultSet.getString("Email"));
                user.setPassword(resultSet.getString("Password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return user;
    }
	public User Register(String userName,String userEmail, String password) {

        User user = null;
        String hashedPassword = PasswordUtil.hashPassword(password);
        // Insert user into the database using hashedPassword
        String query = "INSERT INTO user (Name, Email, Password) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userEmail);
            preparedStatement.setString(3, hashedPassword);

            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) { // Now this will work
                    if (generatedKeys.next()) {
                       int userId = generatedKeys.getInt(1); // Assuming userId is the first column
                        user = new User();
                        user.setUserId(userId);
                        user.setUsername(userName);
                        user.setUserEmail(userEmail);
                        user.setPassword(hashedPassword); // Store the hashed password (optional)
                    }
                }
            }
            }
        catch (SQLException e) {
            // Check for duplicate email error
            if (e.getErrorCode() == 1062) { // Duplicate entry error code for MySQL
                showErrorAlert("Registration Error", "The email " + userEmail + " is already registered.");
            } else {
                e.printStackTrace(); // Print other SQL exceptions to the console
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print any other exceptions to the console
        }
        
        return user;
    }
	private void showErrorAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	}
