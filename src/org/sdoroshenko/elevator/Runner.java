package org.sdoroshenko.elevator;

import java.awt.EventQueue;

import org.sdoroshenko.elevator.gui.ElevatorFrame;
import org.sdoroshenko.elevator.multithreading.Controller;
import org.sdoroshenko.elevator.multithreading.IController;
import org.sdoroshenko.elevator.listeners.ControllerChangeListenerConsole;
import org.sdoroshenko.elevator.util.Configuration;
import org.sdoroshenko.elevator.util.Validator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Application launcher.
 *
 * @author Sergei Doroshenko
 */
public class Runner {

	private static final Logger logger = LogManager.getLogger(Runner.class);

	public static void main (String[] args) {
		final Configuration config = new Configuration("config");
		Validator.validate(config);

		logger.info("Configuration[stories={}, elevator capacity={}, passengers={}, animation={}]",
				config.getStoriesNumberStr(),
				config.getElevatorCapacityStr(),
				config.getPassengersNumberStr(),
				config.getAnimationBoost());

		// if config.getAnimationBoost() gt 0, runs app with graphic UI
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
