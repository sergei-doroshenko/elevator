package org.sdoroshenko.elevator.listeners;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.sdoroshenko.elevator.multithreading.Controller;

/**
 * Swing GUI animation listener.
 *
 * @author Sergei Doroshenko
 */
public class AnimationListener implements ChangeListener {
	
	private final Controller controller;
	
	public AnimationListener(final Controller controller) {
		this.controller = controller;
	}

	@Override
	public void stateChanged(ChangeEvent ev) {
		
		JSlider slider = (JSlider) ev.getSource(); 
		int value = slider.getValue();
		
		if (controller != null) {
			synchronized (controller) {
				controller.setAnimationBoost(value);
			}
		}
		
	}
}
