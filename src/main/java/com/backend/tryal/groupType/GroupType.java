package com.backend.tryal.groupType;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "group_types")
public class GroupType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_type_id")
    private Long groupTypeId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(updatable = false, name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public GroupType() {}

    public GroupType(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
