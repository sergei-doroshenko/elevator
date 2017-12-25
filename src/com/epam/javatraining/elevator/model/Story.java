package com.epam.javatraining.elevator.model;

import java.util.ArrayList;
import java.util.List;

public class Story {
	private static int nextID = 0;
	private int id;
	private List<Passenger> dispatchStoryContainer;
	private List<Passenger> arrivalStoryContainer;
	private boolean elevator;
	
	
	public Story() {
		id = nextID++;
		dispatchStoryContainer = new ArrayList<Passenger>();
		arrivalStoryContainer = new ArrayList<Passenger>();
		elevator = false;
	}
	
	public static int getNextID() {
		return nextID;
	}

	public static void setNextID(int nextID) {
		Story.nextID = nextID;
	}

	public void add (Passenger passenger) {
		dispatchStoryContainer.add(passenger);
	}
	
	public void move (Passenger passenger) {
		dispatchStoryContainer.remove(passenger);
	}
	
	public void dropOff (Passenger passenger) {
		arrivalStoryContainer.add(passenger);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Passenger> getDispatchStoryContainer() {
		return dispatchStoryContainer;
	}

	public void setDispatchStoryContainer(List<Passenger> dispatchStoryContainer) {
		this.dispatchStoryContainer = dispatchStoryContainer;
	}

	public List<Passenger> getArrivalStoryContainer() {
		return arrivalStoryContainer;
	}

	public void setArrivalStoryContainer(List<Passenger> arrivalStoryContainer) {
		this.arrivalStoryContainer = arrivalStoryContainer;
	}
	
	public boolean isElevator() {
		return elevator;
	}

	public void setElevator(boolean elevator) {
		this.elevator = elevator;
	}
	
	public boolean isCompanion (boolean moveUp) {
		for (Passenger p : dispatchStoryContainer) {
			if (p.isMoveUp() == moveUp) {
				return true;
				
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Story other = (Story) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Story [id=" + id + ", dispatchStoryContainer="
				+ dispatchStoryContainer + ", arrivalStoryContainer="
				+ arrivalStoryContainer + "]";
	}
}
