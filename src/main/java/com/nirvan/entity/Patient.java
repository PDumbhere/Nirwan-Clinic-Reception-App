package com.nirvan.entity;

import com.nirvan.constants.Sex;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_seq")
    @SequenceGenerator(name = "patient_seq", sequenceName = "patient_seq", initialValue = 1001, allocationSize = 1)
    private Long patientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime dob;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    private String patientType;
    private String treatment;

    @Column(nullable = false, length = 10)
    private Long phone;
    private String address;

    private LocalDateTime registrationDateTime;

    private LocalDateTime updatedDateTime;

//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Appointment> appointments;

    @PrePersist
    private void onSave(){
        registrationDateTime = LocalDateTime.now();
        updatedDateTime = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate(){
        updatedDateTime = LocalDateTime.now();
    }

}
