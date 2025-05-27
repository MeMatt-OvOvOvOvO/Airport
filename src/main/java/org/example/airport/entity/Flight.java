package org.example.airport.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;
    private String destination;

    private int availableSeats;
    private double baggageLimit;
    private boolean started = false;
}