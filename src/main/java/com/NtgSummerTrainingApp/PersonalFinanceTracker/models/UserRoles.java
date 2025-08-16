package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to User table
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false) // Foreign key to Roles table
    @JsonBackReference
    private Roles role;
}
