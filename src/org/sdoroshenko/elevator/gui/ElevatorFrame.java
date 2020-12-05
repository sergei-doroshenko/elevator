package org.sdoroshenko.elevator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.sdoroshenko.elevator.listeners.AnimationListener;
import org.sdoroshenko.elevator.multithreading.IController;
import org.sdoroshenko.elevator.listeners.ButtonActionListener;
import org.sdoroshenko.elevator.util.Configuration;

public class ElevatorFrame extends JFrame implements IGUIElevator {
	private static final long serialVersionUID = 4219773335116793054L;
	private ControlPanel controlPanel;
	private LogPanel logPanel;
	private ViewComponent component;
	
	public ElevatorFrame(Configuration config) {
		setSize(ConstantsGUI.FRAME_DEFAULT_WIDTH, ConstantsGUI.FRAME_DEFAULT_HEIGHT);
		setTitle(ConstantsGUI.FRAME_NAME);
		setLocation(ConstantsGUI.FRAME_LOCATION_X, ConstantsGUI.FRAME_LOCATION_Y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	
		controlPanel = new ControlPanel(this);
		controlPanel.initPanel(
				config.getStoriesNumberStr(),
				config.getElevatorCapacityStr(),
				config.getPassengersNumberStr(),
				config.getAnimationBoost()
		);

		ButtonActionListener buttonActionListener = new ButtonActionListener(this);
		controlPanel.getButton().addActionListener(buttonActionListener);
		
		add(controlPanel, BorderLayout.NORTH);
		
		JSplitPane splitMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		splitMain.setOneTouchExpandable(true);
		splitMain.setDividerSize(10);
		
		logPanel = new LogPanel();
		splitMain.setBottomComponent(logPanel);

		PropertyChangeListener listener = buttonActionListener.getListener();
		component = new ViewComponent(this, listener);
		component.initeComponent();
		component.repaint();
	    final JScrollPane scrollPane = new JScrollPane(component);
		((Component) component).setPreferredSize(new Dimension(ConstantsGUI.COMPONENT_HEIGHT, ConstantsGUI.COMPONENT_WIDTH));
	    splitMain.setTopComponent(scrollPane);
	    splitMain.setDividerLocation(300);
		getContentPane().add(splitMain, BorderLayout.CENTER);
	}
	
	@Override
	public void initGUI(IController controller) {
		controlPanel.getSlider().addChangeListener(new AnimationListener(controller));
		
		component.initeComponent();
		component.setStoriesViewContainer(controller.getStoriesContainer());
		component.repaint();
	}
	
	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	@Override
	public void elevatorViewMove(int oldStoryId, int newStoryId) {
		component.getElevatorView().move(oldStoryId, newStoryId);
	}

	@Override
	public void movePassengerView(int startStoryId, int destinationStoryId) {
		component.movePassengerView(startStoryId, destinationStoryId);
	}

	@Override
	public void dropOutPassengerView(int destinationStoryId) {
		component.dropOutPassengerView(destinationStoryId);
	}

	@Override
	public void appendLog(String message) {
		logPanel.appendMessage(message);
	}

	@Override
	public void updateChanges() {
		component.repaint();
	}

	@Override
	public void changeButtonText(String buttonText) {
		controlPanel.getButton().setText(buttonText);
	}
	
	@Override
	public Configuration getConfiguration () {
		return controlPanel.getConfiguration();
	}
}
