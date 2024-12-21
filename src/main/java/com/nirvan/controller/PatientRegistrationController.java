package com.nirvan.controller;

import com.nirvan.JavaFxApplication;
import com.nirvan.config.SpringFXMLLoader;
import com.nirvan.constants.Sex;
import com.nirvan.entity.Patient;
import com.nirvan.repository.PatientRepository;
import com.nirvan.service.PatientService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class PatientRegistrationController {

    @Autowired
    private PatientService patientService;

    @FXML
    private Hyperlink returnToMainLink;

    @FXML
    private Hyperlink returnToPatientData;

    @FXML
    private TextField patientNameField;

    @FXML
    private DatePicker dobField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private ComboBox<String> patientTypeComboBox;

    @FXML
    private TextField treatmentField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField addressField;

    @FXML
    public void handleSave() {
        try {
            String name = patientNameField.getText();
            LocalDate dob = dobField.getValue();
            Sex gender = genderComboBox.getValue().equalsIgnoreCase("other")?Sex.OTHER:
                    genderComboBox.getValue().equalsIgnoreCase("male")?Sex.MALE:Sex.FEMALE;
            String patientType = patientTypeComboBox.getValue();
            String treatment = treatmentField.getText();
            String contact = contactField.getText();
            String address = addressField.getText();

            // Validation (Basic)
            if (name == null || dob == null || gender == null || patientType == null || contact == null || contact.length() != 10) {
                showAlert("Validation Error", "Please fill in all fields with valid data.");
                return;
            }

            // Create and save patient entity
            Patient patient = new Patient();
            patient.setName(name);
            patient.setDob(dob.atStartOfDay()); // Convert LocalDate to LocalDateTime
            patient.setSex(gender);
            patient.setPatientType(patientType);
            patient.setTreatment(treatment);
            patient.setPhone(Long.valueOf(contact));
            patient.setAddress(address);

            patientService.savePatient(patient);
            showAlert("Success", "Patient registered successfully!");

            // Clear fields after saving
            clearForm();
        } catch (Exception e) {
            showAlert("Error", "An error occurred while saving patient data: " + e.getMessage());
        }
    }

    private void clearForm() {
        patientNameField.clear();
        dobField.setValue(null);
        genderComboBox.setValue(null);
        patientTypeComboBox.setValue(null);
        treatmentField.clear();
        contactField.clear();
        addressField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void switchToMain() throws Exception{

        System.out.println("Hyperlink clicked!");
        // load the main FXML file
        SpringFXMLLoader loader = JavaFxApplication.springContext.getBean(SpringFXMLLoader.class);
        Scene scene = new Scene((Parent) loader.load("/main.fxml"),800,500);

        // Get the current stage and set the new scene
        Stage stage = (Stage) returnToMainLink.getScene().getWindow();
        stage.setScene(scene);
    }
    @FXML
    public void switchPatientData() throws Exception{

        System.out.println("Hyperlink clicked!");
        // load the main FXML file
        SpringFXMLLoader loader = JavaFxApplication.springContext.getBean(SpringFXMLLoader.class);
        Scene scene = new Scene((Parent) loader.load("/patientList.fxml"),800,500);

        // Get the current stage and set the new scene
        Stage stage = (Stage) returnToPatientData.getScene().getWindow();
        stage.setScene(scene);
    }
}

