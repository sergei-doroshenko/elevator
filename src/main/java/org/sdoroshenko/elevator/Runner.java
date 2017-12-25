package org.sdoroshenko.elevator;

import java.awt.EventQueue;

import org.sdoroshenko.elevator.gui.ElevatorFrame;
import org.sdoroshenko.elevator.ifaces.IController;
import org.sdoroshenko.elevator.listeners.ControllerChangeListenerConsole;
import org.sdoroshenko.elevator.model.Controller;
import org.sdoroshenko.elevator.util.Configuration;
import org.sdoroshenko.elevator.util.Validator;


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
