package org.sdoroshenko.elevator.gui;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ElevatorView {
	
	private List<PassengerView> passengers;
	private double x;
	private double y;
	
	public ElevatorView (Rectangle2D bounds, int storiesNumber) {
		x = bounds.getCenterX() - ConstantsGUI.ELEVATOR_WIDTH/2;
		y = bounds.getMinY() + (storiesNumber * ConstantsGUI.STORY_HEIGHT) - ConstantsGUI.ELEVATOR_HEIGHT;
		passengers = new ArrayList<PassengerView> ();
	}

	public void move (int fromStory, int toStory) {
		
		if (fromStory < toStory) {
	    	y -= ConstantsGUI.STORY_HEIGHT;
	    } else {
	    	y += ConstantsGUI.STORY_HEIGHT;
	    } 
	}
	
	public synchronized void addPassengerView (PassengerView pv) {
		passengers.add(pv);
	}
	
	public synchronized PassengerView getPassengerView (int destinationStoryId) {
		for (PassengerView pv : passengers) {
			if (pv.getDestinationStoryId() == destinationStoryId) {
				return pv;
			}	
		}
		return null;
	}
	
	public synchronized void  dropOutPassengersView (PassengerView pv) {
		passengers.remove(pv);
	}
	
	public synchronized void draw (Graphics2D g2, int elevatorContainerSize) {
		Rectangle2D elevator = new Rectangle2D.Double (x, y, ConstantsGUI.ELEVATOR_WIDTH, ConstantsGUI.ELEVATOR_HEIGHT);
		g2.draw(elevator);
		int dx = ConstantsGUI.PASSENGER_VIEW_OFFCET;
		int dy = ConstantsGUI.PASSENGER_VIEW_OFFCET;
		for (PassengerView pv : passengers) {
			if (x + dx > elevator.getMaxX() - ConstantsGUI.PASSENGER_WIDTH) {
				dx = 2;
				dy += ConstantsGUI.PASSENGER_HEIGHT + ConstantsGUI.PASSENGER_VIEW_OFFCET;
			}
			pv.draw(g2, x + dx, y + dy);
			dx += ConstantsGUI.PASSENGER_WIDTH + ConstantsGUI.PASSENGER_VIEW_OFFCET;
		}
	}
}
