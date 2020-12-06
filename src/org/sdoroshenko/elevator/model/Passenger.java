package org.sdoroshenko.elevator.model;

import org.sdoroshenko.elevator.gui.PassengerView;
import org.sdoroshenko.elevator.gui.SwingPassengerView;

import java.util.Objects;

/**
 * The elevator passenger.
 *
 * @author Sergei Doroshenko
 */
public class Passenger {
	private static int nextID = 0;
	private final int id;
	private final Story startStory;
	private final Story destinationStory;
	private final boolean moveUp;
	private PassengerView view;

	private TransportationState transportationState;

	public Passenger(final Story startStory, final Story destinationStory) {
		this.id = nextID++;
		this.startStory = startStory;
		this.destinationStory = destinationStory;
		this.moveUp = destinationStory.getId() > startStory.getId();
		setTransportationState(TransportationState.NOT_STARTED);
		startStory.add(this);
	}

	public static void setNextID(int nextPassengerID) {
		Passenger.nextID = nextPassengerID;
	}

	public int getID() {
		return id;
	}
	
	public Story getStartStory() {
		return startStory;
	}

	public Story getDestinationStory() {
		return destinationStory;
	}

	public TransportationState getTransportationState() {
		return transportationState;
	}

	public void setTransportationState(TransportationState newState) {
		TransportationState oldState = transportationState;
		transportationState = newState;

		if (view != null) {
			view.fireTransportationStateChange(oldState, newState);
		}
	}
	
	public boolean isMoveUp() {
		return moveUp;
	}

	public void setView(SwingPassengerView view) {
		this.view = view;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Passenger passenger = (Passenger) o;
		return id == passenger.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Passenger [passengerID=" + id + ", startStory="
				+ startStory.getId() + ", destinationStory=" + destinationStory.getId()
				+ ", transpontationState=" + transportationState + "]";
	}
	
	public enum TransportationState {
		NOT_STARTED, IN_PROGRESS, COMPLETED, ABORTED
	}
}
