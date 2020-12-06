package org.sdoroshenko.elevator.gui;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class SwingElevatorView {
	
	private List<SwingPassengerView> passengers;
	private double x;
	private double y;
	
	public SwingElevatorView(Rectangle2D bounds, int storiesNumber) {
		x = bounds.getCenterX() - ConstantsGUI.ELEVATOR_WIDTH/2;
		y = bounds.getMinY() + (storiesNumber * ConstantsGUI.STORY_HEIGHT) - ConstantsGUI.ELEVATOR_HEIGHT;
		passengers = new ArrayList<> ();
	}

	public void move (int fromStory, int toStory) {
		
		if (fromStory < toStory) {
	    	y -= ConstantsGUI.STORY_HEIGHT;
	    } else {
	    	y += ConstantsGUI.STORY_HEIGHT;
	    } 
	}
	
	public synchronized void addPassengerView (SwingPassengerView pv) {
		passengers.add(pv);
	}
	
	public synchronized SwingPassengerView getPassengerView (int destinationStoryId) {
		for (SwingPassengerView pv : passengers) {
			if (pv.getDestinationStoryId() == destinationStoryId) {
				return pv;
			}	
		}
		return null;
	}
	
	public synchronized void  dropOutPassengersView (SwingPassengerView pv) {
		passengers.remove(pv);
	}
	
	public synchronized void draw (Graphics2D g2) {
		Rectangle2D elevator = new Rectangle2D.Double (x, y, ConstantsGUI.ELEVATOR_WIDTH, ConstantsGUI.ELEVATOR_HEIGHT);
		g2.draw(elevator);
		int dx = ConstantsGUI.PASSENGER_VIEW_OFFCET;
		int dy = ConstantsGUI.PASSENGER_VIEW_OFFCET;
		for (SwingPassengerView pv : passengers) {
			if (x + dx > elevator.getMaxX() - ConstantsGUI.PASSENGER_WIDTH) {
				dx = 2;
				dy += ConstantsGUI.PASSENGER_HEIGHT + ConstantsGUI.PASSENGER_VIEW_OFFCET;
			}
			pv.draw(g2, x + dx, y + dy);
			dx += ConstantsGUI.PASSENGER_WIDTH + ConstantsGUI.PASSENGER_VIEW_OFFCET;
		}
	}
}
