package profile;

import java.sql.*;
import java.time.LocalDateTime;
import facebook_depi.DatabaseConnection;
import facebook_depi.UserSession;

public class ProfileDAO {

    int userId = UserSession.getInstance().getUserId();

    // Save or update a profile
    public boolean saveProfile(String bio, String profilePicture, String privacyLevel) {
        // Check if the profile already exists in the database
        Integer profileID = getProfileIDByUserID(userId);

        String query;
        if (profileID == null) {
            // Insert new profile
            query = "INSERT INTO Profile (UserID, Bio, PrivacyLevel, ImagePath, CreatedAt, UpdatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            // Update existing profile
            query = "UPDATE Profile SET Bio = ?, PrivacyLevel = ?, ImagePath = ?, UpdatedAt = ? WHERE ProfileID = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            LocalDateTime now = LocalDateTime.now();

            if (profileID == null) {
                // Insert new profile
                pstmt.setInt(1, userId); // UserID
                pstmt.setString(2, bio); // Bio
                pstmt.setString(3, privacyLevel); // PrivacyLevel
                pstmt.setString(4, profilePicture); // ImagePath
                pstmt.setTimestamp(5, Timestamp.valueOf(now)); // CreatedAt
                pstmt.setTimestamp(6, Timestamp.valueOf(now)); // UpdatedAt
            } else {
                // Update existing profile
                pstmt.setString(1, bio); // Bio
                pstmt.setString(2, privacyLevel); // PrivacyLevel
                pstmt.setString(3, profilePicture); // ImagePath
                pstmt.setTimestamp(4, Timestamp.valueOf(now)); // UpdatedAt
                pstmt.setInt(5, profileID); // ProfileID
            }

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if the profile was saved or updated
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error
            return false; // Return false if an error occurred
        }
    }

    // Helper method to get ProfileID by UserID
    private Integer getProfileIDByUserID(int userId) {
        String query = "SELECT ProfileID FROM Profile WHERE UserID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ProfileID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no profile is found
    }

    // Test the database connection
    public static void testConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Connection to the database successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}