package com.backend.tryal.timeslot;

import com.backend.tryal.experience.Experience;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Time;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "timeslots")
public class Timeslot {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "timeslot_id")
    private UUID timeslotId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id")
    private Experience experience;

    @Column(name = "timeslot_date", nullable = true)
    private Date timeslotDate;

    @Column(name = "start_time", nullable = true)
    private Time startTime;

    @Column(name = "is_cancelled", nullable = true)
    private Boolean isCancelled;

    @Column(name = "exp_convert_price", nullable = true)
    private Double expConvertPrice;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Timeslot() {
    }

    public Timeslot(UUID timeslotId, Experience experience, Date timeslotDate, Time startTime, Boolean isCancelled, Double expConvertPrice) {
        this.timeslotId = timeslotId;
        this.experience = experience;
        this.timeslotDate = timeslotDate;
        this.startTime = startTime;
        this.isCancelled = isCancelled;
        this.expConvertPrice = expConvertPrice;
    }
}
