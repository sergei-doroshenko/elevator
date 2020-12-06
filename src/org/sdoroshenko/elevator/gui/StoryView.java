package org.sdoroshenko.elevator.gui;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.sdoroshenko.elevator.model.Passenger;
import org.sdoroshenko.elevator.model.Story;

public class StoryView {
	private double  yPoint = 50;
	private List<SwingPassengerView> dispatchStoryContainer;
	private List<SwingPassengerView> arrivalStoryContainer;

	public StoryView(final Story story, final PropertyChangeListener listener) {
		super();
		dispatchStoryContainer = new ArrayList<> ();
		arrivalStoryContainer = new ArrayList<> ();
		
		for (Passenger p : story.getDispatchStoryContainer()) {
			SwingPassengerView swingPassengerView = new SwingPassengerView(p.getDestinationStory().getId(), listener, p);
			p.setView(swingPassengerView);
			dispatchStoryContainer.add(swingPassengerView);
		}
	}
	
	public synchronized void draw (Graphics2D g2, Rectangle2D bounds) {
		double leftX = bounds.getMinX();
		double rightX = bounds.getMaxX();
		double centerX = bounds.getCenterX();
		
		g2.draw(new Line2D.Double (leftX, yPoint, rightX - centerX - ConstantsGUI.LINES_OFFSET, yPoint));
		g2.draw(new Line2D.Double (leftX + centerX + ConstantsGUI.LINES_OFFSET, yPoint, rightX, yPoint));
		
		yPoint = yPoint - ConstantsGUI.PASSENGER_WIDTH - ConstantsGUI.PASSENGER_VIEW_OFFCET;
		
		drawContainer(g2, dispatchStoryContainer, leftX + centerX + ConstantsGUI.LINES_OFFSET, rightX - ConstantsGUI.PASSENGER_WIDTH);
		drawContainer(g2, arrivalStoryContainer, leftX, rightX - centerX - ConstantsGUI.LINES_OFFSET);
	}
	
	private void drawContainer (Graphics2D g2, List<SwingPassengerView> container, double lX, double rX) {
		
		double y = yPoint - ConstantsGUI.PASSENGER_WIDTH - ConstantsGUI.PASSENGER_VIEW_OFFCET;
		double dx = 0;
		double dy = ConstantsGUI.PASSENGER_WIDTH + ConstantsGUI.PASSENGER_VIEW_OFFCET;
		
		for (SwingPassengerView pv : container) {
			if (lX + dx > rX) {
				dx = ConstantsGUI.PASSENGER_VIEW_OFFCET;
				dy -= ConstantsGUI.PASSENGER_HEIGHT + ConstantsGUI.PASSENGER_VIEW_OFFCET;
			}
			pv.draw(g2, lX + dx, y + dy);
			dx += ConstantsGUI.PASSENGER_WIDTH + ConstantsGUI.PASSENGER_VIEW_OFFCET;
		}
	}
	
	public synchronized void movePassengerView (SwingPassengerView pv) {
		dispatchStoryContainer.remove(pv);
	}
	
	public synchronized SwingPassengerView getPassengerView (int destinationStoryId) {
		for (SwingPassengerView pv : dispatchStoryContainer) {
			if (pv.getDestinationStoryId() == destinationStoryId) {
				return pv;
			}	
		}
		return null;
	}
	
	public synchronized void dropOutPassengerView (SwingPassengerView pv) {
		arrivalStoryContainer.add(pv);
	}

	public void setY(double y) {
		this.yPoint = y;
	}
}
