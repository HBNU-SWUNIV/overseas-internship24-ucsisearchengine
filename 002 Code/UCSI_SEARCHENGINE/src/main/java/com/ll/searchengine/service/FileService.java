package com.ll.searchengine.service;

import com.ll.searchengine.dto.FileDto;
import com.ll.searchengine.dto.ResultDto;
import com.ll.searchengine.util.FileSettingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileService {

    public void searchFiles(File directory, String keyWord, List<FileDto> fileDtos, String type) {
        File[] files = directory.listFiles();

        System.out.println(directory.getName());

        if (files != null) {
            for (File file : files) {
                if (file.isHidden())
                    continue;

                if (file.isFile() && file.getName().contains(keyWord)) {
                    String fileName = file.getName();
                    String extend = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
                    String[] strings = fileName.split("\\.");
                    LocalDateTime lastModifiedDateTime = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()
                    );
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd\nHH:mm");
                    String formattedDateTime = lastModifiedDateTime.format(formatter);
                    long fileSize = file.length();
                    String size = formatFileSize(fileSize);
                    String absolutePath = file.getAbsolutePath();

                    if (Objects.equals(type, "all"))
                        fileDtos.add(convertToFileDto(strings[0], absolutePath, formattedDateTime, strings[1], size));
                    else {
                        if (extend.equals(type))
                            fileDtos.add(convertToFileDto(strings[0], absolutePath, formattedDateTime, strings[1], size));
                    }
                }
                if (file.isDirectory()) {
                    searchFiles(file, keyWord, fileDtos, type);
                }
            }
        }
    }

    public ResultDto filterFile(List<FileDto> fileDtos, String type) {
        List<FileDto> newFileDtos = new ArrayList<>();
        Integer resultCount = 0;

        for (FileDto fileDto : fileDtos) {
            if (Objects.equals(fileDto.getExtendName(), type)) {
                newFileDtos.add(fileDto);
                resultCount++;
            }
        }

        return convertToResultDto(newFileDtos, resultCount);
    }

    private String formatFileSize(long sizeInBytes) {
        double size = sizeInBytes;
        String unit = "bytes"; // 기본 단위는 바이트

        if (size >= 1024) {
            size /= 1024;
            unit = "KByte";
        }
        if (size >= 1024) {
            size /= 1024;
            unit = "MByte";
        }
        if (size >= 1024) {
            size /= 1024;
            unit = "GByte";
        }

        if (size >= 1024) {
            size /= 1024;
            unit = "TByte";
        }

        return String.format("%.2f %s", size, unit); // 소수점 두 자리까지 표시
    }

    public FileDto convertToFileDto(String fileName, String absolutePath, String date, String extendName, String size) {
        FileDto fileDto = new FileDto();

        fileDto.setFileName(fileName);
        fileDto.setAbsolutePath(absolutePath);
        fileDto.setDate(date);
        fileDto.setExtendName(extendName);
        fileDto.setSize(size);

        return fileDto;
    }

    public ResultDto convertToResultDto(List<FileDto> fileDtos, Integer resultCount) {
        ResultDto resultDto = new ResultDto();

        resultDto.setFileDtos(fileDtos);
        resultDto.setResultCount(resultCount);

        return resultDto;
    }
}
