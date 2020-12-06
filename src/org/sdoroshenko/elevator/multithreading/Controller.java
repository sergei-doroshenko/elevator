package org.sdoroshenko.elevator.multithreading;

import java.util.Map;
import org.sdoroshenko.elevator.model.Story;

/**
 * Transportation dispatcher controller.
 *
 * @author Sergei Doroshenko
 */
public interface Controller {
	
	void execute();

	void cancel ();

	Map<String, Integer> getTransportationState();

	void setAnimationBoost(int value);
	
	Map<Integer, Story> getStoriesContainer();

}
