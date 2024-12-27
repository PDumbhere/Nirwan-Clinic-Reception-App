package com.nirvan.controller;

import com.nirvan.JavaFxApplication;
import com.nirvan.config.SpringFXMLLoader;
import com.nirvan.constants.Sex;
import com.nirvan.entity.Patient;
import com.nirvan.service.PatientService;
import com.nirvan.service.impl.ExcelService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PatientTableController {


    @Autowired
    PatientService patientService;

    @FXML
    private Hyperlink returnToMainLink;

    @FXML
    private Hyperlink patientRegistrationLink;

    @FXML
    private TextField searchNameField;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Long> idColumn;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, String> dobColumn;
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
    @FXML
    private TableColumn<Patient, String> registrationDateColumn;

    private ObservableList<Patient> patientData;

    @FXML
    private void initialize() {
        // Bind table columns to Patient properties
        idColumn.setCellValueFactory(cellData ->(new SimpleObjectProperty<>(cellData.getValue().getPatientId()) ));
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        dobColumn.setCellValueFactory(cellData -> {
            LocalDateTime val = cellData.getValue().getDob();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = val.format(formatter);
            return new SimpleObjectProperty<>(formattedDate);
        });
        sexColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSex()));
        typeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPatientType()));
        treatmentColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTreatment()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhone()));
        addressColumn.setCellValueFactory(cellData ->new SimpleObjectProperty<>(cellData.getValue().getAddress()));
        registrationDateColumn.setCellValueFactory(cellData -> {
           LocalDateTime val = cellData.getValue().getRegistrationDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Format the LocalDateTime to a string
            String formattedDate="";
            if(val!=null)
                formattedDate = val.format(formatter);

            return new SimpleObjectProperty<>(formattedDate);
        });

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

        patientTable.setOnMouseClicked(event ->{
            if(event.getClickCount()==2){
                Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
                if(selectedPatient!=null){
                    showAppointmentHistory(selectedPatient);
                }
            }
        });
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
        FXMLLoader fxmlLoader = loader.loadWithController("/patientRegistration.fxml");

        // Get the current stage and set the new scene
        Parent root = fxmlLoader.getRoot();
        Scene scene = new Scene(root, 800, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Register New Patient");
        stage.showAndWait();

        // Refresh the table after update
        refreshTable();
    }

    @FXML
    public void showFilterDialog(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/filterDialog.fxml"));
            Parent root = loader.load();

            // Get the controller and set the patient data for filtering
            FilterDialogController filterDialogController = loader.getController();
            filterDialogController.setPatientData(patientData);

            //Show filter dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Filter Patients");
            dialogStage.setScene(new Scene(root,300,120));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

            // Apply the filter if criteria are selected
            ObservableList<Patient> filteredData = filterDialogController.getFilteredData();
            if(filteredData != null){
                patientData = filteredData;
                patientTable.setItems(patientData);
            }


        }catch(IOException exception){
            exception.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to load filter dialog");
            alert.setContentText(exception.getMessage());
            alert.show();
        }
    }



    @FXML
    private void handleDelete(){
        //Get Selected Patient
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if(selectedPatient == null){
            showAlert("No Selection", "Please select a patient to delete.");
            return;
        }

        //Confirm deletion
        Alert confiramtionAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confiramtionAlert.setTitle("Confirm Deletion");
        confiramtionAlert.setHeaderText("Are you sure you want to delete this patient details?");
        confiramtionAlert.setContentText("Patient: "+ selectedPatient.getName());

        //Wait for user response
        confiramtionAlert.showAndWait().ifPresent(response->{
            if(response.getButtonData().isDefaultButton()){
                // Delete from the database and table
                patientService.deletePatient(selectedPatient.getPatientId());
                patientData.remove(selectedPatient);
                showAlert("Success", "Patient deleted successfully.");
            }
        });
    }

    @FXML
    private void handleUpdate(){
        // Get the selected patient
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showAlert("No Selection", "Please select a patient to update.");
            return;
        }

        try{
            // Load the patient registration/update form
            SpringFXMLLoader loader = JavaFxApplication.springContext.getBean(SpringFXMLLoader.class);
            FXMLLoader fxmlLoader = loader.loadWithController("/patientRegistration.fxml");
//            Scene scene = new Scene((Parent) loader.load("/patientRegistration.fxml"),800,500);

            PatientRegistrationController controller = fxmlLoader.getController();
            controller.setPatientData(selectedPatient);

            //show the updated form in a new stage
            Parent root = fxmlLoader.getRoot();
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Update Patient");
            stage.showAndWait();

            // Refresh the table after update
            refreshTable();
        }catch (Exception e){
            showAlert("Error", "Failed to load the update form: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch(){
        String searchName = searchNameField.getText();
        List<Patient> patientList = patientService.searchByName(searchName);
        if(patientList == null){
            showAlert("Not Found", "No name matches the character string: "+searchName);
        }else{
            patientData = FXCollections.observableArrayList(patientList);
            patientTable.setItems(patientData);
        }
    }

    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility method to refresh table data
    @FXML
    private void refreshTable() {
        patientData = FXCollections.observableArrayList(patientService.getAllPatients());
        patientTable.setItems(patientData);
        searchNameField.setText("");
    }

    @FXML
    public void handleExportToExcel() {
        try {
            // Open file chooser to select save location
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                List<Patient> patients = patientService.getAllPatients();
                ExcelService exporter = new ExcelService();
                exporter.exportPatientsToExcel(patients, file.getAbsolutePath());
                showAlert("Export Successful","Patients data exported successfully");
                System.out.println("Export successful: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleImportFromExcel(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File to import");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showOpenDialog(new Stage());

            if(file!=null){
                List<Patient> patients = new ExcelService().importPatientsFromExcel(file);
                patientService.importPatientsFromExcel(patients);
                showAlert("Import Successful","Patients data imported successfully");
                System.out.println("Import successful: " + file.getAbsolutePath());
                refreshTable();
            }
        }catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Import Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public ObservableList<Patient> searchByName(String name){
        ObservableList<Patient> searchList = patientData
                .filtered(patient -> patient.getName().toLowerCase()
                        .contains(name.toLowerCase()));
        return searchList;
    }

    public void showAppointmentHistory(Patient patient) {

        try{
            SpringFXMLLoader loader = JavaFxApplication.springContext.getBean(SpringFXMLLoader.class);
            FXMLLoader fxmlLoader = loader.loadWithController("/appointmentHistory.fxml");

            AppointmentHistoryController controller = fxmlLoader.getController();
            controller.setPatient(patient);
            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Appointment history");
            Scene scene = new Scene(root, 580, 500);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Opening Appointment History");
            alert.setContentText(e.getMessage());
        }

    }
}

