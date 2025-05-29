package org.example.airport.strategy;

import org.springframework.stereotype.Component;

@Component
public class BusinessBaggageStrategy implements BaggageCheckStrategy {

    @Override
    public boolean isAllowed(double userBaggage, double flightLimit) {
        // Business class can exceed the limit by 50%
        return userBaggage <= flightLimit * 1.5;
    }
}