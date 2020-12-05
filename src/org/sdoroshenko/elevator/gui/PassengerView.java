package org.sdoroshenko.elevator.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PassengerView {
	private Rectangle2D rect;
	private final int destinationStoryId;
	private Font font = new Font("Dialog", Font.PLAIN, 9);
	private int rule = AlphaComposite.SRC_OVER;
	private float alpha = 0.9f;
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	
	public PassengerView(final int destinationStoryId, final PropertyChangeListener listener) {
		this.destinationStoryId = destinationStoryId;
		this.changeSupport.addPropertyChangeListener(listener);
	}

	public int getDestinationStoryId() {
		return destinationStoryId;
	}

	public void draw (Graphics2D g2, double x, double y) {
		rect = new Rectangle2D.Double(x, y, ConstantsGUI.PASSENGER_HEIGHT, ConstantsGUI.PASSENGER_WIDTH);
		g2.draw(rect);
		g2.setPaint(Color.RED);
		g2.fill(rect);
		g2.setComposite(AlphaComposite.getInstance(rule, alpha));
		g2.setFont(font);
		g2.setPaint(Color.BLACK);
		g2.drawString(destinationStoryId + "", 
				(int) rect.getMinX() + ConstantsGUI.PASSENGER_VIEW_OFFCET, 
				(int) rect.getMaxY() - ConstantsGUI.PASSENGER_VIEW_OFFCET);
	}
}
