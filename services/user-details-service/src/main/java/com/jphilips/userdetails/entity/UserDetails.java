package com.jphilips.userdetails.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDetails {

    @Id
    private Long id; // Same as Auth ID

    @Column(nullable = false)
    private String name;

    private String address;

    @Column(nullable = false)
    private LocalDate birthDate;

}
