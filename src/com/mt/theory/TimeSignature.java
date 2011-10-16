package com.mt.theory;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeSignature implements Parcelable {

	public static final Parcelable.Creator<TimeSignature> CREATOR = new Parcelable.Creator<TimeSignature>() {

		@Override
		public TimeSignature createFromParcel(Parcel source) {
			return new TimeSignature(source);
		}

		@Override
		public TimeSignature[] newArray(int size) {
			return new TimeSignature[size];
		}

	};
	private int notesPerMeasure;

	private int noteValue;

	public TimeSignature(int noteValue, int notesPerMeasure) {
		this.noteValue = noteValue;
		this.notesPerMeasure = notesPerMeasure;
	}

	public TimeSignature(Parcel source) {
		this.noteValue = source.readInt();
		this.notesPerMeasure = source.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public int getNotesPerMeasure() {
		return notesPerMeasure;
	}

	public int getNoteValue() {
		return noteValue;
	}

	public void setNotesPerMeasure(int notesPerMeasure) {
		this.notesPerMeasure = notesPerMeasure;
	}

	public void setNoteValue(int noteValue) {
		this.noteValue = noteValue;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getNoteValue());
		dest.writeInt(getNotesPerMeasure());
	}

}
