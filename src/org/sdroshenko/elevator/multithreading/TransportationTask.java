package org.sdroshenko.elevator.multithreading;

import org.sdroshenko.elevator.model.Passenger;
import org.sdroshenko.elevator.model.Story;

public class TransportationTask extends Thread {
    private Passenger passenger;
    private Story startStory;
    private Story destinationStory;
    private Controller controller;

    public TransportationTask(Passenger passenger) {
        super();
        this.passenger = passenger;
    }

    public TransportationTask(ThreadGroup group, Passenger passenger, Controller controller) {
        super(group, ("TransTaskThread-" + passenger.getID()));
        this.passenger = passenger;
        this.startStory = passenger.getStartStory();
        this.destinationStory = passenger.getDestinationStory();
        this.controller = controller;
    }

    public void run() {
        do {
            synchronized (startStory) {
                while (!startStory.isElevator()) {
                    try {
                        startStory.wait();
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + " interrupt!");
                        passenger.setTransportationState(Passenger.TransportationState.ABORTED);
                        break;
                    }
                }

                if (controller.add(passenger)) {
                    passenger.setTransportationState(Passenger.TransportationState.IN_PROGRESS);
                    controller.setLoadFlag(controller.isPassengersWayIn());
                    startStory.notifyAll();
                } else {
                    try {
                        startStory.wait();
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + " interrupt!");
                        passenger.setTransportationState(Passenger.TransportationState.ABORTED);
                        break;
                    }
                }

                synchronized (controller) {
                    controller.notifyAll();
                }

            }
        } while (passenger.getTransportationState() == Passenger.TransportationState.NOT_STARTED);

        synchronized (controller) {
            controller.notifyAll();
        }


        synchronized (controller) {
            while (destinationStory.isElevator() == false) {
                try {
                    controller.wait();
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupt!");
                    break;
                }
            }
            if (controller.dropOut(passenger)) {
                passenger.setTransportationState(Passenger.TransportationState.COMPLETED);
                controller.setMoveFlag(controller.isPassengersWayOut());//True if is, False if isn't
            }
            controller.notifyAll();
        }


    }//end of run()
}