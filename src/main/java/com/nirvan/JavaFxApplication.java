
package com.nirvan;

import com.nirvan.config.SpringFXMLLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

public class JavaFxApplication extends Application {

    public static ApplicationContext springContext;

    public static void setSpringContext(ApplicationContext context) {
        springContext = context;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SpringFXMLLoader loader = springContext.getBean(SpringFXMLLoader.class);
        Parent root = (Parent) loader.load("/main.fxml");
        Scene scene = new Scene(root, 900, 600);
//        GaussianBlur blur = new GaussianBlur(10); // Adjust the radius to your liking
//        root.setEffect(blur);
        // Set the scene and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Nirvan Dental Clinic");
        primaryStage.show();
    }
}
