package org.example.library.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Reader.java
@Entity
@Table(name = "readers")
@Getter
@Setter
@NoArgsConstructor
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    private String phone;

    @Column(name = "library_card", nullable = false, unique = true, length = 30)
    private String libraryCard;

    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @OneToMany(mappedBy = "reader", fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();
}