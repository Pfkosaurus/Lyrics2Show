package com.matus.lyrics;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONException;

import java.awt.Toolkit;
import java.awt.Dimension;

public class Gui extends LyricsSelection{

private static RestCall rest = new RestCall();
	
public static void guiSetArtist() {
    JFrame frame = new JFrame("Select artist");
    frame.setSize(300, 150);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JTextField inputField = new JTextField();
    inputField.setBounds(10, 10, 280, 30);
    JButton saveButton = new JButton("Save");
    saveButton.setBounds(100, 50, 100, 30);

    saveButton.addActionListener(e -> {
        String artist = inputField.getText();
        artist = artist.replace(" ", "%20");
        setArtist(artist);
        frame.setVisible(false);
        guiSetSong();
    });

   
    frame.add(inputField);
    frame.add(saveButton);    
    frame.setLayout(null);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;
    int dialogWidth = frame.getWidth();
    int dialogHeight = frame.getHeight();
    int dialogX = (screenWidth - dialogWidth) / 2;
    int dialogY = (screenHeight - dialogHeight) / 2;

    frame.setLocation(dialogX, dialogY);
    frame.setVisible(true);
}
	
	public static void guiSetSong() {
		JFrame frame = new JFrame("Select song");
	    frame.setSize(300, 150);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JTextField inputField = new JTextField();
	    inputField.setBounds(10, 10, 280, 30);
	    JButton saveButton = new JButton("Run");
	    saveButton.setBounds(100, 50, 100, 30);

	    saveButton.addActionListener(e -> {
	        String song = inputField.getText();
	        song = song.replace(" ", "%20");
	        setSong(song);
	        JOptionPane.showMessageDialog(null, "I am about to find song " + song + " from artis" + artist);
	        frame.setVisible(false);
	        try {
				rest.fetchLyrics();
			} catch (IOException | JSONException e1) {
				e1.printStackTrace();
			}
	    });

	   
	    frame.add(inputField);
	    frame.add(saveButton);    
	    frame.setLayout(null);

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	    int dialogWidth = frame.getWidth();
	    int dialogHeight = frame.getHeight();
	    int dialogX = (screenWidth - dialogWidth) / 2;
	    int dialogY = (screenHeight - dialogHeight) / 2;

	    frame.setLocation(dialogX, dialogY);
	    frame.setVisible(true);
                
    }
}

