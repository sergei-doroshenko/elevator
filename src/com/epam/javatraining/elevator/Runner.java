package com.epam.javatraining.elevator;

import java.awt.EventQueue;

import com.epam.javatraining.elevator.gui.ElevatorFrame;
import com.epam.javatraining.elevator.ifaces.IController;
import com.epam.javatraining.elevator.listeners.ControllerChangeListenerConsole;
import com.epam.javatraining.elevator.model.Controller;
import com.epam.javatraining.elevator.util.Configuration;
import com.epam.javatraining.elevator.util.Validator;


public class Runner {
	
	public static void main (String[] args) {
		
		final Configuration config = new Configuration("config");
		Validator.validate(config);
		
		System.out.println(config.getStoriesNumberStr() + " " + config.getElevatorCapacityStr()
				+ " " + config.getPassengersNumberStr() + " " + config.getAnimationBoost());
		
		
		if (config.getAnimationBoost() > 0) {
			
			EventQueue.invokeLater(new Runnable() {
				public void run () {
					
					new ElevatorFrame(config);
					
				}	
			});	
			
		} else {
			
			IController controller =  new Controller(config, new ControllerChangeListenerConsole());
			controller.execute();
			
		}	
	}
}
