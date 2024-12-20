
package com.nirvan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFxApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        primaryStage.setScene(new Scene(loader.load(),800,500));
        primaryStage.setTitle("Standalone App");
        primaryStage.show();
    }
}
