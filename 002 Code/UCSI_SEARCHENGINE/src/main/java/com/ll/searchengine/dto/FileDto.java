package com.ll.searchengine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class FileDto {
    private String fileName;
    private String absolutePath;
    private String date;
    private String extendName;
    private String size;
}
