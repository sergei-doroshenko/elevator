package org.sdoroshenko.elevator.multithreading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.sdoroshenko.elevator.gui.ControllerView;
import org.sdoroshenko.elevator.model.Passenger;
import org.sdoroshenko.elevator.model.Story;
import org.sdoroshenko.elevator.util.Configuration;
import org.sdoroshenko.elevator.util.Validator;

/**
 * Elevator controller.
 *
 * @author Sergei Doroshenko
 */
public class Controller extends Thread implements IController {

    private static final Logger log = LogManager.getLogger(Controller.class);

    private int previousStartId;
    private int storiesNumber;
    private int passengersNumber;
    private final int elevatorCapacity;
    private long animationBoost;
    private final Set<Passenger> elevatorContainer = new HashSet<>();
    private final Map<Integer, Story> storiesContainer;
    private final ThreadGroup tGroup;
    private final List<Thread> transportationTasksPool;
    private Story currentStory;
    private int lastStoryID;
    private boolean moveUp;
    private boolean moveFlag;
    private boolean loadFlag;
    private ControllerView controllerView;

    public Controller(Configuration config) {
        log.info("=====> STARTING CONTROLLER <=====");
        Validator.validate(config);

        this.storiesNumber = config.getStoriesNumberInt();
        this.elevatorCapacity = config.getElevatorCapacityInt();
        this.passengersNumber = config.getPassengersNumberInt();
        this.animationBoost = config.getAnimationBoost();

        this.lastStoryID = storiesNumber - 1;

        this.storiesContainer = createStories(storiesNumber);

        this.tGroup = new ThreadGroup("Group");
        this.transportationTasksPool = createTransportationTasks(passengersNumber, storiesNumber, tGroup);
    }

    @Override
    public void run() {
        controllerView.fireExecutionStarted();

        for (Thread t : transportationTasksPool) {
            t.start();
        }

        currentStory = storiesContainer.get(0);
        currentStory.setElevator(true);
        moveUp = true;

        while (tGroup.activeCount() > 0) {

            while (isPassengersWayOut()) {
                synchronized (this) {
                    while (isMoveFlag()) { //Is false at the beginning.
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            log.error(Thread.currentThread().getName() + " interrupt!");
                            break;
                        }
                    }
                    notifyAll();
                }
            }

            synchronized (currentStory) {
                currentStory.notifyAll();
            }

            synchronized (this) {
                while (isPassengersWayIn()) { //isLoadFlag()
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        log.error(Thread.currentThread().getName() + " interrupt!");
                        break;
                    }
                }

                if (!isPassengersWayOut() && !isPassengersWayIn()) {
                    moveElevator();
                    useAnimationBoost();
                }
            }
        }

        String valMessage = validateResults();
        log.info(valMessage);
        controllerView.fireExecutionOver();
    }

    @Override
    public void execute() {
        start();
    }

    @Override
    public void cancel() {
        while (gettGroup().activeCount() > 0) {
            gettGroup().interrupt();
        }
        interrupt();
    }

    @Override
    public String validateResults() {
        int despCon = 0;
        int arrCon = 0;
        Set<Map.Entry<Integer, Story>> setvalue = storiesContainer.entrySet();
        Iterator<Map.Entry<Integer, Story>> i = setvalue.iterator();
        while (i.hasNext()) {
            Map.Entry<Integer, Story> me = i.next();
            Story s = me.getValue();
            despCon += s.getDispatchStoryContainer().size();
            arrCon += s.getArrivalStoryContainer().size();
        }
        return "despatchStoryContainers=" + despCon + " arrivalStoryContainers=" + arrCon
                + " elevatorContainer=" + elevatorContainer.size()
                + " passengersNumber=" + passengersNumber;
    }

    ////Control methods ----------------------------------------------------------

    public void moveElevator() {
        int currentStoryID = currentStory.getId();
        int oldStoryID = currentStoryID;

        currentStory.setElevator(false);

        if (moveUp) {
            currentStoryID++;
        } else {
            currentStoryID--;
        }

        if (currentStoryID == lastStoryID) {
            moveUp = false;
        } else if (currentStoryID == 0) {
            moveUp = true;
        }

        currentStory = storiesContainer.get(currentStoryID);
        currentStory.setElevator(true);

        controllerView.fireFloorChange(oldStoryID, currentStoryID);
    }

    public synchronized boolean pickUp(Passenger passenger) {
        Story startStory = passenger.getStartStory();

        if (startStory.equals(currentStory) && elevatorContainer.size() != elevatorCapacity) {
            if (!(startStory.getId() == 0 || (startStory.getId() == storiesContainer.size() - 1))) {
                if (moveUp != passenger.isMoveUp()) {
                    return false;
                }
            }
            currentStory.getDispatchStoryContainer().remove(passenger);
            elevatorContainer.add(passenger);
            return true;
        }
        return false;
    }

    public synchronized boolean dropOut(Passenger passenger) {
        if (passenger.getDestinationStory().equals(currentStory)) {
            elevatorContainer.remove(passenger);
            currentStory.getArrivalStoryContainer().add(passenger);
            return true;
        }

        return false;
    }

    public synchronized boolean isPassengersWayIn() {
		return elevatorContainer.size() != elevatorCapacity && currentStory.isCompanion(moveUp);
	}

    public synchronized boolean isPassengersWayOut() {
        for (Passenger p : elevatorContainer) {
            if (p.getDestinationStory().equals(currentStory)) {
                return true;
            }
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isMoveFlag() {
        return moveFlag;
    }

    public void setMoveFlag(boolean moveFlag) {
        this.moveFlag = moveFlag;
    }

    public void setLoadFlag(boolean loadFlag) {
        this.loadFlag = loadFlag;
    }

    public void setAnimationBoost(int animationBoost) {
        this.animationBoost = animationBoost;
    }

    public int getPassengersNumber() {
        return passengersNumber;
    }

    public void setPassengersNumber(int passengersNumber) {
        this.passengersNumber = passengersNumber;
    }

    public ThreadGroup gettGroup() {
        return tGroup;
    }

    public Set<Passenger> getElevatorContainer() {
        return elevatorContainer;
    }

    public Map<Integer, Story> getStoriesContainer() {
        return storiesContainer;
    }

    public void setControllerView(ControllerView controllerView) {
        this.controllerView = controllerView;
    }

    /////Util methods----------------------------------------------------------

    /**
     * Init Stories
     */
    private Map<Integer, Story> createStories(int storiesNumber) {
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
    private List<Thread> createTransportationTasks(int passengersNumber, int storiesNumber, ThreadGroup tg) {

        List<Thread> thr = new ArrayList<>();

        for (int i = 0; i < passengersNumber; i++) {

            int startId = generatePassengerStartStoryId(storiesNumber);
            Story startStory = storiesContainer.get(startId);

            int destinationId = generatePassengerDestinationStoryId(startStory);
            Story destinationStory = storiesContainer.get(destinationId);

            Passenger passenger = new Passenger(startStory, destinationStory);

            Thread t = new TransportationTask(tg, passenger, this);
            thr.add(t);
        }

        Passenger.setNextID(0);
        return thr;
    }

    private int generatePassengerStartStoryId(int storiesNumber) {
        int startId = (int) (Math.random() * storiesNumber);
        if (startId == previousStartId) {
            return generatePassengerStartStoryId(storiesNumber);
        }
        previousStartId = startId;
        return startId;
    }

    private int generatePassengerDestinationStoryId(Story startStory) {
        int startId = startStory.getId();
        int destinationId = (int) (Math.random() * storiesNumber);

        if (destinationId == startId) {
            return generatePassengerDestinationStoryId(startStory);
        }
        return destinationId;
    }

    private void useAnimationBoost() {
        try {
            for (int i = 0; i < 20; i++) {
                Thread.sleep(10 - animationBoost);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
