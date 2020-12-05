package org.sdoroshenko.elevator.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.sdoroshenko.elevator.util.Configuration;

import java.awt.*;

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 4923625938638831224L;
	private final JTextField storiesNumberField = new JTextField(7);
	private final JTextField elevatorCapacityFild = new JTextField(7);
	private final JTextField passengersNumberField = new JTextField(7);
	private final JSlider slider = new JSlider(0, 10, 1);
	private final JButton button = new JButton(ConstantsGUI.START_BUTTON_TEXT);
	
	public ControlPanel(ElevatorFrame frame) {
		setLayout(new GridBagLayout());
		Insets insets = new Insets(5, 3, 2, 3);
		
/**		About GridBagConstraints
		GridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, 
				int anchor, int fill, Insets insets, int ipadx, int ipady)
		Insets(int top, int left, int bottom, int right) */
		
		add(new JLabel("storiesNumber"), new GridBagConstraints(0, 0, 1, 1, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 10, 0));
		
		add(storiesNumberField, new GridBagConstraints(1, 0, 1, 1, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 10, 0));
		
		add(new JLabel("elevatorCapacity"), new GridBagConstraints(2, 0, 1, 1, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 10, 0));
		
		add(elevatorCapacityFild, new GridBagConstraints(3, 0, 1, 1, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 10, 0));
		
		add(new JLabel("passengersNumber"), new GridBagConstraints(4, 0, 1, 1, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 10, 0));
		
		add(passengersNumberField, new GridBagConstraints(5, 0, 1, 1, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 10, 0));
		
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
	
		add(slider, new GridBagConstraints(6, 0, 2, 2, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 10, 0));
		button.setMinimumSize(new Dimension(100, 40));
		add(button, new GridBagConstraints(8, 0, 2, 1, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 10, 0));
	}

	public JTextField getStroriesNumberFild() {
		return storiesNumberField;
	}
	
	public JTextField getElevatorCapacityFild() {
		return elevatorCapacityFild;
	}
	
	public JTextField getPassengersNumberFild() {
		return passengersNumberField;
	}

	public JSlider getSlider() {
		return slider;
	}

	public JButton getButton() {
		return button;
	}

	public int getAnimationBoost() {
		return slider.getValue();
	}

	public void setAnimationBoost(int value) {
		slider.setValue(value);
	}
	
	public void initPanel (String storiesNumber, String elevatorCapacity, String passengersNumber, 
			int animationBoost) {
		
		storiesNumberField.setText(storiesNumber);
		elevatorCapacityFild.setText(elevatorCapacity);
		passengersNumberField.setText(passengersNumber);
		setAnimationBoost(animationBoost);
	}
	
	public Configuration getConfiguration () {
			
		return new Configuration (storiesNumberField.getText(), elevatorCapacityFild.getText(),
				passengersNumberField.getText(), getAnimationBoost());
	}
}
