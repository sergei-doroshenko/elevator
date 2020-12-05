package org.sdroshenko.elevator.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Configuration {
	
	public static final String STORIES_NUMBER = "storiesNumber";
	public static final String ELEVATOR_CAPACITY = "elevatorCapacity";
	public static final String PASSENGERS_NUMBER = "passengersNumber";
	public static final String ANIMATION_BOOST = "animationBoost";
	private String storiesNumberStr;
	private String elevatorCapacityStr;
	private String passengersNumberStr;
	private int animationBoost;
	
	public Configuration (String fileName) {
		/**Read init parameters from file */
		ResourceBundle bundle = ResourceBundle.getBundle(fileName, Locale.ENGLISH);
		storiesNumberStr = bundle.getString(STORIES_NUMBER);
		elevatorCapacityStr = bundle.getString(ELEVATOR_CAPACITY);
		passengersNumberStr = bundle.getString(PASSENGERS_NUMBER);
		animationBoost = Integer.parseInt(bundle.getString(ANIMATION_BOOST));
	}
	
	public Configuration (String storiesNumber, String elevatorCapacity, String passengersNumber, int animationBoost){
		this.storiesNumberStr = storiesNumber;
		this.elevatorCapacityStr = elevatorCapacity;
		this.passengersNumberStr = passengersNumber;
		this.animationBoost = animationBoost;
	}
	
	public String getStoriesNumberStr() {
		return storiesNumberStr;
	}

	public String getElevatorCapacityStr() {
		return elevatorCapacityStr;
	}

	public String getPassengersNumberStr() {
		return passengersNumberStr;
	}

	public int getAnimationBoost() {
		return animationBoost;
	}
	
	public int getStoriesNumberInt() {
		return Integer.parseInt(storiesNumberStr);
	}

	public int getElevatorCapacityInt() {
		return Integer.parseInt(elevatorCapacityStr);
	}

	public int getPassengersNumberInt() {
		return Integer.parseInt(passengersNumberStr);
	}
}
