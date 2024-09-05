package com.ll.searchengine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UcsiUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    Long id;
    @Column(unique = true)
    private String username;
    private String password;
}
