package com.nirvan.config;

import javafx.fxml.FXMLLoader;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class SpringFXMLLoader {

    private final ApplicationContext context;

    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    public Object load(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        InputStream fxmlStream = getClass().getResourceAsStream(fxmlPath);
        return loader.load(fxmlStream);
    }

    // Returns the FXMLLoader instance after loading
    public FXMLLoader loadWithController(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        InputStream fxmlStream = getClass().getResourceAsStream(fxmlPath);
        loader.load(fxmlStream); // Load the FXML
        return loader;
    }
}
