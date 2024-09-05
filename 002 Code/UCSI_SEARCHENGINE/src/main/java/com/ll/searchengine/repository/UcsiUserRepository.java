package com.ll.searchengine.repository;

import com.ll.searchengine.entity.UcsiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UcsiUserRepository extends JpaRepository<UcsiUser, Long> {
    Optional<UcsiUser> findByUsername(String userName);
}
