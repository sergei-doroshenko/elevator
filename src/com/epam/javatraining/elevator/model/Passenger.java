package com.epam.javatraining.elevator.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Passenger {
	private static int nextID = 0;
	private int id;
	private Story startStory;
	private Story destinationStory;
	private TransportationState transpontationState;
	private boolean moveUp;
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	
	public Passenger() {
		id = nextID++;
		setTranspontationState(TransportationState.NOT_STARTED);
	}

	public Passenger(Story startStory, Story destinationStory) {
		super();
		id = nextID++;
		this.startStory = startStory;
		this.destinationStory = destinationStory;
		setTranspontationState(TransportationState.NOT_STARTED);
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

	public TransportationState getTranspontationState() {
		return transpontationState;
	}

	public void setTranspontationState(TransportationState newState) {
		TransportationState oldState = transpontationState;
		transpontationState = newState;
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
				+ ", transpontationState=" + transpontationState + "]";
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
