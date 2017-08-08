package com.ciriousjoker.nxtplayer;

public class Tone {
	
	// Raw data
	private int[][] RawData = new int[][] {{-1, -1}, {-1, -1}, {-1, -1}};
	
	
	// Data Layout:
	//
	//	0 | 2 | 4
	//	1 | 3 | 5
	//
	//	ID	Name			Explanation
	//___________________________________________________________________________________________________________
	//	0	Frequency		Sets the tone.
	//	1	Modifier		General modifier (Used for loops, to detect rotations and if the track is finished).
	//	2	Octave			Sets a different octave for the tone (optional).
	//	3	Delay			Delay after a tone has been played.
	//	4	AltModifier		Further modifies the note. If white, the note is considered to be finished.
	//	5	Duration		How long will the tone be played.
	
	
	private int modifier = -1;
	private float frequency = -1;
	private int duration = -1;
	private int delay = 0;
	private int octave = 3; // 3 = no change, 2 = base octave - 1, 4 = base octave + 1
	private int alt_modifier = -1;
	private boolean skip = false;	
	private int repetition = 1;
	private boolean raised = false;
	
	public Tone() {
		
	}
	
	public Tone(int _modifier, float _frequency, int _duration ) {
		setFrequency(_frequency);
		setModifier(_modifier);
		setDuration(_duration);
	}
	
	public Tone(int _modifier, float _frequency, int _duration, int _octave, int _delay ) {
		setFrequency(_frequency);
		setModifier(_modifier);
		setDuration(_duration);
		setOctave(_octave);
		setDelay(_delay);
	}
	
	public Tone(int _modifier, float _frequency, int _duration, int _octave, int _delay, int _alt_modifier ) {
		setFrequency(_frequency);
		setModifier(_modifier);
		setDuration(_duration);
		setOctave(_octave);
		setDelay(_delay);
		setAltModifier(_alt_modifier);
	}
	
	// Setters
	public Tone setFrequency(float _frequency) {
		frequency = _frequency;
		return this;
	}
	
	public Tone setDuration(int _duration) {
		duration = _duration;
		return this;
	}
	
	public Tone setModifier(int _modifier) {
		modifier = _modifier;
		return this;
	}
	
	public Tone setOctave(int _octave) {
		octave = _octave;
		return this;
	}
	
	public Tone setRepetition(int _repetition) {
		repetition = _repetition;
		return this;
	}
	
	public Tone setDelay(int _delay) {
		delay = _delay;
		return this;
	}
	
	public Tone setAltModifier(int _alt_modifier) {
		alt_modifier = _alt_modifier;
		return this;
	}
	
	public Tone skip() {
		skip = true;
		return this;
	}
	
	public Tone halfUp() {
		if(!raised) {
			frequency += 0.5;
			raised = true;
		}
		return this;
		/*
		raised = true;
		//Log.d("Frequency before: " + frequency);
		//frequency += 0.5;
		//Log.d("Raised by 0.5, new frequency: " + frequency);
		float raisedfrequency = (float) (frequency + 0.5);
		return new Tone(modifier,raisedfrequency, duration, octave, delay, alt_modifier);
		*/
	}
	
	// Getters
	public int[][] getRawData() {
		return RawData;
	}
	
	public float getFrequency() {
		return frequency;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getModifier() {
		return modifier;
	}
	
	public int getOctave() {
		return octave;
	}
	
	public int getRepetition() {
		return repetition;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public int getAltModifier() {
		return alt_modifier;
	}
	
	public boolean isSkipped() {
		return skip;
	}
}
