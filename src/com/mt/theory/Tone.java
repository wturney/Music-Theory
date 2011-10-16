package com.mt.theory;

public enum Tone {
	A(0, -3),
	B(1, -1),
	C(2, 0),
	D(3, 2),
	E(4, 4),
	F(5, 5),
	G(6, 7);

	// Midi representation offset used for generating the actual midi value of a
	// tone + octave. Used for audio playback.
	private int midiValue;

	// Arbitrary relative offset for determining the number of visual steps
	// between two given notes on the musical staff
	private int staffValue;

	private Tone(int staffValue, int midiValue) {
		this.staffValue = staffValue;
		this.midiValue = midiValue;
	}

	public int getMidiPositionValue() {
		return midiValue;
	}

	public int getStaffPositionValue() {
		return staffValue;
	}
}
