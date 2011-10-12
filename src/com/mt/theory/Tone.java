package com.mt.theory;

public enum Tone {
	A(0),
	B(1),
	C(2),
	D(3),
	E(4),
	F(5),
	G(6);

	private int positionValue;

	private Tone(int positionValue) {
		this.positionValue = positionValue;
	}

	public int getRelativePositionValue() {
		return positionValue;
	}
}
