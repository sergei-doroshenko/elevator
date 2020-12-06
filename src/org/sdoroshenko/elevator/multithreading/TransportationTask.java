package org.sdoroshenko.elevator.multithreading;

import org.sdoroshenko.elevator.model.Passenger;
import org.sdoroshenko.elevator.model.Story;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Passenger transportation task.
 *
 * @author Sergei Doroshenko
 */
public class TransportationTask extends Thread {

    private static final Logger log = LogManager.getLogger(TransportationTask.class);

    private final Passenger passenger;
    private final Story startStory;
    private final Story destinationStory;
    private final Dispatcher dispatcher;

    public TransportationTask(final ThreadGroup group, final Passenger passenger, final Dispatcher dispatcher) {
        super(group, ("TransTaskThread-" + passenger.getID()));
        this.passenger = passenger;
        this.startStory = passenger.getStartStory();
        this.destinationStory = passenger.getDestinationStory();
        this.dispatcher = dispatcher;
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

                if (dispatcher.pickUp(passenger)) {
                    passenger.setTransportationState(Passenger.TransportationState.IN_PROGRESS);
                    dispatcher.setLoadFlag(dispatcher.isPassengersWayIn());
                    startStory.notifyAll();

                    synchronized (dispatcher) {
                        dispatcher.notifyAll();
                    }
                }
            }

        } while (passenger.getTransportationState() == Passenger.TransportationState.NOT_STARTED);

        // Transportation in the elevator phase
        synchronized (dispatcher) {
            while (!destinationStory.isElevator()) {
                try {
                    dispatcher.wait();
                } catch (InterruptedException e) {
                    log.error(Thread.currentThread().getName() + " interrupt!");
                    break;
                }
            }
            if (dispatcher.dropOut(passenger)) {
                passenger.setTransportationState(Passenger.TransportationState.COMPLETED);
                dispatcher.setMoveFlag(dispatcher.isPassengersWayOut());//True if is, False if isn't
            }
            dispatcher.notifyAll();
        }

    } // The END of journey
}
