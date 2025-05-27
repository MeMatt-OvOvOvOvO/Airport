package org.example.airport.strategy;

import org.springframework.stereotype.Component;

@Component
public class DefaultBaggageCheckStrategy implements BaggageCheckStrategy {

    @Override
    public boolean isAllowed(double userBaggage, double flightLimit) {
        return userBaggage <= flightLimit;
    }
}