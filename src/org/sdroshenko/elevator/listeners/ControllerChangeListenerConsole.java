package org.sdroshenko.elevator.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.log4j.Logger;
import org.sdroshenko.elevator.model.Passenger;

public class ControllerChangeListenerConsole implements PropertyChangeListener {
	private static Logger log = Logger.getLogger(ControllerChangeListenerConsole.class);
	
	public ControllerChangeListenerConsole() {
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String sourceName = event.getSource().getClass().getSimpleName();
		String message = null;
		if (sourceName.equals("Controller")) {
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
			
		} else if (sourceName.equals("Passenger")) {
			
			Passenger p = (Passenger) event.getSource();
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
				case NOT_STARTED:
					break;
				default:
					break;
			}
			
		}
	
		log.info(" ----> " + message);
		System.out.println (message);
	}
}
