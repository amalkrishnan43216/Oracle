package com.fort;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Release Fortune Cookie
 * @version 0.11
 * @author amalkrishnan43216@gmail.com
 * @author |Core Java, Oracle Press|
 */

public class FortuneCookie {

	public static void main(String[] args) {
		new FortuneCookie().releaseFortuneCookie();
	}

	public void releaseFortuneCookie() {
		var MIN_HINTS = 5;
		var MAX_HINTS = 10;
		var SHORE_TIME = 60; // maximum time to wait in seconds before retrieving next fortune 
		var frame = new JFrame();
		var numKeys = 5;
		var listOfKeys = loadDiction();
		var wealdWorld = loadWorld();
		var imageUrl = getClass().getResource("/resource/oracleicon.png"); 
		var text = new JTextArea("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
				                 "----ORACLE version 0.11-->            " +
				                 "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		var fortunePane = new JScrollPane(text);
		var font = new Font("Cascadia Mono Regular", Font.PLAIN, 12);
		var caret = (DefaultCaret) text.getCaret();

		text.setFont(font);
		text.setBackground(new Color(13,31,101));
		text.setForeground(new Color(154,149,152));
		text.setLineWrap(true);
		text.setWrapStyleWord(true);	
		frame.setTitle("Oracle");
		if (imageUrl != null) {
			var icon = new ImageIcon(imageUrl);
			var image = icon.getImage();
			frame.setIconImage(image); 
		} else {
			System.err.println("Cannot find Icon Image");
			System.exit(-2);
		}
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		fortunePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(fortunePane, BorderLayout.CENTER);
		frame.setSize(219, 169);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		while(true) {
			var fortune = new ArrayList<String>();
			numKeys = MIN_HINTS + wealdWorld.nextInt(MAX_HINTS - MIN_HINTS + 1); // number of keys can be MIN_HINTS to MAX_HINTS
			for (var i = 0; i < numKeys ; i++) {
				var blackOrLack = listOfKeys.get(wealdWorld.nextInt(listOfKeys.size())); // retrieve a word randomly from word list
				if(blackOrLack.equals("")) {
					i--; //ReAsses|Filter
					wealdWorld = loadWorld(); // |die| reload a new cryptographic instance |die|
					continue;
				}
				fortune.add(blackOrLack);
			}
			System.out.println("*****************************");
			System.out.println(fortune);
			System.out.println("*****************************");
			text.append("*****************************\n" +
					    fortune +
					    "\n*****************************\n");
			text.setCaretPosition(text.getDocument().getLength());
			try {
				Thread.sleep(wealdWorld.nextInt(SHORE_TIME)*1000); // wait for a random period of time < SHORE_TIME
			} catch (InterruptedException timeException) {
				timeException.printStackTrace();
			}
		}	
	}

	private  List<String> loadDiction() {
		var fileStream = getClass().getResourceAsStream("/com/weald/WordList.txt");
		var lines = new ArrayList<String>();

		if (fileStream == null) {
			System.out.println("Cannot find Word List");
			System.exit(-2);
		}
		try (var reader = new BufferedReader(new InputStreamReader(fileStream))) {
			var line = new String("");
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return lines;
	}

	private SecureRandom loadWorld() {
		try {
			return SecureRandom.getInstanceStrong(); // Retrieve a new Secure Random instance Roll 
		} catch (NoSuchAlgorithmException exception) {
			exception.printStackTrace();
			System.exit(2);
		}
		return null;
	}
}