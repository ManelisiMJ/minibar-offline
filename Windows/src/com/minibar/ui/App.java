package com.minibar.ui;

import com.minibar.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class to launch the application
 */
public class App extends Application {
    Scene scene;

    public App() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        MainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.show();
    }
}

