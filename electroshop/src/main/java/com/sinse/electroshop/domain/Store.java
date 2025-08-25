package com.sinse.electroshop.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="store")
public class Store {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) //Mysql 의 auto_increment 매핑
    private int store_id;

    private String business_id;
    private String password;
    private String store_name;
}
