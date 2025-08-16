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
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false,unique = true)
    private String name;
    // it has a 1-to-many relationship with User_roles table
    @OneToMany(mappedBy = "role")
    @JsonManagedReference
    private Set<UserRoles> userRoles = new HashSet<>();

    public void setName(RoleEnum roleEnum){
        this.name = roleEnum.name();
    }

}
