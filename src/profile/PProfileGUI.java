package profile;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class PProfileGUI extends Application {

    // Components
    private TextArea bioField;
    private TextField profilePictureField;
    private ComboBox<String> privacyLevelComboBox;
    private Button uploadButton, saveButton;
    private VBox profileContainer; // To display profile details
    private Stage primaryStage;

    public PProfileGUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the stage
        primaryStage.setTitle("Profile Management");

        // Main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setStyle("-fx-background-color: #f0f2f5;"); // Light background color like Facebook

        // Profile container
        profileContainer = new VBox(10);
        profileContainer.setPadding(new Insets(15));
        profileContainer.setStyle("-fx-background-color: white; -fx-border-color: #dddfe2; -fx-border-width: 1;");

        // Add bio field with label
        Label bioLabel = new Label("Bio:");
        bioLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        bioField = new TextArea();
        bioField.setWrapText(true);
        bioField.setPrefRowCount(5);
        bioField.setStyle("-fx-font-size: 14;");

        // Add profile picture field with label
        Label pictureLabel = new Label("Profile Picture:");
        pictureLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        profilePictureField = new TextField();
        profilePictureField.setEditable(false);
        uploadButton = new Button("Upload Picture");
        uploadButton.setStyle("-fx-background-color: #1877f2; -fx-text-fill: white; -fx-font-weight: bold;");
        uploadButton.setOnAction(e -> handleUploadPicture());
        HBox pictureBox = new HBox(10, profilePictureField, uploadButton);

        // Add privacy level field with label
        Label privacyLabel = new Label("Privacy Level:");
        privacyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        privacyLevelComboBox = new ComboBox<>();
        privacyLevelComboBox.getItems().addAll("Public", "Private", "Friends Only");
        privacyLevelComboBox.setValue("Public");
        privacyLevelComboBox.setStyle("-fx-font-size: 14;");

        // Add Save button
        saveButton = new Button("Save Profile");
        saveButton.setStyle("-fx-background-color: #1877f2; -fx-text-fill: white; -fx-font-weight: bold;");
        saveButton.setOnAction(e -> handleSaveProfile());

        // Add components to the profile container
        profileContainer.getChildren().addAll(bioLabel, bioField, pictureLabel, pictureBox, privacyLabel, privacyLevelComboBox, saveButton);

        // Add the profile container to the main layout
        mainLayout.getChildren().add(profileContainer);

        // Create the scene and set it on the stage
        Scene scene = new Scene(mainLayout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handle profile picture upload
    private void handleUploadPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            profilePictureField.setText(selectedFile.getAbsolutePath());
        }
    }

    // Handle saving the profile
    private void handleSaveProfile() {
        String bio = bioField.getText();
        String profilePicture = profilePictureField.getText();
        String privacyLevel = privacyLevelComboBox.getValue();

        // Validate input
        if (bio.isEmpty() || profilePicture.isEmpty()) {
            showAlert("Error", "Bio and Profile Picture are required!");
            return;
        }
        ProfileDAO profileDao= new ProfileDAO();
       // Profile pro = new Profile(bio, profilePicture, privacyLevel );
        boolean isCreated =  profileDao.saveProfile(bio, profilePicture, privacyLevel );
        if (isCreated) {
        	System.out.println("profile is created ");
         //   addProfileStage.close(); // Close the "Add Post" window
         //   loadPosts(); // Refresh the posts list
        } else {
            showAlert("Error", "Failed to create post.");
        }
        // Save the profile to the database (you can add this logic if needed)
        // For now, we focus on posts using PostDAO
    }

    // Show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}