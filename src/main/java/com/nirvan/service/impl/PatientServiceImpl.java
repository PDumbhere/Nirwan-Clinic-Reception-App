package com.nirvan.service.impl;

import com.nirvan.entity.Patient;
import com.nirvan.repository.PatientRepository;
import com.nirvan.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public boolean deletePatient(Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if(!patient.isPresent())
            return false;

        patientRepository.deleteById(patientId);
        return true;
    }

    @Override
    public List<Patient> searchByName(String name) {
        return patientRepository.searchName(name);
    }

    @Override
    public boolean importPatientsFromExcel(List<Patient> patients) {
        List<Patient> saveList = new ArrayList<>();
        patients.forEach(patient -> {
            if(patient.getPatientId()==null){
                saveList.add(patient);
                return;
            }
            Optional<Patient> currentPatientRecord = patientRepository.findByName(patient.getName());
            if(currentPatientRecord.isPresent()){
                if(patient.getUpdatedDateTime().isAfter(currentPatientRecord.get().getUpdatedDateTime()))
                    saveList.add(patient);
            }else {
                saveList.add(patient);
            }
        });
        patientRepository.saveAll(saveList);
        return true;
    }

    private boolean verifyPatientEntry(Patient patient){
        if(patient==null || patient.getName().isBlank() || patient.getSex()==null || patient.getDob()== null
                || patient.getPhone()==0)
            return false;
        else
            return true;
    }
}
