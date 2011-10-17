package com.mt.theory;


public enum KeySignature {
	A(3, Accidental.SHARP),
	A_FLAT(4, Accidental.FLAT),
	B(5, Accidental.SHARP),
	B_FLAT(2, Accidental.FLAT),
	C(0, Accidental.SHARP),
	C_FLAT(7, Accidental.FLAT),
	C_SHARP(7, Accidental.SHARP),
	D(2, Accidental.SHARP),
	D_FLAT(5, Accidental.FLAT),
	E(4, Accidental.SHARP),
	E_FLAT(3, Accidental.FLAT),
	F(1, Accidental.FLAT),
	F_SHARP(6, Accidental.SHARP),
	G(1, Accidental.SHARP),
	G_FLAT(6, Accidental.FLAT);

	private static final KeySignature[] FLAT_KEYS = {
		C, B_FLAT, E_FLAT, A_FLAT, D_FLAT, G_FLAT, C_FLAT };
	private static final Tone[] FLAT_TONE_ORDER = {
		Tone.B, Tone.E, Tone.A, Tone.D, Tone.G, Tone.C, Tone.F };

	private static final KeySignature[] SHARP_KEYS = {
		C, G, D, A, E, B, F_SHARP, C_SHARP };
	private static final Tone[] SHARP_TONE_ORDER = {
		Tone.F, Tone.C, Tone.G, Tone.D, Tone.A, Tone.E, Tone.B };

	public static KeySignature[] getFlatKeys() {
		KeySignature[] copy = new KeySignature[FLAT_KEYS.length];
		for (int i = 0; i < FLAT_KEYS.length; i++) {
			copy[i] = FLAT_KEYS[i];
		}
		return copy;
	}

	public static KeySignature[] getSharpKeys() {
		KeySignature[] copy = new KeySignature[SHARP_KEYS.length];
		for (int i = 0; i < SHARP_KEYS.length; i++) {
			copy[i] = SHARP_KEYS[i];
		}
		return copy;
	}

	private int accidentalCount;

	private Accidental accidentalType;

	private KeySignature(int accidentalCount, Accidental accidentalType) {
		this.accidentalCount = accidentalCount;
		this.accidentalType = accidentalType;
	}

	public Tone[] getAccidentals() {
		if (accidentalCount == 0) {
			return new Tone[0];
		}

		Tone[] order = accidentalType.equals(Accidental.SHARP) ? SHARP_TONE_ORDER : FLAT_TONE_ORDER;
		Tone[] copy = new Tone[accidentalCount];
		for (int i = 0; i < accidentalCount; i++) {
			copy[i] = order[i];
		}

		return copy;
	}

	public Accidental getAccidentalType() {
		return accidentalType;
	}
}
