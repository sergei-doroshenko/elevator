package com.epam.javatraining.elevator.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

import com.epam.javatraining.elevator.gui.ConstantsGUI;
import com.epam.javatraining.elevator.gui.LogConsole;
import com.epam.javatraining.elevator.ifaces.IController;
import com.epam.javatraining.elevator.ifaces.IGUIElevator;
import com.epam.javatraining.elevator.model.Controller;

public class ButtonActionListener implements ActionListener {
	
	private IGUIElevator gui;
	private IController controller;
	
	public ButtonActionListener (IGUIElevator gui) {
		this.gui = gui;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		JButton button = (JButton) event.getSource();
		
		String command = event.getActionCommand();
		
		if (command.equals(ConstantsGUI.START_BUTTON_TEXT)) {
			button.setText(ConstantsGUI.ABORT_BUTTON_TEXT);
			button.getParent().repaint();
			
			controller = new Controller(gui.getConfiguration(), new ControllerChangeListenerGUI(gui));
			
			gui.initGUI(controller);
			
			controller.execute();
			
		} else if (command.equals(ConstantsGUI.ABORT_BUTTON_TEXT)) {
			
			button.setText(ConstantsGUI.START_BUTTON_TEXT);
			controller.cancel();
			
		} else {
			
			button.setText(ConstantsGUI.START_BUTTON_TEXT);
			try {
				new LogConsole();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}		
	}
}