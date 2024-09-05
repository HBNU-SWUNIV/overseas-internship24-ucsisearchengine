package com.ll.searchengine.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Data {
    @GeneratedValue @Id
    Long id;
}
