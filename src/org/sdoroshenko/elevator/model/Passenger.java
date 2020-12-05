package org.sdoroshenko.elevator.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	private TransportationState transportationState;

	public Passenger(Story startStory, Story destinationStory, PropertyChangeListener listener) {
		super();
		id = nextID++;
		this.startStory = startStory;
		this.destinationStory = destinationStory;
		this.moveUp = destinationStory.getId() > startStory.getId();
		setTransportationState(TransportationState.NOT_STARTED);
		changeSupport.addPropertyChangeListener(listener);
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
		changeSupport.firePropertyChange("transportationState", oldState, newState);
	}
	
	public boolean isMoveUp() {
		return moveUp;
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
