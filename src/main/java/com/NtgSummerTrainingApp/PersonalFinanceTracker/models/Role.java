package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public void setName(RoleEnum roleEnum){
        this.name = roleEnum.name();
    }

}
