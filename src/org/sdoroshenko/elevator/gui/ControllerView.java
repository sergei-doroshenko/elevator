package org.sdoroshenko.elevator.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ControllerView {

    private final PropertyChangeSupport contrSupport = new PropertyChangeSupport(this);
    private final PropertyChangeListener listener;

    public ControllerView(final PropertyChangeListener listener) {
        this.listener = listener;
        this.contrSupport.addPropertyChangeListener(listener);
    }

    public void fireExecutionOver() {
        contrSupport.firePropertyChange("started", true, false);
    }

    public void fireExecutionStarted() {
        contrSupport.firePropertyChange("started", false, true);
    }

    public void fireFloorChange(int oldStoryID, int currentStoryID) {
        contrSupport.firePropertyChange("currentStore", oldStoryID, currentStoryID);
    }
}
