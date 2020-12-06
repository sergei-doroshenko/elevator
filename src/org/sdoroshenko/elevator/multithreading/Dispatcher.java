package org.sdoroshenko.elevator.multithreading;

import java.util.HashMap;
import java.util.HashSet;
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
public class Dispatcher extends Thread implements Controller {

    private static final Logger log = LogManager.getLogger(Dispatcher.class);

    private int storiesNumber;
    private int passengersNumber;
    private int elevatorCapacity;
    private long animationBoost;

    private final Set<Passenger> elevatorContainer = new HashSet<>();
    private final Map<Integer, Story> storiesContainer;
    private final ThreadGroup tGroup;

    private List<Thread> transportationTasks;

    private Story currentStory;
    private int lastStoryID;
    private boolean moveUp;
    private boolean moveFlag;
    private boolean loadFlag;

    private final ControllerView controllerView;

    public Dispatcher(
            final Configuration config,
            final ControllerView controllerView,
            final Map<Integer, Story> stories) {
        super("DispatcherThread-1");
        log.info("=====> STARTING CONTROLLER <=====");
        Validator.validate(config);

        this.storiesNumber = config.getStoriesNumberInt();
        this.elevatorCapacity = config.getElevatorCapacityInt();
        this.passengersNumber = config.getPassengersNumberInt();
        this.animationBoost = config.getAnimationBoost();

        this.lastStoryID = storiesNumber - 1;
        this.storiesContainer = stories;

        this.tGroup = new ThreadGroup("Group");

        this.controllerView = controllerView;
    }

    @Override
    public void run() {
        controllerView.fireExecutionStarted();

        for (Thread t : transportationTasks) {
            t.start();
        }

        currentStory = storiesContainer.get(0);
        currentStory.setElevator(true);
        moveUp = true;
        log.info(this::getTransportationState);

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

        Map<String, Integer> transportationState = getTransportationState();
        log.info(transportationState);
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
    public Map<String, Integer> getTransportationState() {

        int[] storiesContainers = storiesContainer.values().stream()
                .reduce(new int[]{0, 0}, (a, b) -> {
                    a[0] += b.getDispatchStoryContainer().size();
                    a[1] += b.getArrivalStoryContainer().size();
                    return a;
                }, (a,b) -> new int[0]);

        Map<String, Integer> result = new HashMap<>();
        result.put("Active threads", tGroup.activeCount());
        result.put("dispatchStoryContainers", storiesContainers[0]);
        result.put("arrivalStoryContainers", storiesContainers[1]);
        result.put("elevatorContainer", elevatorContainer.size());
        result.put("passengersNumber", passengersNumber);

        return result;
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

    public int getStoriesNumber() {
        return storiesNumber;
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

    public void setTransportationTasks(List<Thread> transportationTasks) {
        this.transportationTasks = transportationTasks;
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
