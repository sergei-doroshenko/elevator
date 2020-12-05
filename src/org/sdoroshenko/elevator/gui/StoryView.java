package org.sdoroshenko.elevator.gui;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.sdoroshenko.elevator.model.Passenger;
import org.sdoroshenko.elevator.model.Story;

public class StoryView {
	private double  yPoint = 50;
	private List<PassengerView> dispatchStoryContainer;
	private List<PassengerView> arrivalStoryContainer;
	
	public StoryView(Integer integer, Story story) {
	}

	public StoryView(Story story) {
		super();
		dispatchStoryContainer = new ArrayList<PassengerView> ();
		arrivalStoryContainer = new ArrayList<PassengerView> ();
		
		for (Passenger p : story.getDispatchStoryContainer()) {
			dispatchStoryContainer.add(new PassengerView(p.getDestinationStory().getId()));
		}
	}
	
	public synchronized void draw (Graphics2D g2, Rectangle2D bounds) {
		double leftX = bounds.getMinX();
		double rightX = bounds.getMaxX();
		double centerX = bounds.getCenterX();
		
		g2.draw(new Line2D.Double (leftX, yPoint, rightX - centerX - ConstantsGUI.LINES_OFFCET, yPoint));
		g2.draw(new Line2D.Double (leftX + centerX + ConstantsGUI.LINES_OFFCET, yPoint, rightX, yPoint));
		
		yPoint = yPoint - ConstantsGUI.PASSENGER_WIDTH - ConstantsGUI.PASSENGER_VIEW_OFFCET;
		
		drawContainer(g2, dispatchStoryContainer, leftX + centerX + ConstantsGUI.LINES_OFFCET, rightX - ConstantsGUI.PASSENGER_WIDTH);
		drawContainer(g2, arrivalStoryContainer, leftX, rightX - centerX - ConstantsGUI.LINES_OFFCET);
	}
	
	private void drawContainer (Graphics2D g2, List<PassengerView> container, double lX, double rX) {
		
		double y = yPoint - ConstantsGUI.PASSENGER_WIDTH - ConstantsGUI.PASSENGER_VIEW_OFFCET;
		double dx = 0;
		double dy = ConstantsGUI.PASSENGER_WIDTH + ConstantsGUI.PASSENGER_VIEW_OFFCET;
		
		for (PassengerView pv : container) {
			if (lX + dx > rX) {
				dx = ConstantsGUI.PASSENGER_VIEW_OFFCET;
				dy -= ConstantsGUI.PASSENGER_HEIGHT + ConstantsGUI.PASSENGER_VIEW_OFFCET;
			}
			pv.draw(g2, lX + dx, y + dy);
			dx += ConstantsGUI.PASSENGER_WIDTH + ConstantsGUI.PASSENGER_VIEW_OFFCET;
		}
	}
	
	public synchronized void addPassengerView (PassengerView pv) {
		dispatchStoryContainer.add(pv);
	}
	
	public synchronized void movePassengerView (PassengerView pv) {
		dispatchStoryContainer.remove(pv);
	}
	
	public synchronized PassengerView getPassengerView (int destinationStoryId) {
		for (PassengerView pv : dispatchStoryContainer) {
			if (pv.getDestinationStoryId() == destinationStoryId) {
				return pv;
			}	
		}
		return null;
	}
	
	public synchronized void dropOutPassengerView (PassengerView pv) {
		arrivalStoryContainer.add(pv);
	}

	public double getY() {
		return yPoint;
	}

	public void setY(double y) {
		this.yPoint = y;
	}
}
