package com.mt.theory;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
	public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

		@Override
		public Note createFromParcel(Parcel source) {
			return new Note(source);
		}

		@Override
		public Note[] newArray(int size) {
			return new Note[size];
		}

	};
	private Accidental accidental;
	private boolean dotted;
	private Duration duration;
	private int octave;

	private Tone tone;

	public Note() {
		this(4, Tone.C);
	}

	public Note(int octave, Tone tone) {
		this.tone = tone;
		this.octave = octave;
		this.duration = Duration.QUARTER;
		this.dotted = false;
		this.accidental = null;
	}

	private Note(Parcel source) {
		int toneVal = source.readInt();
		int accidentVal = source.readInt();
		int durationVal = source.readInt();

		tone = toneVal == -1 ? null : Tone.values()[toneVal];
		accidental = accidentVal == -1 ? null : Accidental.values()[accidentVal];
		duration = durationVal == -1 ? null : Duration.values()[durationVal];
		octave = source.readInt();
		boolean[] bools = new boolean[1];
		source.readBooleanArray(bools);
		dotted = bools[0];
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public Accidental getAccidental() {
		return accidental;
	}

	public Duration getDuration() {
		return duration;
	}

	public int getOctave() {
		return octave;
	}

	public Tone getTone() {
		return tone;
	}

	public boolean isDotted() {
		return dotted;
	}

	public void setAccidental(Accidental accidental) {
		this.accidental = accidental;
	}

	public void setDotted(boolean dotted) {
		this.dotted = dotted;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

	public void setTone(Tone tone) {
		this.tone = tone;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(tone == null ? -1 : tone.ordinal());
		dest.writeInt(accidental == null ? -1 : accidental.ordinal());
		dest.writeInt(duration == null ? -1 : duration.ordinal());
		dest.writeInt(octave);
		dest.writeBooleanArray(new boolean[] { dotted });
	}

}
