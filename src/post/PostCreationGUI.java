package post;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import facebook_depi.UserSession;

public class PostCreationGUI {

    private VBox postsContainer; // Container to hold all posts
    private String imagePath = ""; // Path to the selected image
    private Stage primaryStage;

    public PostCreationGUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initialize();
    }

    private void initialize() {
        primaryStage.setTitle("Posts");

        // Main layout
        BorderPane mainLayout = new BorderPane();

        // Button to add a new post
        Button addPostButton = new Button("Add Post");
        addPostButton.setOnAction(e -> openAddPostWindow(primaryStage));

        // Container to hold posts
        postsContainer = new VBox(10);
        postsContainer.setPadding(new Insets(10));
        postsContainer.setStyle("-fx-background-color: #f0f2f5;"); // Light background color like Facebook

        // Load and display posts
        loadPosts();

        // Add components to the main layout
        mainLayout.setTop(addPostButton);
        mainLayout.setCenter(new ScrollPane(postsContainer));

        // Set the scene
        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to load and display posts
    private void loadPosts() {
        postsContainer.getChildren().clear(); // Clear existing posts

        PostDAO postDAO = new PostDAO();
        List<Post> posts = postDAO.getPostsForCurrentUser();

        for (Post post : posts) {
            // Create a post card
            VBox postCard = new VBox(10);
            postCard.setStyle("-fx-background-color: white; -fx-border-color: #dddfe2; -fx-border-width: 1; -fx-padding: 15;");
            postCard.setMaxWidth(Double.MAX_VALUE); // Make the post card take the full width

            // User info section (profile picture, username, and date/time)
            HBox userInfoBox = new HBox(10);
            userInfoBox.setAlignment(Pos.CENTER_LEFT);

            // Profile picture placeholder (you can replace this with an actual image)
            ImageView profilePicture = new ImageView(new Image("https://via.placeholder.com/40")); // Placeholder image
            profilePicture.setFitWidth(40);
            profilePicture.setFitHeight(40);
            profilePicture.setStyle("-fx-border-radius: 20;"); // Make the profile picture round

            // Username and date/time
            VBox userDetailsBox = new VBox(2);
            Label userLabel = new Label(UserSession.getInstance().getUsername());
            userLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            Label dateLabel = new Label(post.getPostDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd • HH:mm")));
            dateLabel.setStyle("-fx-text-fill: #606770; -fx-font-size: 12;");

            userDetailsBox.getChildren().addAll(userLabel, dateLabel);

            // Spacer to push the MenuButton to the right
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Three-dots menu for edit and delete options
            MenuButton optionsMenu = new MenuButton();
            optionsMenu.setStyle("-fx-background-color: transparent; -fx-padding: 5;"); // Add padding
            ImageView dotsIcon = new ImageView(new Image("https://via.placeholder.com/20")); // Placeholder for three dots icon
            dotsIcon.setFitWidth(20);
            dotsIcon.setFitHeight(20);
            optionsMenu.setGraphic(dotsIcon);

            // Edit option
            MenuItem editItem = new MenuItem("Edit");
            editItem.setGraphic(new ImageView(new Image("https://via.placeholder.com/15"))); // Placeholder for edit icon
            editItem.setOnAction(e -> openEditPostWindow(post));

            // Delete option
            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setGraphic(new ImageView(new Image("https://via.placeholder.com/15"))); // Placeholder for delete icon
            deleteItem.setOnAction(e -> confirmDeletePost(post));

            optionsMenu.getItems().addAll(editItem, deleteItem);

            // Add profile picture, user details, spacer, and options menu to the user info box
            userInfoBox.getChildren().addAll(profilePicture, userDetailsBox, spacer, optionsMenu);

            // Post content
            Label contentLabel = new Label(post.getContent());
            contentLabel.setWrapText(true);
            contentLabel.setStyle("-fx-font-size: 14;");

            // Post image (if available)
            VBox imageBox = new VBox();
            if (post.getImagePath() != null && !post.getImagePath().isEmpty()) {
                ImageView imageView = new ImageView(new Image(new File(post.getImagePath()).toURI().toString()));
                imageView.setFitWidth(500); // Set a fixed width for the image
                imageView.setPreserveRatio(true);
                imageView.setStyle("-fx-border-radius: 5; -fx-border-color: #dddfe2; -fx-border-width: 1;");
                imageBox.getChildren().add(imageView);
            }

            // Add components to the post card
            postCard.getChildren().addAll(userInfoBox, contentLabel, imageBox);

            // Add the post card to the container
            postsContainer.getChildren().add(postCard);
        }
    }

    // Method to confirm post deletion
    private void confirmDeletePost(Post post) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Post");
        alert.setHeaderText("Are you sure you want to delete this post?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deletePost(post); // Proceed with deletion if the user confirms
            }
        });
    }

    // Method to delete a post
    private void deletePost(Post post) {
        PostDAO postDAO = new PostDAO();
        boolean isDeleted = postDAO.deletePost(post.getPostID());

        if (isDeleted) {
            loadPosts(); // Refresh the posts list
        } else {
            showAlert("Error", "Failed to delete post.");
        }
    }

    // Method to open the "Edit Post" window
    private void openEditPostWindow(Post post) {
        Stage editPostStage = new Stage();
        editPostStage.setTitle("Edit Post");

        // Layout for the "Edit Post" window
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Text area for post content
        TextArea contentArea = new TextArea(post.getContent());
        contentArea.setPromptText("What's on your mind?");
        contentArea.setWrapText(true);

        // Button to upload a new image
        Button uploadImageButton = new Button("Upload Image");
        ImageView imagePreview = new ImageView();
        imagePreview.setFitWidth(200);
        imagePreview.setPreserveRatio(true);

        if (post.getImagePath() != null && !post.getImagePath().isEmpty()) {
            imagePreview.setImage(new Image(new File(post.getImagePath()).toURI().toString()));
        }

        uploadImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(editPostStage);
            if (selectedFile != null) {
                imagePath = selectedFile.getAbsolutePath();
                imagePreview.setImage(new Image(selectedFile.toURI().toString()));
            }
        });

        // Button to submit the edited post
        Button submitButton = new Button("Save Changes");
        submitButton.setOnAction(e -> {
            String content = contentArea.getText();
            if (content.isEmpty() && imagePath.isEmpty()) {
                showAlert("Error", "Post cannot be empty.");
            } else {
                PostDAO postDAO = new PostDAO();
                boolean isUpdated = postDAO.updatePost(post.getPostID(), content, imagePath);

                if (isUpdated) {
                    editPostStage.close(); // Close the "Edit Post" window
                    loadPosts(); // Refresh the posts list
                } else {
                    showAlert("Error", "Failed to update post.");
                }
            }
        });

        // Add components to the layout
        layout.getChildren().addAll(contentArea, uploadImageButton, imagePreview, submitButton);

        // Set the scene
        Scene scene = new Scene(layout, 400, 300);
        editPostStage.setScene(scene);
        editPostStage.show();
    }

    // Method to open the "Add Post" window
    private void openAddPostWindow(Stage primaryStage) {
        Stage addPostStage = new Stage();
        addPostStage.setTitle("Add Post");

        // Layout for the "Add Post" window
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Text area for post content
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("What's on your mind?");
        contentArea.setWrapText(true);

        // Button to upload an image
        Button uploadImageButton = new Button("Upload Image");
        ImageView imagePreview = new ImageView();
        imagePreview.setFitWidth(200);
        imagePreview.setPreserveRatio(true);

        uploadImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(addPostStage);
            if (selectedFile != null) {
                imagePath = selectedFile.getAbsolutePath();
                imagePreview.setImage(new Image(selectedFile.toURI().toString()));
            }
        });

        // Button to submit the post
        Button submitButton = new Button("Post");
        submitButton.setOnAction(e -> {
            String content = contentArea.getText();
            if (content.isEmpty() && imagePath.isEmpty()) {
                showAlert("Error", "Post cannot be empty.");
            } else {
                PostDAO postDAO = new PostDAO();
                boolean isCreated = postDAO.createPost(content, imagePath);

                if (isCreated) {
                    addPostStage.close(); // Close the "Add Post" window
                    loadPosts(); // Refresh the posts list
                } else {
                    showAlert("Error", "Failed to create post.");
                }
            }
        });

        // Add components to the layout
        layout.getChildren().addAll(contentArea, uploadImageButton, imagePreview, submitButton);

        // Set the scene
        Scene scene = new Scene(layout, 400, 300);
        addPostStage.setScene(scene);
        addPostStage.show();
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