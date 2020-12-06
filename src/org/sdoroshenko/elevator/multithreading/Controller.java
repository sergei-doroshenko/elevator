package org.sdoroshenko.elevator.multithreading;

import java.util.Map;
import org.sdoroshenko.elevator.model.Story;

public interface Controller {
	
	void execute();
	void cancel ();
	
	String validateResults ();

	void setAnimationBoost(int value);
	
	Map<Integer, Story> getStoriesContainer();

}
