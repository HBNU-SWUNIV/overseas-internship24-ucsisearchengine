package com.ll.searchengine.service;

import com.ll.searchengine.UserRole;
import com.ll.searchengine.entity.UcsiUser;
import com.ll.searchengine.repository.UcsiUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UcsiUserRepository ucsiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Optional<UcsiUser> ucsiUserOptional = this.ucsiUserRepository.findByUsername(username); // Make Ucsi User Repository

        System.out.println("Call");

        if (ucsiUserOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        UcsiUser ucsiUser = ucsiUserOptional.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        return new User(ucsiUser.getUsername(), ucsiUser.getPassword(), authorities);
    }
}
