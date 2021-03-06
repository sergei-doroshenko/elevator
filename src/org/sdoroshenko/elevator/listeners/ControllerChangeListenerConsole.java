package org.sdoroshenko.elevator.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.sdoroshenko.elevator.gui.SwingPassengerView;
import org.sdoroshenko.elevator.model.Passenger;

public class ControllerChangeListenerConsole implements PropertyChangeListener {
	private static Logger log = LogManager.getLogger(ControllerChangeListenerConsole.class);
	
	public ControllerChangeListenerConsole() {
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String sourceName = event.getSource().getClass().getSimpleName();
		String message = null;
		if (sourceName.equals("SwingControllerView")) {
			String propName = event.getPropertyName();
			if (propName.equals("currentStore")) {
				message = "MOVING_ELEVATOR (from story-" 
					+ event.getOldValue() + " to story-" + event.getNewValue() + ")";
			
			} else if (propName.equals("started")){
				if (event.getNewValue().equals(false)) {
					message = "COMPLETION_TRANSPORTATION";
				} else {
					message = "STARTING_TRANSPORTATION";
				}
				
			}
			
		} else if (sourceName.equals("SwingPassengerView")) {
			SwingPassengerView view = (SwingPassengerView) event.getSource();
			Passenger p = view.getPassenger();
			Passenger.TransportationState newState = (Passenger.TransportationState) event.getNewValue();
			int id = p.getID();
			int startStoryId = p.getStartStory().getId();
			int destinationStoryId = p.getDestinationStory().getId();
			
			switch (newState) {
				case IN_PROGRESS : message = "BOADING_PASSENGER ( " 
						+ id + " on story-" + startStoryId + " )";
					break;
				case COMPLETED : message = "DEBOADING_PASSENGER ( " 
						+ id + " on story-" + destinationStoryId + " )";
					break;
				case ABORTED : message = "ABORTING_PASSENGER";
					break;
				default:
					break;
			}
			
		}
	
		log.info(message);
	}
}
