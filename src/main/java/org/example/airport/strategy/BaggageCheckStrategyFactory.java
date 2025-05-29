package org.example.airport.strategy;

import org.example.airport.enums.TravelClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BaggageCheckStrategyFactory {

    private final Map<String, BaggageCheckStrategy> strategies;

    @Autowired
    public BaggageCheckStrategyFactory(Map<String, BaggageCheckStrategy> strategies) {
        this.strategies = strategies;
    }

    public BaggageCheckStrategy getStrategy(TravelClass travelClass) {
        return strategies.get(travelClass.name());
    }
}