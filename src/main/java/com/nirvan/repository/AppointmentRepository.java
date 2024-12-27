package com.nirvan.repository;

import com.nirvan.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.patient.patientId= :patientId")
    List<Appointment> findByPatientId(@Param("patientId") Long patientId);
}
