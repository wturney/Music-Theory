package com.mt.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.SharedPreferences;

import com.mt.theory.Clef;
import com.mt.theory.KeySignature;
import com.mt.theory.Note;

public class QuizUtil {

	public static Clef getRandomClefFromPreferences(SharedPreferences prefs, String prefKeyPrefix) {
		Random rand = new Random();

		List<Clef> clefs = new ArrayList<Clef>();
		for (Clef clef : Clef.values()) {
			if (prefs.getBoolean(prefKeyPrefix + "Clef" + clef.getKeySuffix(), true)) {
				clefs.add(clef);
			}
		}

		Clef clef = null;
		if (clefs.size() == 1) {
			clef = clefs.get(0);
		} else {
			clef = clefs.get(rand.nextInt(clefs.size()));
		}

		return clef;
	}

	public static KeySignature getRandomKeySignatureFromPreferences(SharedPreferences prefs, String prefKeyPrefix) {
		Random rand = new Random();

		List<KeySignature> keys = new ArrayList<KeySignature>();
		if (prefs.getBoolean(prefKeyPrefix + "Flats", true)) {
			Collections.addAll(keys, KeySignature.getFlatKeys());
		}
		if (prefs.getBoolean(prefKeyPrefix + "Sharps", true)) {
			Collections.addAll(keys, KeySignature.getSharpKeys());
		}

		return keys.get(rand.nextInt(keys.size()));
	}

	public static Note getRandomNoteFromPreferenceRange(Clef clef, SharedPreferences prefs, String prefKeyPrefix) {
		Random rand = new Random();

		int upperLedgerBound = Integer.valueOf(prefs.getString(prefKeyPrefix + "UpperLedgers", "2"));
		int lowerLedgerBound = Integer.valueOf(prefs.getString(prefKeyPrefix + "LowerLedgers", "2"));

		// The first ledger line always begins at 6 steps above or below the
		// middle staff line for a given clef
		int upperLedgerStartValue = NoteUtil.getPositionValue(clef.getMiddleOctave(), clef.getMiddleTone()) + 6;
		int lowerLedgerStartValue = NoteUtil.getPositionValue(clef.getMiddleOctave(), clef.getMiddleTone()) - 6;

		// Numeric representations of the highest and lowest possible notes to
		// random
		int lowerNoteBound = lowerLedgerStartValue - (2 * lowerLedgerBound - 1);
		int upperNoteBound = upperLedgerStartValue + (2 * upperLedgerBound - 1);

		int randomTotalValue = lowerNoteBound + rand.nextInt(upperNoteBound - lowerNoteBound);

		return NoteUtil.getNoteFromPositionValue(randomTotalValue);
	}

}
