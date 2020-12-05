package org.sdoroshenko.elevator.model;

import java.util.HashSet;
import java.util.Set;

public class Story {
	private static int nextID = 0;
	private final int id;
	private final Set<Passenger> dispatchStoryContainer;
	private final Set<Passenger> arrivalStoryContainer;

	private boolean elevator;
	
	public Story() {
		id = nextID++;
		dispatchStoryContainer = new HashSet<>();
		arrivalStoryContainer = new HashSet<>();
		elevator = false;
	}

	public static void setNextID(int nextID) {
		Story.nextID = nextID;
	}

	public void add (Passenger passenger) {
		dispatchStoryContainer.add(passenger);
	}
	
	public int getId() {
		return id;
	}

	public Set<Passenger> getDispatchStoryContainer() {
		return dispatchStoryContainer;
	}

	public Set<Passenger> getArrivalStoryContainer() {
		return arrivalStoryContainer;
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
