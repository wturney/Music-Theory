package com.mt.theory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteGroup implements Parcelable {
	private List<Note> notes;

	public static final Parcelable.Creator<NoteGroup> CREATOR = new Parcelable.Creator<NoteGroup>() {

		@Override
		public NoteGroup createFromParcel(Parcel source) {
			return new NoteGroup(source);
		}

		@Override
		public NoteGroup[] newArray(int size) {
			return new NoteGroup[size];
		}

	};

	public NoteGroup() {
		this.notes = new ArrayList<Note>();
	}

	public NoteGroup(Collection<Note> notes) {
		this.notes = new ArrayList<Note>(notes);
	}

	public NoteGroup(Note... notes) {
		this.notes = Arrays.asList(notes);
	}

	public NoteGroup(Parcel source) {
		Parcelable[] parcels = source.readParcelableArray(Note.class.getClassLoader());
		notes = new ArrayList<Note>();
		for (Parcelable parcel : parcels) {
			notes.add((Note) parcel);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public List<Note> getNotes() {
		return notes;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelableArray(notes.toArray(new Note[] {}), flags);
	}
}
