package org.example.airport.tests;

import org.example.airport.strategy.DefaultBaggageCheckStrategy;
import org.example.airport.strategy.EconomyBaggageStrategy;
import org.example.airport.strategy.BusinessBaggageStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultBaggageCheckStrategyTest {

    private final DefaultBaggageCheckStrategy defaultStrategy = new DefaultBaggageCheckStrategy();
    private final EconomyBaggageStrategy economyStrategy = new EconomyBaggageStrategy();
    private final BusinessBaggageStrategy businessStrategy = new BusinessBaggageStrategy();

    // Default strategy
    @Test
    public void shouldAllowWhenBaggageIsBelowLimit_Default() {
        assertTrue(defaultStrategy.isAllowed(15.0, 20.0));
    }

    @Test
    public void shouldAllowWhenBaggageEqualsLimit_Default() {
        assertTrue(defaultStrategy.isAllowed(20.0, 20.0));
    }

    @Test
    public void shouldRejectWhenBaggageExceedsLimit_Default() {
        assertFalse(defaultStrategy.isAllowed(25.0, 20.0));
    }

    // Economy strategy
    @Test
    public void shouldAllow_EconomyStrategy() {
        assertTrue(economyStrategy.isAllowed(18.0, 20.0));
    }

    @Test
    public void shouldReject_EconomyStrategy() {
        assertFalse(economyStrategy.isAllowed(22.0, 20.0));
    }

    // Business strategy
    @Test
    public void shouldAlwaysAllow_BusinessStrategy() {
        assertTrue(businessStrategy.isAllowed(30.0, 20.0));
        assertFalse(businessStrategy.isAllowed(50.0, 20.0));
    }
}