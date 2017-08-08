package com.ciriousjoker.nxtplayer;

import lejos.util.Delay;

public class BackgroundThread extends Thread {
	
	
	private final int delayBetweenTones = 7000;
	private final int delayPaused = 200;
	private boolean paused = false;
	
	BackgroundThread() {
		
	}

	public void run() {
		/*
		This background thread is to be used for debugging and to see if it works.
		It simulates sensor input. Below you'll see what values are valid when calling
		Tone();
		*/

		// Frequency
		// 7 - H
		// 6 - B
		// 5 - A
		// 4 - G
		// 3 - F
		// 2 - E
		// 1 - D
		// 0 - C
		
		// Duration:
		// 0 - 500 ms
		// 1 - Full
		// 2 - Half
		// 3 - Quarter
		// 4 - Eighth
		// 5 - Sixteenth
		// 6 - Dotted half
		// 7 - Dotted quarter
		// 8 - Dotted eighth
		
		// Modifiers:
		// 0 - Calibration field
		// 1 - Default
		// 2 - Loop marker / play loop
		// 3 - Repeat 2 times
		// 4 - Repeat 3 times
		// 5 - Repeat 4 times
		// 6 - Skip
		// 7 - Half tone up
		// 8 - End of track
		
		
		// A D A C G F G F G F
		// new Tone(modifier,frequency,duration,octave,delay)
		

		// ### Happy Birthday ### //
		int sixteenth = 5;			// Sixteenth
		int dottedeighth = 8;	// dotted Eighth
		int quarter = 4;		// Eighth
		int dottedquarter = 7;	// dotted quarter
		int dottedhalf = 6;		// dotted half
		
		int baseoctave = 3;
		int higheroctave = 4;
		delay();
		delay();
		NXTPlayer.getInstance().mTrack.setMetadata(new Metadata(0, 2, 2, 0, 0, 5, 2, 7));
		delay();
		
		//NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0f, 1));								// > loop1
		//delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 0, quarter, baseoctave, 5));			// 1 c
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 0, sixteenth, baseoctave, 0));			// 1 c
		delay();
		//NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 1));								// > loop1
		//delay();

		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 1, dottedquarter, baseoctave, 0));	// 1 d
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 0, dottedquarter, baseoctave, 0));	// 1 c
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 3, dottedquarter, baseoctave, 0));	// 1 f
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 2, dottedhalf, baseoctave, 0));	// 1 e
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 0, quarter, baseoctave, 5));			// 1 c
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 0, sixteenth, baseoctave, 0));			// 1 c
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 1, dottedquarter, baseoctave, 0));	// 1 d
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 0, dottedquarter, baseoctave, 0));	// 1 c
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 4, dottedquarter, baseoctave, 0));	// 1 g
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 3, dottedhalf, baseoctave, 0));		// 1 f
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 0, quarter, baseoctave, 5));			// 1 c
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 0, sixteenth, baseoctave, 0));			// 1 c
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 0, dottedquarter, higheroctave, 0));	// 1 c
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 5, dottedquarter, baseoctave, 0));	// 1 a
		delay();
		
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 3, dottedeighth, baseoctave, 0));	// 1 f
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 3, sixteenth, baseoctave, 0));			// 1 f
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 2, dottedquarter, baseoctave, 0));	// 1 e
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 1, dottedhalf, baseoctave, 0));		// 1 d
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 5, quarter, baseoctave, 5));			// 1 a#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 5, sixteenth, baseoctave, 0));			// 1 a#
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 5, dottedquarter, baseoctave, 0));	// 1 a
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 3, dottedquarter, baseoctave, 0));	// 1 f
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 4, dottedquarter, baseoctave, 0));	// 1 g
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 3, dottedhalf, baseoctave, 0));		// 1 f
		delay();
		
		// End of track
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(8,8,0));
		
		
		/*
		// ### Still D.R.E. ### //
		
		// Note: I changed the tone duration after I finished this song. You'll have to fix them first.

		int fasttone = 4; // Sixteenth
		int semitone = 3; // Eighth
		int slowtone = 6; // dotted half
		
		int baseoctave = 3;
		int loweroctave = 2;
		
		delay();
		delay();
		NXTPlayer.getInstance().mTrack.setMetadata(new Metadata(0, 1, 1, 1, 3, 5, 2, 7));
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 1));			// > loop1
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 1, fasttone, baseoctave, 0, 4));	// 4 d#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 1, fasttone, baseoctave, 0, 4));	// 4 d#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 1, fasttone, baseoctave, 0, 3));	// 3 d#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 0, fasttone, baseoctave, 0, 3));	// 3 c#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 1));			// > loop1
		
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 0, fasttone, baseoctave, 0, 2));	// 2 c#
		
		
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 2));			// > loop2
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 1));			// > play loop1
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 0, fasttone, baseoctave, 0, 2));	// 2 c#    delay
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 2));			// > loop2
		delay();
		
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 2));			// > play loop2
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 1));			// > play loop1
		
		
		
		
		
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 3));			// > loop3
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 5, semitone, loweroctave, 0));	// a#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 1, slowtone));	// d#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 2, semitone));	// e									x
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 3));			// > loop3
		
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 5, slowtone, loweroctave, 0));	// a#
		
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 4));			// > loop4
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 3));			// > play loop3
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 1, fasttone, baseoctave, 0, 3));	// 3 d#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 0, fasttone, baseoctave, 0, 3));	// 3 c#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 4));			// > loop4
		
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 4));			// > play loop4
		
		
		//NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 2));			// > play loop4
		
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 0, fasttone, baseoctave, 0, 2));	// 2 c#
		
		
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 5));			// > loop5
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 1, fasttone, baseoctave, 0, 6));	// 6 d#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(1, 2, semitone));	// e
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 1, fasttone, baseoctave, 0, 3));	// 3 d#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(7, 0, fasttone, baseoctave, 0, 5));	// 5 c#
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 5));			// > loop5
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(2, 0, 5));			// > play loop5
		
		// End of track
		delay();
		NXTPlayer.getInstance().mTrack.addTone(new Tone(8,8,0));
		*/
	}
	
	private void delay() {
		while(paused) {
			Delay.msDelay(delayPaused);
		}
		Delay.msDelay(delayBetweenTones);
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}
}
