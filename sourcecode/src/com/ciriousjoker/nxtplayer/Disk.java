package com.ciriousjoker.nxtplayer;

import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

public class Disk implements RegulatedMotorListener{
	// Objects
	NXTBrain mNXTPlayer;
	NXTRegulatedMotor mDiskMotor;
	
	// Store motor state
	int InitialAngle = 0;
	boolean isPaused = false;
	
	// Overlapping degree
	private final int overlappingDegree = 8;
	
	// Motor gearing ratio
	private final int gearRatio = 5;
	private final int FullRotationDegrees = gearRatio * 360;
	
	public Disk(NXTBrain _player, NXTRegulatedMotor _motor) {
		mNXTPlayer = _player;
		mDiskMotor = _motor;
		mDiskMotor.setSpeed(25);
	}
	
	// Do one full rotation or finish pending rotation
	public void rotate() {
		if(!mDiskMotor.isMoving()) {
			int CalculatedCurrentPosition = mDiskMotor.getPosition();
			while(CalculatedCurrentPosition >= FullRotationDegrees) {
				CalculatedCurrentPosition -= FullRotationDegrees;
			}
			
			mDiskMotor.rotate(-1 * (-FullRotationDegrees + CalculatedCurrentPosition), true);
			isPaused = false;
			mDiskMotor.addListener(this);
		}
	}
	
	// Pause if rotating
	public void pause() {
		if(mDiskMotor.isMoving()) {
			mDiskMotor.stop(true);
			isPaused = true;
		}
	}
	
	// Reset to its initial position
	public void reset() {
		// pause the program until reset is complete
		mDiskMotor.rotateTo(InitialAngle);
	}
	
	// Uses current position as initial position
	public void setInitialPosition() {
		InitialAngle = mDiskMotor.getPosition();
	}
	
	// Return states
	public boolean isRotating() {
		return mDiskMotor.isMoving();
	}
	
	public boolean isOverlapping() {
		return ((FullRotationDegrees - mDiskMotor.getPosition()) < overlappingDegree);
	}
	
	@Override
	public void rotationStopped(RegulatedMotor motor, int tachoCount, boolean stalled, long timeStamp) {
		int currentPosition = mDiskMotor.getPosition();
		while(currentPosition >= 360) {
			currentPosition -= 360;
		}
		
		// Notify the NXT that a rotation is complete to move the sensorbar
		if(currentPosition == InitialAngle && !isPaused) {
			NXTPlayer.mNXTBrain.OnRotationComplete();
		}
	}
	
	@Override
	public void rotationStarted(RegulatedMotor motor, int tachoCount, boolean stalled, long timeStamp) {
		
	}
}
