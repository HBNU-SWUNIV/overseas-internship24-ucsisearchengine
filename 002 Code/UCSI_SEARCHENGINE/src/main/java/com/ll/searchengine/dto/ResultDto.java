package com.ll.searchengine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class ResultDto {
    List<FileDto> fileDtos;
    Integer resultCount;
}
