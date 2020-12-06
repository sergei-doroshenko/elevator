package org.sdoroshenko.elevator.multithreading;

import org.sdoroshenko.elevator.model.Passenger;
import org.sdoroshenko.elevator.model.Story;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatcherInitializer {

    private int previousStartId;

    /**
     * Init Stories
     */
    public Map<Integer, Story> createStories(int storiesNumber) {
        Map<Integer, Story> stories = new HashMap<>();

        for (int i = 0; i < storiesNumber; i++) {
            Story s = new Story();
            stories.put(s.getId(), s);
        }

        Story.setNextID(0);
        return stories;
    }

    /**
     * Create passengers and put them into Stories
     */
    public void createTransportationTasks(final Dispatcher dispatcher) {

        Map<Integer, Story> stories = dispatcher.getStoriesContainer();
        ThreadGroup tg = dispatcher.gettGroup();
        int storiesNumber = dispatcher.getStoriesNumber();
        List<Thread> thr = new ArrayList<>();

        for (int i = 0; i < dispatcher.getPassengersNumber(); i++) {


            int startId = generatePassengerStartStoryId(storiesNumber);

            Story startStory = stories.get(startId);

            int destinationId = generatePassengerDestinationStoryId(startStory, storiesNumber);
            Story destinationStory = stories.get(destinationId);

            Passenger passenger = new Passenger(startStory, destinationStory);

            Thread t = new TransportationTask(tg, passenger, dispatcher);
            thr.add(t);
        }

        Passenger.setNextID(0);
        dispatcher.setTransportationTasks(thr);
    }

    private int generatePassengerStartStoryId(int storiesNumber) {
        int startId = (int) (Math.random() * storiesNumber);
        if (startId == previousStartId) {
            return generatePassengerStartStoryId(storiesNumber);
        }
        previousStartId = startId;
        return startId;
    }

    private int generatePassengerDestinationStoryId(Story startStory, int storiesNumber) {
        int startId = startStory.getId();
        int destinationId = (int) (Math.random() * storiesNumber);

        if (destinationId == startId) {
            return generatePassengerDestinationStoryId(startStory, storiesNumber);
        }
        return destinationId;
    }
}
