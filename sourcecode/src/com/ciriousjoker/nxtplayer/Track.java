package com.ciriousjoker.nxtplayer;

import java.util.ArrayList;

public class Track {
	private int PositionIndicator = 0;	// Playing position of the track
	
	// Complete track data
	Metadata mMetaData;
	ArrayList<Tone> mMusicData = new ArrayList<Tone>();
	
	// Loop management
	ArrayList<ArrayList<Tone>> mLoop;
	ArrayList<Integer> mLoopMarker;
	
	public Track() {
		
	}
	
	// Track management
	public Metadata getMetaData() {
		return mMetaData;
	}
	
	public ArrayList<Tone> getMusicData() {
		return mMusicData;
	}
	
	public void storeTrack() {
		// Not implemented.
		// Would be used to store the track in a file named [ID].midi after reading it
		// This file could be played back using an interface even when the NXTDisk isn't present
	}
	
	public Track loadTrack() {
		// Not implemented.
		// Would be used to load the track from the memory
		return this;
	}
	
	// Set the meta data
	public void setMetadata(Metadata _meta) {
		mMetaData = _meta;
		
		Log.u(0, "Metadata:");
		Log.u(1, "ID: " + mMetaData.getId());
		Log.u(2, "Octave: " + mMetaData.getOctave());
		Log.u(3, "Bpm: " + mMetaData.getBpm());
		Log.u(4, "Buffer: " + mMetaData.getBuffer());	
	}
	
	
	// Tone management
	public void addTone(Tone _Tone) {
		Log.d("Added tone");
		mMusicData.add(_Tone);
	}
	
	public Tone returnTone(int index) {
		PositionIndicator = index;
		return mMusicData.get(index);
	}
	
	// Loop management
	public void setLoopMarker(int id) {
		Log.d("setLoopMarker(" + id + ")");
		Log.d("Current size: " + mLoopMarker.size());
		
		if(mLoopMarker.size() >= id) {
			// Second marker
			final int begin = mLoopMarker.get(id-1);
			final int end = PositionIndicator;
			
			// Can't do this on a separate thread as then it would come to problems when
			// playing the loop right after adding it
			Track.this.mLoop.add(getTonesFromTo(begin, end));
		} else {
			// First marker
			mLoopMarker.add(PositionIndicator);
			Log.d("Added " + PositionIndicator + " at " + id);
		}
	}
	
	private ArrayList<Tone> getTonesFromTo(int from, int to) {
		ArrayList<Tone> ret = new ArrayList<Tone>();
		Log.d("Added loop from " + (from+1) + " to " + (to-1));
		// (from+1) & (to-1) to cut away the loop markers without tone data
		for(int i = (from+1); i <= (to-1); i++) {
			ret.add(mMusicData.get(i));
		}
		return ret;
	}
	
	public ArrayList<Tone> getLoop(int id) {
		Log.d("getLoop(" + (id - 1) + ")");
		Log.d("Loop size: " + mLoop.size());
		
		if(hasLoop(id)) {
			return mLoop.get(id-1);
		}
		return new ArrayList<Tone>();
	}
	
	public boolean hasLoop(int id) {
		try {
			if(mLoop.contains(mLoop.get(id-1))) {
				return true;
			}
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}
	
	public void clearLoops() {
		mLoopMarker = new ArrayList<Integer>();
		mLoop = new ArrayList<ArrayList<Tone>>();
	}
	
	public boolean isBuffered() {
		if(isMetaRead()) {
			Log.u(5, "Buffer: " + (mMusicData.size()) + " / " + (PositionIndicator + mMetaData.getBuffer()));
			return (mMusicData.size()) >= (PositionIndicator + mMetaData.getBuffer());
		}
		return false;
	}
	
	public boolean isMetaRead() {
		if(mMetaData != null) {
			return true;
		}
		return false;
	}
}
