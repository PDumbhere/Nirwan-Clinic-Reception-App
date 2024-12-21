package com.nirvan.service.impl;

import com.nirvan.entity.Patient;
import com.nirvan.repository.PatientRepository;
import com.nirvan.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient savePatient(Patient patient) {
        if(!verifyPatientEntry(patient)){
            throw new RuntimeException("Please fill all the mandatory fields");
        }
        return patientRepository.save(patient);
    }

    private boolean verifyPatientEntry(Patient patient){
        if(patient==null || patient.getName().isBlank() || patient.getSex()==null || patient.getDob()== null
                || patient.getPhone()==0)
            return false;
        else
            return true;
    }
}
