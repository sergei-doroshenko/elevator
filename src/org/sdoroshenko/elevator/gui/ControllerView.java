package org.sdoroshenko.elevator.gui;

public interface ControllerView {
    void fireExecutionOver();

    void fireExecutionStarted();

    void fireFloorChange(int oldStoryID, int currentStoryID);
}
