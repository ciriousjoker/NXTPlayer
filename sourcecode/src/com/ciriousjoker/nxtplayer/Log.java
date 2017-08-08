package com.ciriousjoker.nxtplayer;

import lejos.nxt.LCD;

public class Log {
	
	private static boolean showDebug = true;
	private static boolean showInfo	 = true;
	private static boolean showError = true;
	
	private static String[] updateMessage = new String[6];
	
	
	// Display debug message
	public static void d(String s) {
		if(showDebug) {
			System.out.println(s);
		}
	}
	
	// Display information
	public static void i(String s) {
		if(showInfo) {
			System.out.println(s);
		}
	}
	
	// Display debug message
	public static void e(String s) {
		if(showError) {
			System.out.println(s);
		}
	}
	
	// Display array
	public static void a(int[][] array) {
		for(int v = 0; v < array.length; v++) {
			for(int h = 0; h < array[v].length; h++) {
				d("[" + v + "][" + h + "]" + array[v][h]);
			}
		}
	}
	
	// Display updatable screen
	public static void u(int id, String s) {
		if(showInfo) {
			updateMessage[id] = s;
			LCD.clear();
			for(String line : updateMessage) {
				System.out.println(line);
			}
		}
	}
	

	// Methods to manage which messages are displayed
	public static void showDebug(boolean _showDebug) {
		showDebug = _showDebug;
	}
	
	public static void showInfo(boolean _showDebug) {
		showDebug = _showDebug;
	}
	
	public static void showError(boolean _showError) {
		showError = _showError;
	}
}