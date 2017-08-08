package com.ciriousjoker.nxtplayer;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import lejos.nxt.NXTRegulatedMotor;

public class SensorBar {
	// Objects
	NXTRegulatedMotor mMotor;
	DiskSensor mUpperSensor;
	DiskSensor mLowerSensor;
	
	// Rotation angle to move the bar to the next reading line
	final int MotorToBarRatio = 1300;
	final String PositionFilename = "sensorbar_position.cfg";
	
	// Cached sensor / motor values
	private int[][] MetaStorage = new int[][] {{-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}};
	private int[][] ToneStorage = new int[][] {{-1, -1}, {-1, -1}, {-1, -1}};
	private int[] DataCache = new int [] {-1, -1};
	private int MotorPosition = 0;
	
	private final int MaxTracks = 3;
	private int CurrentTrack = 0;
	
	public SensorBar(NXTRegulatedMotor _motor, DiskSensor _UpperSensor, DiskSensor _LowerSensor) {
		mMotor = _motor;
		mUpperSensor = _UpperSensor;
		mLowerSensor = _LowerSensor;
		loadMotorPosition();

		moveStart();
		resetDataCache();
	}
	
	public void moveTo(int index) {
		if(index <= MaxTracks) {
			int rotationAngle = (index * MotorToBarRatio) - mMotor.getPosition();
			savePosition(rotationAngle);
			
			mLowerSensor.pause();
			mUpperSensor.pause();
			
			mMotor.rotate(rotationAngle, true);

			mLowerSensor.resume();
			mUpperSensor.resume();
		}
	}
	
	public boolean moveNext() {
		if(CurrentTrack < (MaxTracks - 1)) {
			mLowerSensor.pause();
			mUpperSensor.pause();
			
			savePosition(MotorToBarRatio);
			mMotor.rotate(MotorToBarRatio, true);
			
			mLowerSensor.resume();
			mUpperSensor.resume();
			CurrentTrack++;
			return true;
		}
		return false;
	}
	
	public void moveStart() {
		int rotationAngle = -MotorPosition;
		savePosition(rotationAngle);
		mMotor.rotate(rotationAngle, true);
		CurrentTrack = 0;
		Log.d("Rotating " + (rotationAngle) + " degrees");
	}
	
	private void savePosition(int change) {
		File file = new File(PositionFilename);
		
		//Log.d("Saving position after change: " + change);
		
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
			if(!file.exists()) {
				file.createNewFile();
				Log.d("File created");
			}
			
			// Save new position
			MotorPosition += change;
			
			//Log.d("Current position: " + MotorPosition);
			writer.write("" + MotorPosition);
			Log.d("Stored sensorbar position as: " + MotorPosition);
		} catch(IOException e) {
			Log.d("IOException while writing " + PositionFilename);
		}
	}
	
	private void loadMotorPosition() {
		File file = new File(PositionFilename);
		
		try (DataInputStream reader = new DataInputStream(new FileInputStream(file))) {
			if(file.exists()) {
				@SuppressWarnings("deprecation")
				int newPosition = Integer.parseInt(reader.readLine());
				
				MotorPosition = newPosition;
				Log.d("Loaded last motor position: " + MotorPosition);
			}
		} catch(IOException e) {
			Log.d("IOException while reading " + PositionFilename);
		}
	}
	
	public void calibrate() {
		// Calibrate upper sensor
		NXTPlayer.mNXTBrain.mDiskSensorUpper.calibrate();
		NXTPlayer.mNXTBrain.mDiskSensorLower.calibrate();
	}
	
	public void calibrate(Tone _tone) {
		// Calibrate upper sensor
		mUpperSensor.setHigh(_tone.getRawData()[0][0]);
		mUpperSensor.setLow(_tone.getRawData()[0][1]);
		
		// Calibrate lower sensor
		mLowerSensor.setLow(_tone.getRawData()[1][0]);
		mLowerSensor.setHigh(_tone.getRawData()[1][1]);
	}
	
	private void resetDataCache() {
		ToneStorage = new int[][] {{-1, -1}, {-1, -1}, {-1, -1}};
	}
	
	// Called by NXTBrain once a new block of data was read AND it's currently in the stage of reading music data
	public Tone ReturnCompleteTone(int id, int value) {		
//		Log.d("Current value: " + mUpperSensor.rawValue() + ", that's " + mUpperSensor.currentValue());
		
		Log.d("[0] " + mUpperSensor.currentValue() + " = " + mUpperSensor.currentValue() + "(" + mUpperSensor.rawValue() + ")");
		Log.d("[1] " + mLowerSensor.currentValue() + " = " + mLowerSensor.currentValue() + "(" + mLowerSensor.rawValue() + ")\n");
		
		// Generate a value pair for the current data
		int[] ValuePair = new int[2];
		
		if(id == 0) {
			ValuePair[0] = value;
			ValuePair[1] = mLowerSensor.currentValue();
		} else {
			ValuePair[0] = mUpperSensor.currentValue();
			ValuePair[1] = value;
		}
		
		// Check if the value pair is different from the cache object
		if(DataCache[0] == ValuePair[0] && DataCache[1] == ValuePair[1]) {
			// Value is already cached
			return null;
		}
		
		// Overwrite the cache object with the new values
		DataCache = ValuePair;
		//Log.d("NewValue/Cache(" + ValuePair[0] + "/" + ValuePair[1] + ")");
		
		// Add the value pair to the data storage
		if(ToneStorage[0][0] == -1 && ToneStorage[0][1] == -1) {
			// Added first column
			// x o o
			// x o o
			
			ToneStorage[0][0] = ValuePair[0];
			ToneStorage[0][1] = ValuePair[1];
		} else if(ToneStorage[1][0] == -1 && ToneStorage[1][1] == -1) {
			// Added second column
			// x x o
			// x x o
			
			ToneStorage[1][0] = ValuePair[0];
			ToneStorage[1][1] = ValuePair[1];
		} else if(ToneStorage[2][0] == -1 && ToneStorage[2][0] == -1) {
			// Added third column
			// x x x
			// x x x
			
			ToneStorage[2][0] = ValuePair[0];
			ToneStorage[2][1] = ValuePair[1];
			
			if(ValuePair[0] != 8) {
				Log.d("Error. The last block of data does not contain a calibration field. Instead its value is: " + ValuePair[0]);
			}
		}
		
		//Log.d("Current data: " + ToneStorage[0][0] + "   /   " + ToneStorage[0][1]);
		//Log.d("                       " + ToneStorage[1][0] + "   /   " + ToneStorage[1][1]);
		
		// If tone finished (either Valpair = 8 or tonestorage[2][0]), return the tone and reset the cache
		if(ValuePair[0] == 8 || ToneStorage[2][0] != -1) {
			// Build a new tone out of the data storage
			Tone ret = ToneConverter.getTone(ToneStorage);
			
			// Reset the data storage
			resetDataCache();
			
			// Return the new tone
			return ret;
		}
		return null;		
	}
	
	// Called by NXTBrain once a new block of data was read AND it's currently in the stage of reading meta data
	public Metadata ReturnCompleteMetadata(int id, int value) {		
//		Log.d("[" + id + "] Current meta value: " + mUpperSensor.rawValue() + ", that's " + mUpperSensor.currentValue());
		
		Log.d("[0] " + mUpperSensor.currentValue() + " = " + mUpperSensor.currentValue() + "(" + mUpperSensor.rawValue() + ")");
		Log.d("[1] " + mLowerSensor.currentValue() + " = " + mLowerSensor.currentValue() + "(" + mLowerSensor.rawValue() + ")\n");
		
		// Generate a value pair for the current data
		int[] ValuePair = new int[2];
		
		if(id == 0) {
			ValuePair[0] = value;
			ValuePair[1] = mLowerSensor.currentValue();
		} else {
			ValuePair[0] = mUpperSensor.currentValue();
			ValuePair[1] = value;
		}
		
		// Check if the value pair is different from the cache object
		if(DataCache[0] == ValuePair[0] && DataCache[1] == ValuePair[1]) {
			// Value is already cached
			return null;
		}

		// Overwrite the cache object with the new values
		DataCache = ValuePair;
		
		// Add the value pair to the data storage
		if(MetaStorage[0][0] == -1 && MetaStorage[0][1] == -1) {
			// Added first column
			// x o o o o o
			// x o o o o o

			Log.d("Calibration #1");
			if(NXTPlayer.mNXTBrain.mDiskSensorLower.calibLow(true)) {
				MetaStorage[0][1] = mLowerSensor.currentValue();
			}
			
			if(NXTPlayer.mNXTBrain.mDiskSensorUpper.calibHigh(true)) {
				MetaStorage[0][0] = mUpperSensor.currentValue();
			}
		} else if(MetaStorage[1][0] == -1 && MetaStorage[1][1] == -1) {
			// Added first column
			// x x o o o o
			// x x o o o o

			Log.d("Calibration #2");
			
			if(NXTPlayer.mNXTBrain.mDiskSensorLower.calibHigh(false)) {
				MetaStorage[1][1] = mLowerSensor.currentValue();
			}
			
			if(NXTPlayer.mNXTBrain.mDiskSensorUpper.calibLow(false)) {
				MetaStorage[1][0] = mUpperSensor.currentValue();
			}
		} else if(MetaStorage[2][0] == -1 && MetaStorage[2][1] == -1) {
			// Added third column
			// x x x o o o
			// x x x o o o

			MetaStorage[2][0] = ValuePair[0];
			MetaStorage[2][1] = ValuePair[1];
		} else if(MetaStorage[3][0] == -1 && MetaStorage[3][1] == -1) {
			// Added fourth column
			// x x x x o o
			// x x x x o o

			MetaStorage[3][0] = ValuePair[0];
			MetaStorage[3][1] = ValuePair[1];
		} else if(MetaStorage[4][0] == -1 && MetaStorage[4][1] == -1) {
			// Added fifth column
			// x x x x x o
			// x x x x x o

			MetaStorage[4][0] = ValuePair[0];
			MetaStorage[4][1] = ValuePair[1];
		}  else if(MetaStorage[5][0] == -1 && MetaStorage[5][1] == -1) {
			// Added fifth column
			// x x x x x x
			// x x x x x x

			MetaStorage[5][0] = ValuePair[0];
			MetaStorage[5][1] = ValuePair[1];
		}
		
		if(MetaStorage[5][0] != -1) {
			// Build the meta data out of the data storage
			Metadata ret = new Metadata().setRawData(MetaStorage);
			
			// Return the meta data
			return ret;
		}
		return null;		
	}
}
