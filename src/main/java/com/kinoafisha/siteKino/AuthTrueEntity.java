package com.kinoafisha.siteKino;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AuthTrue")
@Data
public class AuthTrueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer authTrueId;

    Integer value;
}
