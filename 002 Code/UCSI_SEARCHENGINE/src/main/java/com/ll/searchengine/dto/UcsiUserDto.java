package com.ll.searchengine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UcsiUserDto {
    private Long id;
    private String username;
    private String password;
}
