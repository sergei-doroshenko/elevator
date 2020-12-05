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
	private int id;
	private Story startStory;
	private Story destinationStory;
	private TransportationState transportationState;
	private boolean moveUp;
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public Passenger() {
		id = nextID++;
		setTransportationState(TransportationState.NOT_STARTED);
	}

	public Passenger(Story startStory, Story destinationStory, PropertyChangeListener listener) {
		super();
		id = nextID++;
		this.startStory = startStory;
		setDestinationStory(destinationStory);
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

	public void setStartStory(Story startStory) {
		this.startStory = startStory;
	}

	public Story getDestinationStory() {
		return destinationStory;
	}

	public void setDestinationStory(Story destinationStory) {
		this.destinationStory = destinationStory;
		moveUp = destinationStory.getId() > startStory.getId();
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
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
	
	public enum TransportationState {
		NOT_STARTED, IN_PROGRESS, COMPLETED, ABORTED
	}
}
