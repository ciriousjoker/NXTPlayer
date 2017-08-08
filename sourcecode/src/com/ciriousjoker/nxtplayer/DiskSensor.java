package com.ciriousjoker.nxtplayer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class DiskSensor extends Thread {
	SensorBar mSensorBar;
	LightSensor mSensor;
	
	private int Id;
	private int CachedValue = 0;

	private int MinValue = 0;
	private int MaxValue = 10;
	
	private boolean isPaused = true;
	private boolean isStopped = false;
	
	private boolean isCalibratedLow = false;
	private boolean isCalibratedHigh = false;
	
	private final int delay = 20;
	
	int TemporaryValue = -1;
	int consistentValues = 6;
	int VerticalThreshhold = 4;
	int horizontalThreshhold = 5;
	
	// Calibration
	private final String filenameLow = Id + "_low.cfg";
	private final String filenameHigh = Id + "_high.cfg";
	private final int calibrationThreshhold = 40;
	
	public DiskSensor(SensorBar _sensorbar, LightSensor _sensor, int _id) {
		mSensorBar = _sensorbar;
		mSensor = _sensor;
		Id = _id;
		
		Log.d("Sensor " + Id + " created");
		mSensor.setFloodlight(true);
		
		loadCalibration();
	}
	
	public void run() {
		while (!isStopped) {
			if(isPaused) {
				Delay.msDelay(delay);
				continue;
			}
			
			int CurrentValue = ToneConverter.getGreyscaleId(mSensor.readValue());
			
			if(CurrentValue > MaxValue) {
				CurrentValue = MaxValue;
			}
			if(CurrentValue < MinValue) {
				CurrentValue = MinValue;
			}
			
			if(CurrentValue != CachedValue) {
				if(addValue(mSensor.readValue())) {
					sendValue(CurrentValue);
				}	
			}
			Delay.msDelay(delay);
		}
	}
	
	private void sendValue(int CurrentValue) {
//		Log.d("[" + Id + "] Sent value: " + CurrentValue + "(" + rawValue() + ")");
		NXTPlayer.mNXTBrain.ReadValue(Id,  CurrentValue);
		CachedValue = CurrentValue;
	}
	
	private boolean addValue(int value) {
		//Log.d("Raw difference: " + (value - TemporaryValue));
		if(TemporaryValue == -1) {
			TemporaryValue = value;
		}
		
		if(Math.abs((value - TemporaryValue)) <= VerticalThreshhold) {
			consistentValues++;
			//Log.d("Consistent: " + consistentValues);
		} else {
			consistentValues = 0;
			TemporaryValue = value;
			//Log.d("Consistent values reset");
		}
		
		
		
		if(consistentValues > horizontalThreshhold) {
			consistentValues = 0;
			//Log.d("Returned true");
			return true;
		}
		return false;
	}
	
	public int currentValue() {
		return ToneConverter.getGreyscaleId(mSensor.readValue());
	}
	
	public int rawValue() {
		return mSensor.readValue();
	}
	
	public void pause() {
		isPaused = true;
	}
	
	public void resume() {
		isPaused = false;
	}
	
	public void updateCache() {
		CachedValue = currentValue();
	}
	
	public boolean calibHigh(boolean force) {
		if(!isCalibratedHigh && (Math.abs(mSensor.getLow() - mSensor.readNormalizedValue()) > calibrationThreshhold || force)) {
			Log.d("[" + Id + "] Calibrating high");
			mSensor.calibrateHigh();
			CachedValue = currentValue();
			isCalibratedHigh = true;
			saveCalibration();
			return true;
		} else {
			Log.d("[" + Id + "] Didn't calibrate high (" + Math.abs(mSensor.getHigh() - mSensor.readNormalizedValue()) + ")");
			return false;
		}
	}
	
	public boolean calibLow(boolean force) {
		if(!isCalibratedLow && (Math.abs(mSensor.getHigh() - mSensor.readNormalizedValue()) > calibrationThreshhold || force)) {
			Log.d("[" + Id + "] Calibrating low");
			mSensor.calibrateLow();
			CachedValue = currentValue();
			isCalibratedLow = true;
			saveCalibration();
			return true;
		} else {
			Log.d("[" + Id + "] Didn't calibrate low (" + Math.abs(mSensor.getLow() - mSensor.readNormalizedValue()) + ")");
			return false;
		}
	}
	
	public void saveCalibration() {
		File fileLow = new File(filenameLow);
		File fileHigh = new File(filenameHigh);
		
		
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileLow))) {
			if(!fileLow.exists()) {
				fileLow.createNewFile();
			}
			
			writer.write("" + mSensor.getLow());
			Log.d("[" + Id + "] Stored low as " + mSensor.getLow());
		} catch(IOException e) {
			Log.d("IOException while writing " + filenameLow);
		}
		
		try (OutputStreamWriter writerHigh = new OutputStreamWriter(new FileOutputStream(fileHigh))) {
			if(!fileHigh.exists()) {
				fileHigh.createNewFile();
			}
			
			writerHigh.write("" + mSensor.getHigh());
			Log.d("[" + Id + "] Stored high as " + mSensor.getHigh());
		} catch(IOException e) {
			Log.d("IOException while writing " + filenameHigh);
		}
	}
	
	public void loadCalibration() {
		File fileLow = new File(filenameLow);
		File fileHigh = new File(filenameHigh);
		
		try (DataInputStream readerLow = new DataInputStream(new FileInputStream(fileLow))) {
			if(fileLow.exists()) {
				@SuppressWarnings("deprecation")
				int newLow = Integer.parseInt(readerLow.readLine());
				mSensor.setLow(newLow);
				Log.d("Sensor " + Id + ": Set low to " + newLow);
			}
		} catch(IOException e) {
			Log.d("IOException while writing " + filenameLow);
		}
		
		try (DataInputStream readerHigh = new DataInputStream(new FileInputStream(fileHigh))) {
			if(fileLow.exists()) {
				@SuppressWarnings("deprecation")
				int newHigh = Integer.parseInt(readerHigh.readLine());
				mSensor.setHigh(newHigh);
				Log.d("Sensor " + Id + ": Set high to " + newHigh);
			}
		} catch(IOException e) {
			Log.d("IOException while writing " + filenameHigh);
		}
		
		// This sets the calibration constant
		mSensor.setLow(400);
		mSensor.setHigh(700);
	}
	
	public void setHigh(int _value) {
		if(!isCalibratedHigh) {
			mSensor.setHigh(_value);
			CachedValue = currentValue();
			isCalibratedHigh = true;
			saveCalibration();
		}
	}
	
	public void setLow(int _value) {
		if(!isCalibratedLow) {
			mSensor.setLow(_value);
			CachedValue = currentValue();
			isCalibratedLow = true;
			saveCalibration();
		}
	}
	
	public void calibrate() {
		Sound.playTone(5000, 1000);
		Delay.msDelay(1000);
		mSensor.calibrateHigh();

		Delay.msDelay(500);
		Sound.playTone(3000, 1000);
		Delay.msDelay(1000);
		mSensor.calibrateLow();
		

		Delay.msDelay(1000);
	}
}