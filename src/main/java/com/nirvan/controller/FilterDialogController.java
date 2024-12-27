package com.nirvan.controller;

import com.nirvan.entity.Patient;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class FilterDialogController {

    @FXML
    private ComboBox<String> patientTypeComboBox;

    @FXML
    private ComboBox<String> treatmentComboBox;

    @FXML
    private Button applyButton;

    private ObservableList<Patient> originalData;
    private ObservableList<Patient> filteredData;

    public void setPatientData(ObservableList<Patient> patientData){
        this.originalData = patientData;
    }

    @FXML
    public void applyFilter() {
        String patientType = patientTypeComboBox.getValue();
        String treatment = treatmentComboBox.getValue();

        filteredData = originalData.filtered(patient -> {
            if((patientType!=null && !patientType.equalsIgnoreCase(patient.getPatientType())) ||
                    (treatment !=null && !treatment.equalsIgnoreCase(patient.getTreatment()))){
                return false;
            }else{
                return true;
            }
        });

        Stage stage = (Stage) applyButton.getScene().getWindow();
        stage.close();
    }

    public ObservableList<Patient> getFilteredData(){
        return filteredData;
    }
}
