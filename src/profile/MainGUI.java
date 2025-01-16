package profile;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import post.PostCreationGUI;

public class MainGUI  {
	private Stage primaryStage;

    public MainGUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initialize();
    }

    public void initialize() {
    	primaryStage.setTitle("choose your option");
        // Create buttons for navigation
        Button postsButton = new Button("Go to Posts Page");
        Button profileButton = new Button("Go to Profile Page");

        // Set actions for the buttons
        postsButton.setOnAction(e -> {
            // Open the Posts Page
            Stage postStage = new Stage();
            PostCreationGUI postCreationGUI = new PostCreationGUI(postStage);
            // postCreationGUI.display(postStage); // Uncomment if you have a display method in PostCreationGUI
        });

        profileButton.setOnAction(e -> {
            // Open the Profile Page
            Stage profileStage = new Stage();
            PProfileGUI profilePage = new PProfileGUI(profileStage);
            profilePage.start(profileStage);
        });

        // Create a layout and add buttons
        VBox layout = new VBox(10, postsButton, profileButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}