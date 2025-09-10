package com.sinse.jwtredis.domain;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="member")
@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int member_id;

    private String id;
    private String password;
    private String name;
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id")
    private Role role;
}



