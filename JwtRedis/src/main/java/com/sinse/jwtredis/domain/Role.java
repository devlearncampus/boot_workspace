package com.sinse.jwtredis.domain;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="role")
@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int role_id;
    private String role_name;
}
