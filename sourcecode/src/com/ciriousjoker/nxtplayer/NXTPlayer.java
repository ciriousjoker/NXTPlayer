package com.ciriousjoker.nxtplayer;

import lejos.robotics.objectdetection.*;

public class NXTPlayer {
	
	FeatureListener mFeatureListener;
	
	static NXTBrain mNXTBrain;
	
	// Entry point for the program
	public static void main(String[] args) {
		Log.showDebug(true);
		mNXTBrain = new NXTBrain();
		System.out.println("Loaded ...");
	}
	
	public static NXTBrain getInstance() {
		return mNXTBrain;
	}
}