package org.sdroshenko.elevator.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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

	public Passenger(Story startStory, Story destinationStory) {
		super();
		id = nextID++;
		this.startStory = startStory;
		this.destinationStory = destinationStory;
		setTransportationState(TransportationState.NOT_STARTED);
	}
	
	public static int getNextID() {
		return nextID;
	}

	public static void setNextID(int nextPassengerID) {
		Passenger.nextID = nextPassengerID;
	}

	public int getID() {
		return id;
	}

	public void setID(int passengerID) {
		this.id = passengerID;
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

	public void setMoveUp(boolean moveUp) {
		this.moveUp = moveUp;
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
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
	
	public enum TransportationState {
		NOT_STARTED, IN_PROGRESS, COMPLETED, ABORTED
	}
}
