package com.ciriousjoker.nxtplayer;

import java.util.List;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class Interpreter extends Thread {
	// Objects
	Track mTrack;
	DiskSensor mDiskSensorUpper, mDiskSensorLower;
	
	// Track current position
	int PositionIndicator = 0;
	boolean isLooping = false;
	boolean isFinished = false;
	boolean isPaused = false;
	boolean isRead = false;
	int pauseDelay = 200;
	
	// Constants
	final int CheckBufferFrequency = 1000;
	final int[] Instrument = Sound.PIANO;
	
	public Interpreter(Track _track, DiskSensor _uppersensor, DiskSensor _lowersensor) {
		mTrack = _track;
		mDiskSensorUpper = _uppersensor;
		mDiskSensorLower = _lowersensor;
		PositionIndicator = 0;
	}
	
	public void run() {
		Log.d("Interpreter started");
		while(!mTrack.isBuffered()) {
			Delay.msDelay(CheckBufferFrequency);
		}
		
		interpretMusicdata();
	}
	
	private void interpretMusicdata() {
		Log.d("Reading music data");
		
		while(true) {
			isFinished = false;
			PositionIndicator = 0;
			mTrack.clearLoops();
			
			Log.d("Starting the loop");
			while(!isFinished) {
				if(PositionIndicator <= mTrack.getMusicData().size() - 1 || isPaused) {
					Log.d((PositionIndicator + 1) + " / " + mTrack.getMusicData().size());
					Tone CurrentTone = mTrack.returnTone(PositionIndicator);
					
					PositionIndicator++;
					
					Tone modifiedTone = interpretModifier(CurrentTone);
					
					if(CurrentTone.getAltModifier() != -1) {
						modifiedTone = interpretAltModifier(modifiedTone);
					}
					
					if(!modifiedTone.isSkipped()) {
						// Interpret as a normal tone
						interpretTone(CurrentTone);
					}
				} else {
					Delay.msDelay(pauseDelay);
				}
			}
			isRead = true;
			NXTPlayer.mNXTBrain.mSensorBar.moveStart();
			Log.d("Track finished, attempting to play it again");
		}
	}
	
	public boolean isRead() {
		return isRead;
	}
	
	public void pause() {
		isPaused = true;
	}
	
	public void resume() {
		isPaused = false;
	}
	
	private void interpretTone(Tone _tone) {
		Tone ActualTone = new Tone();
		
		// The octave value is relative to the metaOctave while octave=3 means metaOctave +- 0
		int actual_octave = mTrack.getMetaData().getOctave() + (_tone.getOctave() - 3);
		ActualTone.setFrequency(ToneConverter.convertFrequency(_tone.getFrequency(), actual_octave));
		
		// Calculate duration based on note type and BPM
		ActualTone.setDuration(ToneConverter.convertDuration(_tone.getDuration(), mTrack.getMetaData().getBpm()));
		
		// Calculate delay based on delay type and BPM
		ActualTone.setDelay(ToneConverter.convertDuration(_tone.getDelay(), mTrack.getMetaData().getBpm()));
		
		// Set repetition if applicable
		ActualTone.setRepetition(_tone.getRepetition());
		
		// Play the tone x number of times
		for(int i = 0; i < ActualTone.getRepetition(); i++) {
			playTone(ActualTone);
		}
	}

	private Tone interpretModifier(Tone _tone) {
		Log.d("modifier: " + _tone.getModifier());
		
		switch(_tone.getModifier()) {
		case 0:
			// Don't do anything here, just skip the tone if it's a calibration field like
			// b w
			// b w
		case 1:
			// Play without modification
			return _tone;
		case 2:
			// Set loop marker at the current position.
			// Note that the id is stored in the frequency and the duration field.
			// These fields can range from 0 to 8, therefore the stored number has to have 9 as its base
			int loopId = 9 * Math.round(_tone.getFrequency()) + _tone.getDuration();
			
			if(mTrack.hasLoop(loopId)) {
				Log.d("Playing loop # " + loopId);
				interpretLoop(mTrack.getLoop(loopId));
			} else {
				Log.d("Setting loop marker " + loopId);
				mTrack.setLoopMarker(loopId);
			}
			return _tone.skip();
		case 3:
			return _tone.setRepetition(2);
		case 4:
			return _tone.setRepetition(3);
		case 5:
			return _tone.setRepetition(4);
		case 6:
			return _tone.setRepetition(5);
		case 7:
			return _tone.halfUp();
		// Ideas
		//  - Add cases for instruments
		//  - Add cases for attack/decay/reverb
		//  - Add cases for repetition
		//  - Add cases BPM multiplication
		//  - Add cases for some delays
		//  - Add cases for two octaves
		//  - Add case for same octave color as tone (So the upper row has three different colors 100%)
		//  - Added cases for half tone steps
		case 8:
			// End of rotation / calibration black
			// CD fully played once, now start looping / return
			isFinished = true;
			return _tone;
		default:
			return _tone.skip();
		}
	}
	
	private Tone interpretAltModifier(Tone _tone) {
		Log.d("Alt modifier: " + _tone.getAltModifier());
		
		switch(_tone.getAltModifier()) {
		case 0:
			// Unused
		case 1:
			// Just play the tone
			return _tone;
		case 2:
			return _tone.setRepetition(2);
		case 3:
			return _tone.setRepetition(3);
		case 4:
			return _tone.setRepetition(4);
		case 5:
			return _tone.setRepetition(5);
		case 6:
			return _tone.setRepetition(6);
			// Ideas:
			//  - Add cases for directly playing a loop a second time
			//  - Add cases for repetition
			//  - Add cases for half tones
			//  - Add cases for attack/reverb/decay
		case 7:
			return _tone.setRepetition(7);
		case 8:
			// Just return the tone. This field will be interpreted by interpretAltModifier() later
			return _tone;
		default:
			return _tone.skip();
		}
	}
	
	private void interpretLoop(List<Tone> _Loop) {
		for(int i = 0; i < _Loop.size(); i++) {
			Tone CurrentTone = _Loop.get(i);
			
			Tone modifiedTone = interpretModifier(CurrentTone);
			if(!modifiedTone.isSkipped()) {
				// Interpret as tone
				interpretTone(modifiedTone);
			}
		}
	}
	
	private void playTone(Tone tone) {
		while(isPaused) {
			Delay.msDelay(pauseDelay);
		}
		Log.d(tone.getFrequency() + "hz for " + tone.getDuration() + "ms");
		
		// Play the tone while doing nothing else
		Sound.playNote(Instrument, Math.round(tone.getFrequency()), tone.getDuration());
		
		// Add optional delay after the tone
		Delay.msDelay(tone.getDelay());
	}
}
