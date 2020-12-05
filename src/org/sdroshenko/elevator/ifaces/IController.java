package org.sdroshenko.elevator.ifaces;

import java.util.Map;
import org.sdroshenko.elevator.model.Story;

public interface IController {
	
	void execute();
	void cancel ();
	
	String validateResults ();

	void setAnimationBoost(int value);
	
	Map<Integer, Story> getStoriesContainer();

}
