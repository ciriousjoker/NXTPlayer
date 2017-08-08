package com.ciriousjoker.nxtplayer;

public class Metadata {
	
	
	// Metadata Layout:
	//
	//	0 | 1 | 2 | 4 | 5 | 6
	//	1 | 0 | 3 | 7 | 8 | 9
	//
	//	ID		Name			Explanation
	//_______________________________________________________________________________________________________________
	//	0		White			Calibration field.
	//	1		Black			Calibration field.
	//	2/3		Disk ID			Used to calculate the NXTDisk ID. This could be used to store and load disks later.
	//	4/5/6	BPM				Used to calculate Tone durations
	//	7		Octave			Base octave for the track. All the other octaves are calculated based on this.
	//	8/9		Buffer			This allows for simultanious reading/playing back of the song.
	// 
	// Note:
	//		Values are coded with base 9 and have to be converted back upon reading.
	
	private int id = -1;
	private int bpm = -1;
	private int buffer = -1;
	private int octave = -1;
	
	public Metadata() {
		
	}
	
	public Metadata(int id_upper, int id_lower, int bpm_0, int bpm_1, int bpm_2, int octave, int buf_0, int buf_1 ) {
		setId(id_upper, id_lower);
		setBpm(bpm_0, bpm_1, bpm_2);
		setOctave(octave);
		setBuffer(buf_0, buf_1);
	}
	
	public Metadata setRawData(int[][] _data) {
		// _data[0] = upper row
		// _data[1] = lower row
		setId(_data[2][0], _data[2][1]);
		setBpm(_data[3][0], _data[4][0], _data[5][0]);
		setOctave(_data[3][1]);
		setBuffer(_data[4][1], _data[5][1]);
		return this;
	}
	
	public Metadata setId(int upper, int lower) {
		id = 10 * upper + lower;
		return this;
	}
	
	public Metadata setBpm(int bpm_0, int bpm_1, int bpm_2) {
		bpm = 81 * bpm_0 + 9 * bpm_1 + bpm_2;
		return this;
	}
	
	public Metadata setOctave(int _octave) {
		octave = _octave;
		return this;
	}
	
	public Metadata setBuffer(int buf_0, int buf_1) {
		buffer = 9 * buf_0 + buf_1;
		return this;
	}
	
	// Getters
	public int getId() {
		return id;
	}
	
	public int getBpm() {
		return bpm;
	}
	
	public int getOctave() {
		return octave;
	}
	
	public int getBuffer() {
		return buffer;
	}
}