package org.example.airport.strategy;

public interface BaggageCheckStrategy {
    boolean isAllowed(double userBaggage, double flightLimit);
}