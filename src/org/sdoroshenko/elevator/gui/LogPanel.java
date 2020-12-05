package org.sdoroshenko.elevator.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Caret;

public class LogPanel extends JPanel {

	private static final long serialVersionUID = -4096262570497501768L;
	private final JTextArea logArea = new JTextArea(ConstantsGUI.LOG_AREA_ROWS, ConstantsGUI.LOG_AREA_COLUMNS);
	private final JScrollPane scroll = new JScrollPane(logArea);
	
	public LogPanel() {
		add(scroll);
	}	

	public JTextArea getLogArea() {
		return logArea;
	}
	
	public void appendMessage(String message) {
		logArea.append(message + "\n");
		
		Caret caret = logArea.getCaret();
		caret.setDot(logArea.getDocument().getLength());
		logArea.repaint();
	}
}
