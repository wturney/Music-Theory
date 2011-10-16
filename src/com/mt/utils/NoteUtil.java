package com.mt.utils;

import com.mt.theory.Clef;
import com.mt.theory.Note;
import com.mt.theory.Tone;

public class NoteUtil {

	/**
	 * Calculates the number of ledger lines required to display a given note on
	 * a given clef
	 * 
	 * @param note
	 *            The note in question
	 * @param clef
	 *            The clef in question
	 * @return The number of ledger lines that would be required to display the
	 *         given note on the given clef
	 */
	public static int getLedgerCount(Note note, Clef clef) {
		int absStepsFromCenter = Math.abs(getStepsFromStaffCenter(note, clef));
		return (int) Math.floor(0.5 * (absStepsFromCenter - 4));
	}

	/**
	 * Utility method for getting the midi numeric representation of this note.
	 * Midi values are all relative to C4 which is arbitrarily assigned 60. This
	 * value isn't used for generating note quizzes because it takes into
	 * account the entire chromatic scale. The note quiz is only concerned with
	 * the users ability to differentiate notes based on their position on the
	 * staff regardless of accidentals.
	 * 
	 * @param octave
	 * @param tone
	 * @return
	 */
	public static int getMidiPositionValue(int octave, Tone tone) {
		return ((octave + 1) * 12) + tone.getMidiPositionValue();
	}

	/**
	 * Utility method for obtaining an octave & tone from an integer
	 * representation. This is primarily used to simply generating random notes
	 * that fall within a minimum and maximum range on the staff
	 * 
	 * @param positionValue
	 *            An integer representation of a particular notes position
	 *            calculated with the following equation: pval = (octave * 7) +
	 *            toneVal
	 * @return
	 */
	public static Note getNoteFromPositionValue(int positionValue) {
		int octave = (int) Math.floor(positionValue / 7.0);
		Tone tone = Tone.values()[positionValue - (octave * 7)];
		Note note = new Note(octave, tone);
		return note;
	}

	/**
	 * Utility method for generating an integer representation of a octave/tone
	 * position in relation to the musical staff. The value is generated via the
	 * following equation: pval = (octave * 7) + toneVal
	 * 
	 * In this way note A0 is represented by 0, B0 represented by 1, G0 by , A1
	 * by 7 and C4 by 30 and so on. One value for each vertical position on the
	 * staff.
	 * 
	 * @param octave
	 * @param tone
	 * @return
	 */
	public static int getPositionValue(int octave, Tone tone) {
		return (octave * 7) + tone.getStaffPositionValue();
	}

	public static int getStepsFromStaffCenter(int notePositionValue, Clef clef) {
		int staffMiddlePositionValue = getPositionValue(clef.getMiddleOctave(), clef.getMiddleTone());
		return notePositionValue - staffMiddlePositionValue;
	}

	/**
	 * Calculates the number of steps the given note would appear from the
	 * center line of a staff in the given clef
	 * 
	 * @param note
	 *            The note in question
	 * @param clef
	 *            The clef in question
	 * @return An integer representing the number of visual steps between the
	 *         staff center line for the given clef and the position of the
	 *         given note
	 */
	public static int getStepsFromStaffCenter(Note note, Clef clef) {
		int notePositionValue = getPositionValue(note.getOctave(), note.getTone());
		return getStepsFromStaffCenter(notePositionValue, clef);
	}
}
