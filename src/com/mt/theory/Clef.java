package com.mt.theory;

import com.mt.R;

public enum Clef {
	ALTO("Alto", 4, Tone.C, R.drawable.clef_c, 0.50f, 28, 34, 26, 32),
	BASS("Bass", 3, Tone.D, R.drawable.clef_bass, 0.64f, 21, 27, 19, 25),
	TENOR("Tenor", 4, Tone.A, R.drawable.clef_c, 0.75f, 26, 32, 26, 32),
	TREBLE("Treble", 5, Tone.B, R.drawable.clef_treble, 0.49f, 35, 41, 33, 39);

	private int drawableResourceId;
	private String keySuffix;
	private int maxFlatKeyPosition;
	private int maxSharpKeyPosition;
	private int middleOctave;

	private Tone middleTone;
	private int minFlatKeyPosition;
	private int minSharpKeyPosition;
	private float ratioAboveMiddle;

	private Clef(String keySuffix, int middleOctave, Tone middleTone, int drawableResourceId, float ratioAboveMiddle,
			int minSharpKeyPosition, int maxSharpKeyPosition, int minFlatKeyPosition, int maxFlatKeyPosition) {
		this.keySuffix = keySuffix;
		this.middleOctave = middleOctave;
		this.middleTone = middleTone;
		this.drawableResourceId = drawableResourceId;
		this.ratioAboveMiddle = ratioAboveMiddle;
		this.minFlatKeyPosition = minFlatKeyPosition;
		this.minSharpKeyPosition = minSharpKeyPosition;
		this.maxFlatKeyPosition = maxFlatKeyPosition;
		this.maxSharpKeyPosition = maxSharpKeyPosition;
	}

	public String getKeySuffix() {
		return keySuffix;
	}

	public int getMaxFlatKeyPosition() {
		return maxFlatKeyPosition;
	}

	public int getMaxSharpKeyPosition() {
		return maxSharpKeyPosition;
	}

	/**
	 * Gets the octave of the note situated on the center line of a staff in
	 * this particular clef
	 * 
	 * @return
	 */
	public int getMiddleOctave() {
		return middleOctave;
	}

	/**
	 * Gets the tone of the note situated on the center line of a staff in this
	 * particular clef
	 * 
	 * @return
	 */
	public Tone getMiddleTone() {
		return middleTone;
	}

	public int getMinFlatKeyPosition() {
		return minFlatKeyPosition;
	}

	public int getMinSharpKeyPosition() {
		return minSharpKeyPosition;
	}

	/**
	 * Gets the proportion of a clefs vertical height that should appear above
	 * the middle line of the staff
	 * 
	 * @return
	 */
	public float getRatioAboveMiddle() {
		return ratioAboveMiddle;
	}

	public int getResourceId() {
		return drawableResourceId;
	}

}
