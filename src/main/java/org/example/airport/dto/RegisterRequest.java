package org.example.airport.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "RegisterRequest", description = "Dane potrzebne do rejestracji nowego użytkownika")
@AllArgsConstructor
public class RegisterRequest {

    @Schema(description = "Nazwa użytkownika", example = "jan123", required = true)
    private String username;

    @Schema(description = "Hasło", example = "secret123", required = true)
    private String password;

    @Schema(description = "Rola użytkownika", example = "ROLE_USER", required = true)
    private String role;

    @Schema(description = "Waga bagażu użytkownika", example = "12.5", required = true)
    private double baggageWeight;
}