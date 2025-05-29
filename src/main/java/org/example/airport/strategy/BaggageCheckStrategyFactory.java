package org.example.airport.strategy;

import org.example.airport.enums.TravelClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BaggageCheckStrategyFactory {

    private final Map<TravelClass, BaggageCheckStrategy> strategies;

    public BaggageCheckStrategyFactory(
            EconomyBaggageStrategy economyStrategy,
            BusinessBaggageStrategy businessStrategy
    ) {
        this.strategies = new HashMap<>();
        strategies.put(TravelClass.ECONOMY, economyStrategy);
        strategies.put(TravelClass.BUSINESS, businessStrategy);
    }

    public BaggageCheckStrategy getStrategy(TravelClass travelClass) {
        return strategies.get(travelClass);
    }
}