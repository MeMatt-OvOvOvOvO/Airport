package org.example.airport.strategy;

import org.springframework.stereotype.Component;

@Component
public class BusinessClassBaggageCheckStrategy implements BaggageCheckStrategy {

    @Override
    public boolean isAllowed(double userBaggage, double flightLimit) {
        // Klasa biznesowa ma +10kg tolerancji
        return userBaggage <= (flightLimit + 10);
    }
}