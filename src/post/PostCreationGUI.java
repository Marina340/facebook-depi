package post;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;

public class PostCreationGUI {

    private String imagePath = ""; // Stores the path of the selected image

    // Method to display the Post Creation GUI
    public void display(Stage primaryStage) {
        primaryStage.setTitle("Create Post");

        // Text Area for Post Content
        TextArea postTextArea = new TextArea();
        postTextArea.setPromptText("What's on your mind?");
        postTextArea.setWrapText(true);

        // Button to Upload Image
        Button uploadImageButton = new Button("Upload Image");
        uploadImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                imagePath = selectedFile.getAbsolutePath();
                System.out.println("Image Selected: " + imagePath);
            }
        });

        // Button to Submit Post
        Button submitPostButton = new Button("Post");
        submitPostButton.setOnAction(e -> {
            String postText = postTextArea.getText();
            if (postText.isEmpty() && imagePath.isEmpty()) {
                showAlert("Error", "Post cannot be empty.");
            } else {
                // Create a Post object using the Builder
              Post post = new Post.Builder(0, 1, LocalDateTime.now()) // Replace 0 and 1 with actual IDs
                        .setContent(postText)
                        .setImagePath(imagePath)
                        .build();

                savePostToDatabase(post); // Save post to database
                showAlert("Success", "Post created successfully!");
                postTextArea.clear();
                imagePath = ""; // Reset image path
            }
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(postTextArea, uploadImageButton, submitPostButton);

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to save post to database (placeholder)
    private void savePostToDatabase(Post post) {
        // TODO: Implement database logic to save the post
        System.out.println("Saving post to database:");
        System.out.println("Post ID: " + post.getPostID());
        System.out.println("User ID: " + post.getUserID());
        System.out.println("Content: " + post.getContent());
        System.out.println("Post Date and Time: " + post.getPostDate());
        System.out.println("Image Path: " + post.getImagePath());
    }

    // Method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}