package org.sdroshenko.elevator.multithreading;

import org.sdroshenko.elevator.model.Passenger;
import org.sdroshenko.elevator.model.Story;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class TransportationTask extends Thread {

    private static final Logger log = LogManager.getLogger(TransportationTask.class);

    private final Passenger passenger;
    private final Story startStory;
    private final Story destinationStory;
    private final Controller controller;

    public TransportationTask(final ThreadGroup group, final Passenger passenger, final Controller controller) {
        super(group, ("TransTaskThread-" + passenger.getID()));
        this.passenger = passenger;
        this.startStory = passenger.getStartStory();
        this.destinationStory = passenger.getDestinationStory();
        this.controller = controller;
    }

    public void run() {

        // Waiting for the elevator phase
        do {

            synchronized (startStory) {
                while (!startStory.isElevator()) {
                    try {
                        startStory.wait();
                    } catch (InterruptedException e) {
                        log.error(Thread.currentThread().getName() + " interrupt!");
                        passenger.setTransportationState(Passenger.TransportationState.ABORTED);
                        break;
                    }
                }

                if (controller.pickUp(passenger)) {
                    passenger.setTransportationState(Passenger.TransportationState.IN_PROGRESS);
                    controller.setLoadFlag(controller.isPassengersWayIn());
                    startStory.notifyAll();

                    synchronized (controller) {
                        controller.notifyAll();
                    }
                }
            }

        } while (passenger.getTransportationState() == Passenger.TransportationState.NOT_STARTED);

        // Transportation in the elevator phase
        synchronized (controller) {
            while (!destinationStory.isElevator()) {
                try {
                    controller.wait();
                } catch (InterruptedException e) {
                    log.error(Thread.currentThread().getName() + " interrupt!");
                    break;
                }
            }
            if (controller.dropOut(passenger)) {
                passenger.setTransportationState(Passenger.TransportationState.COMPLETED);
                controller.setMoveFlag(controller.isPassengersWayOut());//True if is, False if isn't
            }
            controller.notifyAll();
        }

    } // The END of journey
}
