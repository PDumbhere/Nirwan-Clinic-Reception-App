package com.nirvan.controller;

import com.nirvan.entity.Appointment;
import com.nirvan.entity.Patient;
import com.nirvan.service.AppointmentService;
import com.nirvan.service.PatientService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppointmentHistoryController {

    @FXML
    public Label patientId;
    @FXML
    public Label name;
    @FXML
    public Label dob;
    @FXML
    public Label sex;
    @FXML
    public Label patientType;
    @FXML
    public Label phone;
    @FXML
    public Label address;
    @FXML
    public Label registrationDate;
    @FXML
    public ComboBox<String> treatment;
    @FXML
    public DatePicker appointmentDate;
    @FXML
    public ComboBox<LocalTime> appointmentTime;
    @FXML
    public TableView<Appointment> appointmentTable;
    @FXML
    public TableColumn<Appointment, LocalDate> dateColumn;
    @FXML
    public TableColumn<Appointment, LocalTime> timeColumn;
    @FXML
    public TableColumn<Appointment, String> treatmentColumn;
    @FXML
    public TitledPane titledPane;

    private ObservableList<Appointment> appointmentData;

    private Patient patient;

    @Autowired
    private AppointmentService appointmentService;

    public void initialize(){

        dateColumn.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getDate()));
        timeColumn.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getTime()));
        treatmentColumn.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getPurpose()));

//        appointmentData = FXCollections.observableArrayList(patient.getAppointments());

        appointmentTable.setItems(appointmentData);

        setTimes();
    }

    private void setTimes(){
        List<LocalTime> times = generateTimeValues(LocalTime.of(9,0),
                LocalTime.of(21,0),
                30);
        appointmentTime.setItems(FXCollections.observableArrayList(times));

        appointmentTime.setConverter(new StringConverter<LocalTime>() {
            @Override
            public String toString(LocalTime localTime) {
                return localTime!=null? localTime.format(DateTimeFormatter.ofPattern("hh:mm a")):"";
            }

            @Override
            public LocalTime fromString(String s) {
                return LocalTime.parse(s, DateTimeFormatter.ofPattern("hh:mm a") );
            }
        });
        if (!times.isEmpty()) {
            appointmentTime.setValue(times.get(0));
        }
    }

    private List<LocalTime> generateTimeValues(LocalTime start, LocalTime end, int interval){
        List<LocalTime> timeList = new ArrayList<>();
        LocalTime current = start;
        while(!current.isAfter(end)){
            timeList.add(current);
            current = current.plusMinutes(interval);
        }
        return timeList;
    }

    public void setPatient(Patient patient){
        appointmentData = FXCollections.observableArrayList(appointmentService.getAppointmentByPatient(patient.getPatientId()));
        appointmentTable.setItems(appointmentData);
        this.patient = patient;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
        patientId.setText(String.valueOf(patient.getPatientId()));
        name.setText(patient.getName());
        dob.setText(patient.getDob().format(formatter));
        sex.setText(patient.getSex().name());
        patientType.setText(patient.getPatientType());
        phone.setText(String.valueOf(patient.getPhone()));
        address.setText(patient.getAddress());
        registrationDate.setText(patient.getRegistrationDateTime().format(formatter));
    }

    @FXML
    public void handleSaveAppointment(){
        Appointment appointment = new Appointment();
        appointment.setPurpose(treatment.getValue());
        appointment.setDate(appointmentDate.getValue());
        appointment.setTime(appointmentTime.getValue());
        appointment.setPatient(patient);

        Appointment savedAppointment = appointmentService.saveAppointment(appointment);
        appointmentData.add(savedAppointment);
        titledPane.setExpanded(false);
        clear();
    }

    private void refresh(){
        clear();
        appointmentData = FXCollections.observableArrayList(appointmentService.getAppointmentByPatient(Long.parseLong(patientId.getText())));
    }
    private void clear(){
        treatment.setPromptText("Select a Treatment");
        appointmentDate.setPromptText("Select Appointment Date");
        appointmentTime.setPromptText("Select Appointment Time");
    }
}
