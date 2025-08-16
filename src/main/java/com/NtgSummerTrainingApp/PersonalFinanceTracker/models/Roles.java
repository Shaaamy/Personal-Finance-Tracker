package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private RoleEnum role;
    // it has a 1-to-many relationship with User_roles table
    @OneToMany(mappedBy = "role")
    @JsonManagedReference
    private Set<UserRoles> userRoles = new HashSet<>();

}
