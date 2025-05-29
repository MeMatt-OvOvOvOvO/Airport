package org.example.airport.tests;

import org.example.airport.strategy.DefaultBaggageCheckStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultBaggageCheckStrategyTest {

    private final DefaultBaggageCheckStrategy strategy = new DefaultBaggageCheckStrategy();

    @Test
    public void shouldAllowWhenBaggageIsBelowLimit() {
        boolean result = strategy.isAllowed(15.0, 20.0);
        assertTrue(result);
    }

    @Test
    public void shouldAllowWhenBaggageEqualsLimit() {
        boolean result = strategy.isAllowed(20.0, 20.0);
        assertTrue(result);
    }

    @Test
    public void shouldRejectWhenBaggageExceedsLimit() {
        boolean result = strategy.isAllowed(25.0, 20.0);
        assertFalse(result);
    }
}