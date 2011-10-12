package com.mt.theory;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Score implements Parcelable {

	private Clef clef;
	private List<NoteGroup> noteGroups;
	private KeySignature keySignature;
	private TimeSignature timeSignature;

	public static final Parcelable.Creator<Score> CREATOR = new Parcelable.Creator<Score>() {

		@Override
		public Score createFromParcel(Parcel source) {
			return new Score(source);
		}

		@Override
		public Score[] newArray(int size) {
			return new Score[size];
		}

	};

	public Score(Clef clef, List<NoteGroup> noteGroups, KeySignature keySignature, TimeSignature timeSignature) {
		this.clef = clef;
		this.noteGroups = noteGroups;
		this.keySignature = keySignature;
		this.timeSignature = timeSignature;
	}

	public Score(Parcel source) {
		int clefVal = source.readInt();
		int keyVal = source.readInt();
		clef = (clefVal == -1) ? null : Clef.values()[clefVal];
		keySignature = (keyVal == -1) ? null : KeySignature.values()[keyVal];
		timeSignature = (TimeSignature) source.readParcelable(TimeSignature.class.getClassLoader());
		noteGroups = new ArrayList<NoteGroup>();
		Parcelable[] noteParcels = source.readParcelableArray(NoteGroup.class.getClassLoader());
		for (Parcelable noteGroup : noteParcels) {
			noteGroups.add((NoteGroup) noteGroup);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public Clef getClef() {
		return clef;
	}

	public KeySignature getKeySignature() {
		return keySignature;
	}

	public List<NoteGroup> getNoteGroups() {
		return noteGroups;
	}

	public TimeSignature getTimeSignature() {
		return timeSignature;
	}

	public void setClef(Clef clef) {
		this.clef = clef;
	}

	public void setKeySignature(KeySignature keySignature) {
		this.keySignature = keySignature;
	}

	public void setNoteGroups(List<NoteGroup> noteGroups) {
		this.noteGroups = noteGroups;
	}

	public void setTimeSignature(TimeSignature timeSignature) {
		this.timeSignature = timeSignature;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(clef == null ? -1 : clef.ordinal());
		dest.writeInt(keySignature == null ? -1 : keySignature.ordinal());
		dest.writeParcelable(timeSignature, flags);
		dest.writeParcelableArray(noteGroups.toArray(new NoteGroup[] {}), flags);
	}

}
