package com.ll.searchengine.controller;

import com.ll.searchengine.dto.FileDto;
import com.ll.searchengine.dto.ResultDto;
import com.ll.searchengine.dto.UcsiUserDto;
import com.ll.searchengine.entity.UcsiUser;
import com.ll.searchengine.repository.UcsiUserRepository;
import com.ll.searchengine.service.FileService;
import com.ll.searchengine.service.UcsiUserService;
import com.ll.searchengine.util.FileSettingUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UcsiUserService ucsiUserService;
    private final FileService fileService;
    private final UcsiUserRepository ucsiUserRepository;

    @GetMapping("/")
    public String mainController() {
        return "main.html";
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UcsiUserDto ucsiUserDto) {
        UcsiUserDto createucsiUserDto = ucsiUserService.signup(ucsiUserDto);
        UcsiUserDto ResponseEntity;
        return new ResponseEntity<>(createucsiUserDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public String searchController(Authentication auth, Model model) {
        if (auth == null)
            return "redirect:/";

        model.addAttribute("username", auth.getName());

        return "search.html";
    }

    @GetMapping("/result")
    public String resultController(
            Authentication auth,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "type", defaultValue = "all") String type,
            Model model,
            HttpServletRequest request
    ) {
        if (auth == null)
            return "redirect:/";

        List<FileDto> fileDtos = new ArrayList<>();
        File directory = new File(FileSettingUtil.FILEPATH);
        fileService.searchFiles(directory, keyword, fileDtos, type);

        model.addAttribute("results", fileDtos);
        model.addAttribute("resultcounts", fileDtos.size());
        model.addAttribute("keyword", keyword);
        System.out.println("find");

        return "result.html";
    }

    @PostMapping("/result")
    public String restResultController(
            Authentication auth,
            @RequestBody Map<String, String> data,
            Model model
    ) {
        if (auth == null)
            return null;

        String keyword = data.getOrDefault("searchKeyword", "");
        String type = data.getOrDefault("type", "all");

        List<FileDto> fileDtos = new ArrayList<>();
        File directory = new File(FileSettingUtil.FILEPATH);
        fileService.searchFiles(directory, keyword, fileDtos, type);

        model.addAttribute("results", fileDtos);
        model.addAttribute("resultcounts", fileDtos.size());

        System.out.println("Call");
        System.out.println(keyword);
        System.out.println(type);
        System.out.println(fileDtos.size());

        return "/result :: #result-wrapper";
    }
}
