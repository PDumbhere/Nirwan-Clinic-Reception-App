package com.nirvan.controller;

import com.nirvan.JavaFxApplication;
import com.nirvan.config.SpringFXMLLoader;
import com.nirvan.constants.Sex;
import com.nirvan.entity.Patient;
import com.nirvan.service.PatientService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class PatientTableController {


    @Autowired
    PatientService patientService;

    @FXML
    private Hyperlink returnToMainLink;

    @FXML
    private Hyperlink patientRegistrationLink;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Long> idColumn;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, LocalDateTime> dobColumn;
    @FXML
    private TableColumn<Patient, Sex> sexColumn;
    @FXML
    private TableColumn<Patient, String> typeColumn;
    @FXML
    private TableColumn<Patient, String> treatmentColumn;
    @FXML
    private TableColumn<Patient, Long> phoneColumn;
    @FXML
    private TableColumn<Patient, String> addressColumn;

    private ObservableList<Patient> patientData;

    @FXML
    private void initialize() {
        // Bind table columns to Patient properties
        idColumn.setCellValueFactory(cellData ->(new SimpleObjectProperty<>(cellData.getValue().getPatientId()) ));
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        dobColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDob()));
        sexColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSex()));
        typeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPatientType()));
        treatmentColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTreatment()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhone()));
        addressColumn.setCellValueFactory(cellData ->new SimpleObjectProperty<>(cellData.getValue().getAddress()));

        // Sample data
//        patientData = FXCollections.observableArrayList(
//                new Patient(1L, "Yash Kamble", LocalDateTime.of(2014, 1, 9, 0, 0), Sex.MALE, "New", "Consultation", 9543585607L, "Nagpur"),
//                new Patient(2L, "Usha Rachlwar", LocalDateTime.of(1994, 2, 16, 0, 0), Sex.FEMALE, "Old", "Follow-up", 9673636370L, "Nagpur")
//        );
        ObservableList<Patient> patients = FXCollections.observableArrayList(patientService.getAllPatients());
        if(!patients.isEmpty()){
            patientData = patients;
        }

        // Add data to table
        patientTable.setItems(patientData);
    }

    public void switchToMain() throws Exception{
        // load the main FXML file
        SpringFXMLLoader loader = JavaFxApplication.springContext.getBean(SpringFXMLLoader.class);
        Scene scene = new Scene((Parent) loader.load("/main.fxml"),800,500);

        // Get the current stage and set the new scene
        Stage stage = (Stage) returnToMainLink.getScene().getWindow();
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

