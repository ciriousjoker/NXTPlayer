package com.ciriousjoker.nxtplayer;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.TouchFeatureDetector;

public class NXTBrain {
    
	FeatureListener mFeatureListener;
	
	Disk mDisk;
	SensorBar mSensorBar;
	Interpreter mInterpreter;
	Track mTrack = new Track();
	
	// Sensors
	TouchSensor mSensorButton1 = new TouchSensor(SensorPort.S1);
	TouchSensor mSensorButton2 = new TouchSensor(SensorPort.S2);
	LightSensor mSensorLightUpper = new LightSensor(SensorPort.S3);
	LightSensor mSensorLightLower = new LightSensor(SensorPort.S4);

	// Motors
	NXTRegulatedMotor mDiskMotor = new NXTRegulatedMotor(MotorPort.C);
	NXTRegulatedMotor mSensorBarMotor = new NXTRegulatedMotor(MotorPort.B);
	
	// Buttons
	TouchFeatureDetector mButton1; // Play / pause button
	TouchFeatureDetector mButton2; // Manual calibration
	
	// Sensor threads
	DiskSensor mDiskSensorUpper;
	DiskSensor mDiskSensorLower;
	
	// [2c49g5] Activate Background thread here
	boolean backgroundThreadStarted = false;
	
	// Sensor timeout
	private long CacheTimer = 0;
	private static final int CacheDelay = 300;
	
	BackgroundThread mBackgroundThread = new BackgroundThread();
	
	public NXTBrain() {
		InitializePorts();
		InitializeModules();
	}
	
	// Executed when button #1 is clicked
	public void Button1_Clicked(Feature feature, FeatureDetector detector){
		if(mDisk.isRotating()) {
			mDiskSensorLower.pause();
			mDiskSensorUpper.pause();
			mDisk.pause();
			mInterpreter.pause();

			// [2c49g5] Activate Background thread here
//			mBackgroundThread.pause();
		} else {
			mDisk.rotate();
			mDiskSensorLower.resume();
			mDiskSensorUpper.resume();
			mInterpreter.resume();
			/*
			// [2c49g5] Activate Background thread here
			mBackgroundThread.resume();
			
			if(!backgroundThreadStarted) {
				mBackgroundThread.start();
				backgroundThreadStarted = true;
			}
			*/
		}
	}
	
	// Shut down when the second button is clicked
	public void Button2_Clicked(Feature feature, FeatureDetector detector){
		System.exit(0);
	}
	
	private void InitializePorts() {
		// Initialize FeatureDetectors
		mButton1 = new TouchFeatureDetector(mSensorButton1);
		mButton2 = new TouchFeatureDetector(mSensorButton2);
		
		// Add sensor listeners
		mButton1.addListener(new FeatureListener() {
			@Override
			public void featureDetected(Feature feature, FeatureDetector detector) {
				Button1_Clicked(feature, detector);
			}
		});
		
		mButton2.addListener(new FeatureListener() {
			@Override
			public void featureDetected(Feature feature, FeatureDetector detector) {
				Button2_Clicked(feature, detector);
			}
		});
	}
	
	private void InitializeModules() {
		mDisk = new Disk(this, mDiskMotor);
		mDiskSensorUpper = new DiskSensor(mSensorBar, mSensorLightUpper, 0);
		mDiskSensorLower = new DiskSensor(mSensorBar, mSensorLightLower, 1);
		mSensorBar = new SensorBar(mSensorBarMotor, mDiskSensorUpper, mDiskSensorLower);
		mInterpreter = new Interpreter(mTrack, mDiskSensorUpper, mDiskSensorLower);
		
		// If you want to use the background thread for debugging, insert
//		mBackgroundThread.start();
		// here. Also, search for "[2c49g5]" to see where else to activate it.
		
		mInterpreter.start();	
	}
	
	// Callback from Interpreter to indicate that one rotation is complete
	public void OnRotationComplete() {
		Log.d("Rotation completed");
		if(!mInterpreter.isRead()) {
			if(mSensorBar.moveNext()) {
				Log.d("Starting to rotate the disk");
				new Thread() {
					public void run() {
						try {
							Thread.sleep(4000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						// Note:
						// This has to be done on a second thread as I can't call Motor.rotate(x, true) and Thread.sleep() on the same thread.
						// Also, Motor.rotate(x, false); won't work for some reason.
						mDisk.rotate();
					}
				}.start();
			} else {
				Log.i("Can't move the sensor bar any further, resetting to its original position");
				mSensorBar.moveStart();
			}
		}
	}
	
	// Callback from SensorBar to indicate that a new block of data was read
	public void ReadValue(int id, int value) {
		// Account for multiple value submission due to two sensors sending their values independently
		if(System.currentTimeMillis() - CacheTimer < CacheDelay) {
			return;
		}
		CacheTimer = System.currentTimeMillis();
		
		if(mDisk.isOverlapping()) {
			Log.d("Disk is overlapping");
			return;
		}
		
		
		if(!mTrack.isMetaRead()) {
			Metadata _meta = mSensorBar.ReturnCompleteMetadata(id, value);
			
			if(_meta == null) {
				return;
			}
			
			mTrack.setMetadata(_meta);
		} else {
			Tone _tone = mSensorBar.ReturnCompleteTone(id, value);

			if(_tone == null) {
				return;
			}
			
			mTrack.addTone(_tone);
		}
	}
}
