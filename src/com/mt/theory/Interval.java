package com.mt.theory;

public class Interval {
	private Note firstNote;
	private Note secondNote;
	private Quality quality;

	public Note getFirstNote() {
		return firstNote;
	}

	public Quality getQuality() {
		return quality;
	}

	public Note getSecondNote() {
		return secondNote;
	}

	public void setFirstNote(Note firstNote) {
		this.firstNote = firstNote;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
	}

	public void setSecondNote(Note secondNote) {
		this.secondNote = secondNote;
	}

}
