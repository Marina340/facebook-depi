package profile;

public class Profile {
    private int profileID;
    private int userID;
    private String bio;
    private String profilePicture;
    private String privacyLevel;

    // Constructor
    public Profile(int userID, String bio, String profilePicture, String privacyLevel) {
        this.userID = userID;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.privacyLevel = privacyLevel;
    }

    // Getters and Setters
    public int getProfileID() { return profileID; }
    public void setProfileID(int profileID) { this.profileID = profileID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getPrivacyLevel() { return privacyLevel; }
    public void setPrivacyLevel(String privacyLevel) { this.privacyLevel = privacyLevel; }
}