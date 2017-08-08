package com.ciriousjoker.nxtplayer;

public final class ToneConverter {
	
	public static Tone getTone(int[][] _data) {
		
		// Data Layout:
		//
		//	0 | 2 | 4
		//	1 | 3 | 5
		//
		//	data[0][x] - Upper row
		//	data[1][x] - Lower row
		//
		//	0 - Frequency:		Sets the tone.
		//	1 - Modifier:		General modifier (Used for loops, to detect rotations and if the track is finished).
		//	2 - Octave:			Sets a different octave for the tone (optional).
		//	3 - Delay:			Delay after a tone has been played.
		//	4 - AltModifier:	Further modifies the note. If white, the note is considered to be finished.
		//	5 - Duration:		How long will the tone be played.
		
		Log.d("Converting tone");
		int modifier, octave, delay, duration, alt_modifier;
		float frequency;
		
		frequency = _data[0][0];
		modifier = _data[1][0];
		
		// Check if it's two or three blocks of data as the Octave and Delay are optional
		if(_data[0].length == 2) {
			duration = _data[1][1];
			
			return new Tone(modifier, frequency, duration);
		} else if(_data[0].length == 3) {			
			octave = _data[0][1];
			delay = _data[1][1];
			
			alt_modifier = _data[0][2];
			duration = _data[1][2];
			
			if(alt_modifier != -1) {
				return new Tone(modifier, frequency, duration, octave, delay);
			}
			return new Tone(modifier, frequency, duration, octave, delay);
		} else {
			Log.e("The block of data seems to be corrupted. Its length is " + _data[0].length + ".\nUpper value #1: " + _data[0][0] + "\nLower value #1: " + _data[1][0]);
			return null;
		}
	}
	
	public static int getId(int[] part) {	
		return (getGreyscaleId(part[0]) * 10 + getGreyscaleId(part[0]));
	}

	public static int getGreyscaleId(int value) {
		if(value > 100) {
			value = 100;
		} else if(value < 0 ) {
			value = 0;
		}
		
		int ret;
		if(value < 7) {
			ret = 0;
		} else if(value < 18) {
			ret = 1;
		} else if (value < 27) {
			ret = 2;
		} else if (value < 37) {
			ret = 3;
		} else if (value < 46) {
			ret = 4;
		} else if (value < 62) {
			ret = 5;
		} else if (value < 74) {
			ret = 6;
		} else if (value < 90) {
			ret = 7;
		} else {
			ret = 8;
		}
		
		return ret;
	}
	
	public static int convertDuration(int type, int bpm) {
		switch(type) {
		case 0:
			return 0;
		case 1:
			// Full
			return Math.round(1000 * 240 / bpm);
		case 2:
			// Half
			return Math.round(1000 * 120 / bpm);
		case 3:
			// Quarter
			return Math.round(1000 * 60 / bpm);
		case 4:
			// Eighth
			return Math.round(1000 * 30 / bpm);
		case 5:
			// Sixteenth
			return Math.round(1000 * 15 / bpm);
		case 6:
			// Dotted half
			return Math.round(1000 * 180 / bpm);
		case 7:
			// Dotted quarter
			return Math.round(1000 * 90 / bpm);
		case 8:
			// Dotted eighth
			return Math.round(1000 * 45 / bpm);
		default:
			// Default duration in ms
			return 0;
		}
	}
	
	
	public static int convertFrequency(float frequency, int octave) {
		// Set a default value so Eclipse doesn't complain
		double base_frequency = 32.70;

		int type = Math.round(frequency * 2);
		
		
		switch(type) {
		case -1: // ''H
			base_frequency = 30.87;
			break;
		case 0: // 'C
			base_frequency = 32.70;
			break;
		case 1: // 'Des / 'Cis
			base_frequency = 34.65;
			break;
		case 2: // 'D
			base_frequency = 36.71;
			break;
		case 3: // 'Dis / 'Es
			base_frequency = 38.89;
			break;
		case 4: // 'E
			base_frequency = 41.20;
			break;
		case 5: // 'Eis / 'Fes (non-existent, equals 'F).
			// Note:
			// This returns an F even if some retard decides to lower an F by 0.5
			base_frequency = 43.65;
			break;
		case 6: // 'F
			base_frequency = 43.65;
			break;
		case 7: // 'Fis / 'Ges
			base_frequency = 46.25;
			break;
		case 8: // 'G
			base_frequency = 49.00;
			break;
		case 9: // 'Ges / 'As
			base_frequency = 51.91;
			break;
		case 10: // 'A
			base_frequency = 55.0;
			Log.d("Playing a");
			break;
		case 11: // 'Ais / 'H
			base_frequency = 58.27;
			Log.d("Playing a#");
			break;
		case 12: // 'H
			base_frequency = 61.74;
			break;
		case 13: // 'Des / 'Cis
			base_frequency = 34.65;
			break;
		case 14: // 'H
			base_frequency = 61.74;
			break;
		case 15:
			base_frequency = 65.40;
		case 16:
			// White space (ID = 8) isn't allowed for this field
			base_frequency = 0;
			break;
		default:
			// unused
			base_frequency = 0;
		}
		
		double after_octave = base_frequency;
		
		for(int i = 0; i < octave; i++) {
			after_octave *= 2;
		}
		
		int ret = (int) Math.round(after_octave);
		
		return ret;
	}
}
