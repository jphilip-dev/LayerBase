package com.jphilips.analytics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "registered_event_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisteredEventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "eventId", referencedColumnName = "eventId")
    private HeaderEventLog header;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;
}
