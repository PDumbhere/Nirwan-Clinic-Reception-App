package com.nirvan.repository;

import com.nirvan.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Patient> searchName(@Param("name") String name);
}
