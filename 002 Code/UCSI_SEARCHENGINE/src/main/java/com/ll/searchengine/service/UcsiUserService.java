package com.ll.searchengine.service;

import com.ll.searchengine.dto.UcsiUserDto;
import com.ll.searchengine.entity.UcsiUser;
import com.ll.searchengine.repository.UcsiUserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class UcsiUserService {
    private final UcsiUserRepository ucsiUserRepository;
    public UcsiUserDto signup(UcsiUserDto ucsiUserDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UcsiUser ucsiUser = new UcsiUser();

        String username = ucsiUserDto.getUsername();
        String password = ucsiUserDto.getPassword();

        ucsiUser.setUsername(username);
        ucsiUser.setPassword(passwordEncoder.encode(password));

        ucsiUserRepository.save(ucsiUser);

        ucsiUserDto.setId(ucsiUser.getId());

        return ucsiUserDto;
    }
}
