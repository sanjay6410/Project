package com.valtech.team3.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")  //Name of The Table in the DataBase..
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "role_id")
    private int id;  //Declare Id for the Roles Assigned ..
    @Column(name = "role")
    private String role;  //Name of the Roles i.e, USER/ADMIN
}

