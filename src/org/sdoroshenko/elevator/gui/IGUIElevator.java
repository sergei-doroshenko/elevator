package org.sdoroshenko.elevator.gui;

import org.sdoroshenko.elevator.multithreading.IController;
import org.sdoroshenko.elevator.util.Configuration;

public interface IGUIElevator {
	
	Configuration getConfiguration();
		
	void initGUI (IController controller);
	
	void elevatorViewMove(int oldStoryId, int newStoryId);

	void movePassengerView(int startStoryId, int destinationStoryId);

	void dropOutPassengerView(int destinationStoryId);

	void appendLog(String string);

	void updateChanges();

	void changeButtonText(String string);

}
