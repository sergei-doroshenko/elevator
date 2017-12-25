package com.epam.javatraining.elevator.ifaces;

import java.util.Map;
import com.epam.javatraining.elevator.model.Story;

public interface IController {
	
	void execute();
	void cancel ();
	
	String validateResults ();

	void setAnimationBoost(int value);
	
	Map<Integer, Story> getStoriesContainer();

}
