
package com.nirvan;

import com.nirvan.config.SpringFXMLLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        Scene scene = new Scene((Parent) loader.load("/main.fxml"),800,500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Standalone App");
        primaryStage.show();
    }
}
