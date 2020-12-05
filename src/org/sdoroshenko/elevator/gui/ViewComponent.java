package org.sdoroshenko.elevator.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import org.sdoroshenko.elevator.model.Story;

public class ViewComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	private ElevatorFrame frame;
	private ElevatorView elevator;
	private Map <Integer, StoryView> storiesViewContainer;
	private int storiesNumber;
	
	public ViewComponent(ElevatorFrame frame) {
		this.setFrame(frame);
		storiesNumber = Integer.parseInt(frame.getControlPanel().getStroriesNumberFild().getText());
		storiesViewContainer = new HashMap <Integer, StoryView> ();
	}
	
	public void initeComponent () {
		storiesNumber = Integer.parseInt(frame.getControlPanel().getStroriesNumberFild().getText());
		elevator = new ElevatorView(getBounds(), storiesNumber);
		
	}
	
	public Map <Integer, StoryView> getStoriesViewContainer() {
		return storiesViewContainer;
	}

	public void setStoriesViewContainer(Map<Integer, Story> storiesContainer) {
		Set<Map.Entry<Integer, Story>> setValue = storiesContainer.entrySet();
		Iterator<Map.Entry<Integer, Story>> i = setValue.iterator();
		while (i.hasNext()) {
			Map.Entry<Integer, Story> me = i.next();
			storiesViewContainer.put(me.getKey(), new StoryView (me.getValue()));
		}
	}

	public void paintComponent (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		elevator.draw(g2, storiesNumber);
		drawStoriesContainer(g2, getBounds());
	}
	
	public void drawStoriesContainer (Graphics2D g2, Rectangle2D bounds) {
		Set<Map.Entry<Integer, StoryView>> setValue = storiesViewContainer.entrySet();
		Iterator<Map.Entry<Integer, StoryView>> i = setValue.iterator();
		double y = storiesNumber * ConstantsGUI.STORY_HEIGHT;
		
		while (i.hasNext()) {
			Map.Entry<Integer, StoryView> me = i.next();
			StoryView sv = me.getValue();
			sv.setY(y);
			sv.draw(g2, bounds);
			y -= ConstantsGUI.STORY_HEIGHT;
		}
	}
	
	public void movePassengerView (int startStoryId, int destinationStoryId) {
		StoryView sv = storiesViewContainer.get(startStoryId);
		PassengerView pv = sv.getPassengerView(destinationStoryId);
		sv.movePassengerView(pv);
		elevator.addPassengerView(pv);
	}
	
	public void dropOutPassengerView (int destinationStoryId) {
		StoryView sv = storiesViewContainer.get(destinationStoryId);
		PassengerView pv = elevator.getPassengerView(destinationStoryId);
		elevator.dropOutPassengersView(pv);
		sv.dropOutPassengerView(pv);
	}
	
	public ElevatorView getElevatorView() {
		return elevator;
	}

	public void setElevatorView(ElevatorView lift) {
		this.elevator = lift;
	}

	public IGUIElevator getFrame() {
		return frame;
	}

	public void setFrame(ElevatorFrame frame) {
		this.frame = frame;
	}

	public void setStoriesNumber(int storiesNumber) {
		this.storiesNumber = storiesNumber;
	}
	
}
