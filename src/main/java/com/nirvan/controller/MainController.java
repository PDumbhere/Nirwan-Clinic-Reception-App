
package com.nirvan.controller;

import com.nirvan.JavaFxApplication;
import com.nirvan.config.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
public class MainController {


    @FXML
    private Label greetingLabel;

    @FXML
    private Hyperlink patientDataLink;

    @FXML
    private Hyperlink patientRegistrationLink;

    public void loadGreeting() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/greet"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            greetingLabel.setText(response.body());
            greetingLabel.setTextFill(Paint.valueOf("green"));
            greetingLabel.setWrapText(true);
        } catch (Exception e) {
            greetingLabel.setText("Error fetching greeting!");
            e.printStackTrace();
        }
    }

    public void switchToPatientData() throws Exception{
        // load the patient data FXML file
        SpringFXMLLoader loader = JavaFxApplication.springContext.getBean(SpringFXMLLoader.class);
        Scene scene = new Scene((Parent) loader.load("/patientList.fxml"),800,500);

        // Get the current stage and set the new scene
        Stage stage = (Stage) patientDataLink.getScene().getWindow();
        stage.setScene(scene);
    }

    public void switchToPatientRegistration() throws Exception{
        SpringFXMLLoader loader = JavaFxApplication.springContext.getBean(SpringFXMLLoader.class);
        Scene scene = new Scene((Parent) loader.load("/patientRegistration.fxml"),800,500);

        // Get the current stage and set the new scene
        Stage stage = (Stage) patientRegistrationLink.getScene().getWindow();
        stage.setScene(scene);
    }
}