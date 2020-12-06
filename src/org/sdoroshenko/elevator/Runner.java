package org.sdoroshenko.elevator;

import java.awt.EventQueue;
import java.beans.PropertyChangeListener;

import org.sdoroshenko.elevator.gui.SwingControllerView;
import org.sdoroshenko.elevator.gui.ElevatorFrame;
import org.sdoroshenko.elevator.gui.StoryView;
import org.sdoroshenko.elevator.multithreading.Controller;
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
			EventQueue.invokeLater(() -> new ElevatorFrame(config));
		} else {
			PropertyChangeListener propertyChangeListener = new ControllerChangeListenerConsole();
			SwingControllerView swingControllerView = new SwingControllerView(propertyChangeListener);
			Controller controller =  new Controller(config, swingControllerView);
			controller.getStoriesContainer().forEach((key, value) -> new StoryView(value, propertyChangeListener));

			controller.execute();
		}	
	}
}
