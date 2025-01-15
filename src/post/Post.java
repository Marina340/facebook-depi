package post;
import java.util.Date;
import java.time.LocalDateTime;

public class Post {
    private int postID;
    private int userID;
    private String content;
    private LocalDateTime postDate;
    private String imagePath;

    // Private constructor to enforce the use of the Builder
    private Post(Builder builder) {
        this.postID = builder.postID;
        this.userID = builder.userID;
        this.content = builder.content;
        this.postDate = builder.postDate;
        this.imagePath = builder.imagePath;
    }

    // Getters
    public int getPostID() { return postID; }
    public int getUserID() { return userID; }
    public String getContent() { return content; }
    public LocalDateTime getPostDate() { return postDate; }
    public String getImagePath() { return imagePath; }

    // Builder Class
    public static class Builder {
        // Required fields
        private int postID;
        private int userID;
        private LocalDateTime postDate;

        // Optional fields (with default values)
        private String content = ""; // Default value for content
        private String imagePath = ""; // Default value for imagePath

        // Constructor for required fields
        public Builder(int postID, int userID, LocalDateTime postDate) {
            this.postID = postID;
            this.userID = userID;
            this.postDate = postDate;
        }

        // Setter for content (optional)
        public Builder setContent(String content) {
            this.content = content;
            return this; // Return the Builder instance for method chaining
        }

        // Setter for imagePath (optional)
        public Builder setImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this; // Return the Builder instance for method chaining
        }

        // Build method to create the Post object
        public Post build() {
            return new Post(this);
        }
    }
}