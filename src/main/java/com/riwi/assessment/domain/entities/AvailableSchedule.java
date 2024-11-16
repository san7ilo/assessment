package com.riwi.assessment.domain.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "available_schedule",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"doctor_id", "date", "start_time", "end_time"},
                        name = "unique_schedule_per_doctor"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Users doctor;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime startTime;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean isAvailable = true;

    public AvailableSchedule(LocalDate date, LocalTime startTime, LocalTime endTime, boolean isAvailable, Users doctor) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.doctor = doctor;
    }
}


