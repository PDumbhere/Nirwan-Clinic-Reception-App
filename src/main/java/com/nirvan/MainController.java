
package com.nirvan;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MainController {
    @FXML
    private Label greetingLabel;

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
}
