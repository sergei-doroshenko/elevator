package org.sdoroshenko.elevator.gui;

import org.sdoroshenko.elevator.model.Passenger;

public interface PassengerView {
    void fireTransportationStateChange(Passenger.TransportationState oldState, Passenger.TransportationState newState);
}
