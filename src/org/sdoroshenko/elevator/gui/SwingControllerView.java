package org.sdoroshenko.elevator.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SwingControllerView implements ControllerView {

    private final PropertyChangeSupport contrSupport = new PropertyChangeSupport(this);
    private final PropertyChangeListener listener;

    public SwingControllerView(final PropertyChangeListener listener) {
        this.listener = listener;
        this.contrSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void fireExecutionOver() {
        contrSupport.firePropertyChange("started", true, false);
    }

    @Override
    public void fireExecutionStarted() {
        contrSupport.firePropertyChange("started", false, true);
    }

    @Override
    public void fireFloorChange(int oldStoryID, int currentStoryID) {
        contrSupport.firePropertyChange("currentStore", oldStoryID, currentStoryID);
    }
}
