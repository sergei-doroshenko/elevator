package org.sdroshenko.elevator.util;


public class Validator {
	
	public static void validate (Configuration configuration) {
		if (configuration.getStoriesNumberInt() <= 0) {
			throw new IllegalArgumentException();
		}
	
		if (configuration.getElevatorCapacityInt() <= 0) {
			throw new IllegalArgumentException();
		}
		
		if (configuration.getPassengersNumberInt() < 0) {
			throw new IllegalArgumentException();
		}

		if (configuration.getAnimationBoost() < 0) {
			throw new IllegalArgumentException();
		}
	}
}
