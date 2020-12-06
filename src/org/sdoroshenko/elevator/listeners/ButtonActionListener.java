package org.sdoroshenko.elevator.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

import org.sdoroshenko.elevator.gui.ConstantsGUI;
import org.sdoroshenko.elevator.gui.IGUIElevator;
import org.sdoroshenko.elevator.multithreading.Controller;
import org.sdoroshenko.elevator.gui.LogConsole;

public class ButtonActionListener implements ActionListener {

	private final IGUIElevator gui;
	private final Controller controller;
	
	public ButtonActionListener (final IGUIElevator gui, final Controller controller) {
		this.gui = gui;
		this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		JButton button = (JButton) event.getSource();
		String command = event.getActionCommand();
		this.gui.initGUI(controller);

		if (command.equals(ConstantsGUI.START_BUTTON_TEXT)) {
			button.setText(ConstantsGUI.ABORT_BUTTON_TEXT);
			button.getParent().repaint();
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
