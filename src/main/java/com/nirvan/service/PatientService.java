package com.nirvan.service;

import com.nirvan.entity.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> getAllPatients();

    Patient savePatient(Patient patient);
}
