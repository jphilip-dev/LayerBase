package com.jphilips.userdetails.repository;

import com.jphilips.userdetails.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
