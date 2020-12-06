package org.sdoroshenko.elevator.multithreading;

import org.sdoroshenko.elevator.gui.ControllerView;
import org.sdoroshenko.elevator.gui.SwingControllerView;
import org.sdoroshenko.elevator.model.Story;
import org.sdoroshenko.elevator.util.Configuration;

import java.util.Map;

public class DispatcherTest {

    public static void main(String[] args) {
        Configuration config = new Configuration(Integer.toString(10), Integer.toString(10), Integer.toString(100), 0);
        ControllerView controllerView = new SwingControllerView(evt -> {});

        DispatcherInitializer dispatcherInitializer = new DispatcherInitializer();
        Map<Integer, Story> stories = dispatcherInitializer.createStories(config.getStoriesNumberInt());
        Dispatcher dispatcher = new Dispatcher(config, controllerView, stories);
        dispatcherInitializer.createTransportationTasks(dispatcher);

        dispatcher.execute();
    }

}
