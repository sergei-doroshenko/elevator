package org.sdroshenko.elevator.listeners;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.sdroshenko.elevator.ifaces.IController;

public class AnimationListener implements ChangeListener {
	
	private IController controller;
	
	public AnimationListener(IController controller) {
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
