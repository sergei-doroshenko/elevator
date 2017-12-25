package org.sdoroshenko.elevator.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import org.sdoroshenko.elevator.ifaces.IController;
import org.sdoroshenko.elevator.util.Configuration;
import org.sdoroshenko.elevator.util.Validator;

public class Controller extends Thread implements IController {
	private static Logger log = Logger.getLogger(Controller.class);
    private int previosStartId;
	private int storiesNumber;
	private int passengersNumber;
	private int elevatorCapacity;
	private long animationBoost;
    private List<Passenger> elevatorContainer = new ArrayList<Passenger> ();
    private Map<Integer, Story> storiesContainer;
    private ThreadGroup tGroup;
    private List<Thread> transportationTasksPool;
    private Story currentStory;
	private int lastStoryID;
	private boolean moveUp;
	private boolean moveFlag;
	private boolean loadFlag;
	private PropertyChangeSupport contrSupport = new PropertyChangeSupport(this);
	private PropertyChangeListener listener;
	
	public Controller(Configuration config, PropertyChangeListener listener) {
		
		Validator.validate(config);
		
		this.storiesNumber = config.getStoriesNumberInt();
		this.elevatorCapacity = config.getElevatorCapacityInt();
		this.passengersNumber = config.getPassengersNumberInt();
		this.animationBoost = config.getAnimationBoost();
		this.listener = listener;
		
		lastStoryID = storiesNumber - 1;
		
		storiesContainer = createStories(storiesNumber);
		
		tGroup = new ThreadGroup("Group");
		transportationTasksPool = createTransportaitionTasks(passengersNumber, storiesNumber, tGroup);
		
		/**Init ControllerChangeListener */
		contrSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void run() {
		contrSupport.firePropertyChange("started", false, true);

		for (Thread t : transportationTasksPool) {
			t.start();
		}
		
		currentStory = storiesContainer.get(0);
		currentStory.setElevator(true);
		moveUp = true;
		
		while (tGroup.activeCount() > 0) {        	

        	while (isPassengersWayOut()) {
	        	synchronized (this) {
	        		while (isMoveFlag()) {//Is false at the begining.
	        			try {
							wait();
						} catch (InterruptedException e) {
							System.out.println(Thread.currentThread().getName() + " interrupt!");
							break;
						}
	        		}
	        		notifyAll();
	        	}
        	}
        	
        	synchronized (currentStory) {
        		currentStory.notifyAll();
        	}

        	synchronized (this){	        	
        		while (isPassengersWayIn()) {//isLoadFlag()
        			try {
        				this.wait();
        			} catch (InterruptedException e) {
        				System.out.println(Thread.currentThread().getName() + " interrupt!");
        				break;
        			}
        		}
        			if ( !isPassengersWayOut() && !isPassengersWayIn()) {//
        				moveElevator();
        				useAnimationBoost();
        				
        			}
        		}
       }
///////////////////////////////////////////////////////////        
		
		String valMessage = validateResults();
		log.info(valMessage);
		System.out.println(valMessage);
		
		contrSupport.firePropertyChange("started", true, false);
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
	public void moveElevator () {
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
		
		contrSupport.firePropertyChange("currentStore", oldStoryID, currentStoryID);
	}
	
	public synchronized boolean add (Passenger passenger) {
		Story startStory = passenger.getStartStory();
		
		if(startStory.equals(currentStory) && elevatorContainer.size() != elevatorCapacity) {
			if ( !(startStory.getId() == 0 || (startStory.getId() == storiesContainer.size() - 1)) ) {
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
	
	public synchronized boolean dropOut (Passenger passenger) {
		if(passenger.getDestinationStory().equals(currentStory)) {
			elevatorContainer.remove(passenger);
			currentStory.getArrivalStoryContainer().add(passenger);
			return true;
		}
		
		return false;
	}
	
	public synchronized boolean isPassengersWayIn () {
		if ( elevatorContainer.size() == elevatorCapacity || !currentStory.isCompanion(moveUp))  {
    		return false;	
    	}
		return true;
	}
	
	public synchronized boolean isPassengersWayOut () {
		for (Passenger p : elevatorContainer) {
			if(p.getDestinationStory().equals(currentStory)){
				return true;
			};
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
	
	public boolean isLoadFlag() {
		return loadFlag;
	}

	public void setLoadFlag(boolean loadFlag) {
		this.loadFlag = loadFlag;
	}
	
	public PropertyChangeListener getListener() {
		return listener;
	}

	public void setListener(PropertyChangeListener listener) {
		this.listener = listener;
	}
	
	public long getAnimationBoost() {
		return animationBoost;
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

	public void setAnimationBoost(long animationBoost) {
		this.animationBoost = animationBoost;
	}

	public ThreadGroup gettGroup() {
		return tGroup;
	}

	public void settGroup(ThreadGroup tGroup) {
		this.tGroup = tGroup;
	}

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        contrSupport.removePropertyChangeListener(listener);
    }
    
	public List<Passenger> getElevatorContainer() {
		return elevatorContainer;
	}

	public void setElevatorContainer(List<Passenger> elevatorContainer) {
		this.elevatorContainer = elevatorContainer;
	}
	
	public Map<Integer, Story> getStoriesContainer() {
		return storiesContainer;
	}

	public void setStoriesContainer(Map<Integer, Story> storiesContainer) {
		this.storiesContainer = storiesContainer;
	}

	/////Util methods----------------------------------------------------------
	/** Init Stories */
	public Map<Integer, Story> createStories(int storiesNumber) {
		Map<Integer, Story> stories = new HashMap<Integer, Story>();
		for (int i = 0; i < storiesNumber; i++) {
			Story s = new Story();
			stories.put(s.getId(), s);
		}
		Story.setNextID(0);
		
		return stories;
	}
	/**Create passengers and put them into Stories */
	public List<Thread> createTransportaitionTasks(int passengersNumber, int storiesNumber, ThreadGroup tg) {
		
		
		List<Thread> thr = new ArrayList<Thread>();
		
		for (int i = 0; i < passengersNumber; i++) {
			Passenger passenger = new Passenger();
			
			int startId = generatePassengerStartStoryId (storiesNumber);
			Story startStory = storiesContainer.get(startId);
			passenger.setStartStory(startStory);
			
			int destinationId = generatePassengerDestinationStoryId (passenger);
			Story destinationStory = storiesContainer.get(destinationId);
			
			passenger.setDestinationStory(destinationStory);		
			startStory.add(passenger);
			
			passenger.addPropertyChangeListener(listener);
			Thread t = new TransportationTask(tg, passenger, this);
			thr.add(t);
		}
		
		Passenger.setNextID(0);
		return thr;
	}
	
	public int generatePassengerStartStoryId (int storiesNumber) {
		int startId = (int) (Math.random() * storiesNumber);
		if (startId == previosStartId) {
			return generatePassengerStartStoryId(storiesNumber);
		}
		previosStartId = startId;
		return startId;	
	}
		
	public int generatePassengerDestinationStoryId (Passenger passenger) {
		int startId = passenger.getStartStory().getId();
		int destinationId = (int) (Math.random() * storiesNumber);
			
		if (destinationId == startId) {
			return generatePassengerDestinationStoryId(passenger);
		}
		return destinationId;
	}
	
	public void useAnimationBoost () {
		try {
			for (int i = 0; i < 20; i++) {
				Thread.sleep(10 - animationBoost);
			}	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}
