package org.sdoroshenko.elevator.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;

import org.sdoroshenko.elevator.gui.ConstantsGUI;
import org.sdoroshenko.elevator.gui.IGUIElevator;
import org.sdoroshenko.elevator.gui.PassengerView;
import org.sdoroshenko.elevator.model.Passenger;


public class ControllerChangeListenerGUI implements PropertyChangeListener {
    private static Logger log = Logger.getLogger(ControllerChangeListenerGUI.class);
    private IGUIElevator gui;

    public ControllerChangeListenerGUI(IGUIElevator gui) {
        this.gui = gui;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        String sourceName = event.getSource().getClass().getSimpleName();

        String message = null;

        if (sourceName.equals("Controller")) {
            String propName = event.getPropertyName();
            if (propName.equals("currentStore")) {
                int oldStoryId = (Integer) event.getOldValue();
                int newStoryId = (Integer) event.getNewValue();
                message = "MOVING_ELEVATOR (from story-"
                        + event.getOldValue() + " to story-" + event.getNewValue() + ")";

                gui.elevatorViewMove(oldStoryId, newStoryId);
            } else if (propName.equals("started")) {
                if (event.getNewValue().equals(false)) {
                    message = "COMPLETION_TRANSPORTATION";
                    gui.changeButtonText(ConstantsGUI.VIEW_LOG_BUTTON_TEXT);
                } else {
                    message = "STARTING_TRANSPORTATION";
                }
            }
        } else if (sourceName.equals("PassengerView")) {
            PassengerView view = (PassengerView) event.getSource();
            Passenger p = view.getPassenger();
            Passenger.TransportationState newState = (Passenger.TransportationState) event.getNewValue();
            int id = p.getID();
            int startStoryId = p.getStartStory().getId();
            int destinationStoryId = p.getDestinationStory().getId();

            switch (newState) {
                case IN_PROGRESS:
                    message = "BOADING_PASSENGER ( "
                            + id + " on story-" + startStoryId + " )";
                    gui.movePassengerView(startStoryId, destinationStoryId);
                    break;
                case COMPLETED:
                    message = "DEBOADING_PASSENGER ( "
                            + id + " on story-" + destinationStoryId + " )";
                    gui.dropOutPassengerView(destinationStoryId);
                    break;
                case ABORTED:
                    message = "ABORTING_PASSENGER";
                    break;
                case NOT_STARTED:
                    break;
                default:
                    break;
            }
        }

        gui.appendLog(message);
        log.info(message);
        gui.updateChanges();
    }
}
