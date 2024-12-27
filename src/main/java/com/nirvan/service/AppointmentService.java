package com.nirvan.service;

import com.nirvan.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment saveAppointment(Appointment appointment);
    List<Appointment> getAppointmentByPatient(Long patientId);
}
