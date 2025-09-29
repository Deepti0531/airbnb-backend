package com.projects.airBnbApp.repository;

import com.projects.airBnbApp.entity.Guest;
import com.projects.airBnbApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByUser(User user);
}