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

            // Like and Comment buttons
            HBox buttonsBox = new HBox(10);
            buttonsBox.setAlignment(Pos.CENTER_LEFT);

            // Like button
            Button likeButton = new Button("Like");
            likeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #385898; -fx-font-weight: bold;");
            Label likeCountLabel = new Label("0"); // Placeholder for like count
            likeCountLabel.setStyle("-fx-text-fill: #606770; -fx-font-size: 12;");

            // Track whether the current user has liked the post
            boolean[] isLiked = {false}; // Using an array to make it effectively final for the lambda

            likeButton.setOnAction(e -> {
                if (isLiked[0]) {
                    // If already liked, unlike the post
                    likeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #385898; -fx-font-weight: bold;");
                    int currentLikes = Integer.parseInt(likeCountLabel.getText());
                    likeCountLabel.setText(String.valueOf(currentLikes - 1));
                } else {
                    // If not liked, like the post
                    likeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #1877f2; -fx-font-weight: bold;");
                    int currentLikes = Integer.parseInt(likeCountLabel.getText());
                    likeCountLabel.setText(String.valueOf(currentLikes + 1));
                }
                isLiked[0] = !isLiked[0]; // Toggle the like state
            });

            // Comment button
            Button commentButton = new Button("Comment");
            commentButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #385898; -fx-font-weight: bold;");
            Label commentCountLabel = new Label("0"); // Placeholder for comment count
            commentCountLabel.setStyle("-fx-text-fill: #606770; -fx-font-size: 12;");

            commentButton.setOnAction(e -> openCommentWindow(post, commentCountLabel));

            buttonsBox.getChildren().addAll(likeButton, likeCountLabel, commentButton, commentCountLabel);

            // Add components to the post card
            postCard.getChildren().addAll(userInfoBox, contentLabel, imageBox, buttonsBox);

            // Add the post card to the container
            postsContainer.getChildren().add(postCard);
        }
    }

    // Method to open the comment window
    private void openCommentWindow(Post post, Label commentCountLabel) {
        Stage commentStage = new Stage();
        commentStage.setTitle("Comments");

        // Main layout for the comment window
        BorderPane mainLayout = new BorderPane();

        // Original post components
        VBox postCard = new VBox(10);
        postCard.setStyle("-fx-background-color: white; -fx-border-color: #dddfe2; -fx-border-width: 1; -fx-padding: 15;");

        // User info section (profile picture, username, and date/time)
        HBox userInfoBox = new HBox(10);
        userInfoBox.setAlignment(Pos.CENTER_LEFT);

        // Profile picture placeholder
        ImageView profilePicture = new ImageView(new Image("https://via.placeholder.com/40"));
        profilePicture.setFitWidth(40);
        profilePicture.setFitHeight(40);
        profilePicture.setStyle("-fx-border-radius: 20;");

        // Username and date/time
        VBox userDetailsBox = new VBox(2);
        Label userLabel = new Label(UserSession.getInstance().getUsername());
        userLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label dateLabel = new Label(post.getPostDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd • HH:mm")));
        dateLabel.setStyle("-fx-text-fill: #606770; -fx-font-size: 12;");

        userDetailsBox.getChildren().addAll(userLabel, dateLabel);

        // Add profile picture and user details to the user info box
        userInfoBox.getChildren().addAll(profilePicture, userDetailsBox);

        // Post content
        Label contentLabel = new Label(post.getContent());
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 14;");

        // Post image (if available)
        VBox imageBox = new VBox();
        if (post.getImagePath() != null && !post.getImagePath().isEmpty()) {
            ImageView imageView = new ImageView(new Image(new File(post.getImagePath()).toURI().toString()));
            imageView.setFitWidth(500);
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-border-radius: 5; -fx-border-color: #dddfe2; -fx-border-width: 1;");
            imageBox.getChildren().add(imageView);
        }

        // Like and Comment buttons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);

        // Like button
        Button likeButton = new Button("Like");
        likeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #385898; -fx-font-weight: bold;");
        Label likeCountLabel = new Label("0"); // Placeholder for like count
        likeCountLabel.setStyle("-fx-text-fill: #606770; -fx-font-size: 12;");

        // Track whether the current user has liked the post
        boolean[] isLiked = {false}; // Using an array to make it effectively final for the lambda

        likeButton.setOnAction(e -> {
            if (isLiked[0]) {
                // If already liked, unlike the post
                likeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #385898; -fx-font-weight: bold;");
                int currentLikes = Integer.parseInt(likeCountLabel.getText());
                likeCountLabel.setText(String.valueOf(currentLikes - 1));
            } else {
                // If not liked, like the post
                likeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #1877f2; -fx-font-weight: bold;");
                int currentLikes = Integer.parseInt(likeCountLabel.getText());
                likeCountLabel.setText(String.valueOf(currentLikes + 1));
            }
            isLiked[0] = !isLiked[0]; // Toggle the like state
        });

        // Comment button
        Button commentButton = new Button("Comment");
        commentButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #385898; -fx-font-weight: bold;");

        buttonsBox.getChildren().addAll(likeButton, likeCountLabel, commentButton);

        // Add components to the post card
        postCard.getChildren().addAll(userInfoBox, contentLabel, imageBox, buttonsBox);

        // Scrollable comments section
        VBox commentsContainer = new VBox(10);
        commentsContainer.setPadding(new Insets(10));
        commentsContainer.setStyle("-fx-background-color: #f0f2f5;");

        // Placeholder for comments (replace with actual comments from the database later)
        for (int i = 1; i <= 10; i++) {
            HBox commentBox = new HBox(10);
            commentBox.setAlignment(Pos.CENTER_LEFT);

            // Profile picture placeholder for the commenter
            ImageView commenterProfilePicture = new ImageView(new Image("https://via.placeholder.com/30"));
            commenterProfilePicture.setFitWidth(30);
            commenterProfilePicture.setFitHeight(30);
            commenterProfilePicture.setStyle("-fx-border-radius: 15;");

            // Comment content
            Label commentLabel = new Label("Comment " + i);
            commentLabel.setStyle("-fx-font-size: 14; -fx-background-color: white; -fx-padding: 10; -fx-border-color: #dddfe2; -fx-border-width: 1;");

            // Spacer to push the MenuButton to the right
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Three-dots menu for edit and delete options (only for my comments)
            if (i % 2 == 0) { // Simulate that every second comment is mine (replace with actual logic)
                MenuButton commentOptionsMenu = new MenuButton();
                commentOptionsMenu.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
                ImageView commentDotsIcon = new ImageView(new Image("https://via.placeholder.com/15"));
                commentDotsIcon.setFitWidth(15);
                commentDotsIcon.setFitHeight(15);
                commentOptionsMenu.setGraphic(commentDotsIcon);

                // Edit option
                MenuItem editCommentItem = new MenuItem("Edit");
                editCommentItem.setGraphic(new ImageView(new Image("https://via.placeholder.com/10")));
                editCommentItem.setOnAction(e -> editComment(commentLabel));

                // Delete option
                MenuItem deleteCommentItem = new MenuItem("Delete");
                deleteCommentItem.setGraphic(new ImageView(new Image("https://via.placeholder.com/10")));
                deleteCommentItem.setOnAction(e -> confirmDeleteComment(commentsContainer, commentBox, commentCountLabel));

                commentOptionsMenu.getItems().addAll(editCommentItem, deleteCommentItem);

                // Add the menu to the comment box
                commentBox.getChildren().add(commentOptionsMenu);
            }

            // Add components to the comment box
            commentBox.getChildren().addAll(commenterProfilePicture, commentLabel, spacer);

            // Add the comment box to the container
            commentsContainer.getChildren().add(commentBox);
        }

        ScrollPane commentsScrollPane = new ScrollPane(commentsContainer);
        commentsScrollPane.setFitToWidth(true);
        commentsScrollPane.setStyle("-fx-background-color: transparent;");

        // Text box for writing a new comment
        HBox newCommentBox = new HBox(10);
        newCommentBox.setPadding(new Insets(10));
        newCommentBox.setStyle("-fx-background-color: white; -fx-border-color: #dddfe2; -fx-border-width: 1;");

        TextField commentField = new TextField();
        commentField.setPromptText("Write a comment...");
        commentField.setStyle("-fx-font-size: 14;");

        Button postCommentButton = new Button("Post");
        postCommentButton.setStyle("-fx-background-color: #1877f2; -fx-text-fill: white; -fx-font-weight: bold;");
        postCommentButton.setOnAction(e -> {
            String commentText = commentField.getText();
            if (!commentText.isEmpty()) {
                // Add the new comment to the comments container
                HBox commentBox = new HBox(10);
                commentBox.setAlignment(Pos.CENTER_LEFT);

                // Profile picture placeholder for the commenter
                ImageView commenterProfilePicture = new ImageView(new Image("https://via.placeholder.com/30"));
                commenterProfilePicture.setFitWidth(30);
                commenterProfilePicture.setFitHeight(30);
                commenterProfilePicture.setStyle("-fx-border-radius: 15;");

                // Comment content
                Label commentLabel = new Label(commentText);
                commentLabel.setStyle("-fx-font-size: 14; -fx-background-color: white; -fx-padding: 10; -fx-border-color: #dddfe2; -fx-border-width: 1;");

                // Spacer to push the MenuButton to the right
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // Three-dots menu for edit and delete options (only for my comments)
                MenuButton commentOptionsMenu = new MenuButton();
                commentOptionsMenu.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
                ImageView commentDotsIcon = new ImageView(new Image("https://via.placeholder.com/15"));
                commentDotsIcon.setFitWidth(15);
                commentDotsIcon.setFitHeight(15);
                commentOptionsMenu.setGraphic(commentDotsIcon);

                // Edit option
                MenuItem editCommentItem = new MenuItem("Edit");
                editCommentItem.setGraphic(new ImageView(new Image("https://via.placeholder.com/10")));
                editCommentItem.setOnAction(event -> editComment(commentLabel));

                // Delete option
                MenuItem deleteCommentItem = new MenuItem("Delete");
                deleteCommentItem.setGraphic(new ImageView(new Image("https://via.placeholder.com/10")));
                deleteCommentItem.setOnAction(event -> confirmDeleteComment(commentsContainer, commentBox, commentCountLabel));

                commentOptionsMenu.getItems().addAll(editCommentItem, deleteCommentItem);

                // Add components to the comment box
                commentBox.getChildren().addAll(commenterProfilePicture, commentLabel, spacer, commentOptionsMenu);

                // Add the comment box to the container
                commentsContainer.getChildren().add(commentBox);

                // Update the comment count
                int currentComments = Integer.parseInt(commentCountLabel.getText());
                commentCountLabel.setText(String.valueOf(currentComments + 1));

                // Clear the comment field
                commentField.clear();
            }
        });

        newCommentBox.getChildren().addAll(commentField, postCommentButton);

        // Add components to the main layout
        mainLayout.setTop(postCard);
        mainLayout.setCenter(commentsScrollPane);
        mainLayout.setBottom(newCommentBox);

        // Set the scene
        Scene scene = new Scene(mainLayout, 600, 500);
        commentStage.setScene(scene);
        commentStage.show();
    }

    // Method to edit a comment
    private void editComment(Label commentLabel) {
        TextInputDialog dialog = new TextInputDialog(commentLabel.getText());
        dialog.setTitle("Edit Comment");
        dialog.setHeaderText("Edit your comment:");
        dialog.setContentText("Comment:");

        dialog.showAndWait().ifPresent(newCommentText -> {
            if (!newCommentText.isEmpty()) {
                commentLabel.setText(newCommentText);
            }
        });
    }

    // Method to confirm comment deletion
    private void confirmDeleteComment(VBox commentsContainer, HBox commentBox, Label commentCountLabel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Comment");
        alert.setHeaderText("Are you sure you want to delete this comment?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                commentsContainer.getChildren().remove(commentBox); // Remove the comment
                int currentComments = Integer.parseInt(commentCountLabel.getText());
                commentCountLabel.setText(String.valueOf(currentComments - 1)); // Update the comment count
            }
        });
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