package com.epam.javatraining.elevator.gui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Caret;

public class LogConsole extends JFrame {
	private static final long serialVersionUID = 1L;
    BufferedReader br;
    JTextArea textArea = new JTextArea();

    public LogConsole() throws IOException {
        
    	br = new BufferedReader(new FileReader(ConstantsGUI.LOGGING_FILE_NAME));
    
        textArea.setEditable(false);
        textArea.setRows(10);
        textArea.setColumns(68);
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        pack();
        setVisible(true);

        // Create reader threads
        new ReaderThread(br).start();
    }

    class ReaderThread extends Thread {
    	BufferedReader br;
    	
        ReaderThread(BufferedReader br) {
            this.br = br;
        }

        public void run() {
            String line = null;
            try {
            	while ((line = br.readLine()) != null) {
                    textArea.append(line);
                    textArea.append("\n");
                    
                }
            	Caret caret = textArea.getCaret();
            	caret.setDot(textArea.getDocument().getLength());
            } catch (IOException e) {
            }
        }
    }
}