package com.mt.keys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mt.QuizActivity;
import com.mt.R;
import com.mt.staff.ScoreView;
import com.mt.theory.Accidental;
import com.mt.theory.Clef;
import com.mt.theory.KeySignature;
import com.mt.theory.NoteGroup;
import com.mt.theory.Score;
import com.mt.utils.QuizUtil;

public class KeySignatureQuizActivity extends QuizActivity {

	private static final String PREFERENCE_KEY_PREFIX = "key";
	private static final char FLAT_CHAR = '\u266D';
	private static final char SHARP_CHAR = '\u266F';

	private static final int[] btnIds = {
		R.id.btn_answer_a, R.id.btn_answer_a_sharp_b_flat, R.id.btn_answer_b, R.id.btn_answer_c,
		R.id.btn_answer_c_sharp_d_flat, R.id.btn_answer_d, R.id.btn_answer_d_sharp_e_flat, R.id.btn_answer_e,
		R.id.btn_answer_f, R.id.btn_answer_f_sharp_g_flat, R.id.btn_answer_g, R.id.btn_answer_g_sharp_a_flat };

	private static final boolean[] btnAccidental = {
		false, true, false, false, true, false, true, false, false, true, false, true };

	private static final String[] btnTextSharp = {
		"A", "A", "B", "C", "C", "D", "D", "E", "F", "F", "G", "G" };

	private static final String[] btnTextFlat = {
		"A", "B", "B", "C", "D", "D", "E", "E", "F", "G", "G", "A" };

	private Map<KeySignature, Integer> majorKeyButtonMap;
	private Map<KeySignature, Integer> minorKeyButtonMap;

	private boolean minorKeyQuestion;

	@Override
	public void displayQuestion() {
		updateButtonText();
		updateQuestionText();

		ScoreView staff = new ScoreView(this, getCurrentQuestion());

		FrameLayout frame = (FrameLayout) findViewById(R.id.question_frame);

		frame.removeAllViews();
		frame.addView(staff);
	}

	@Override
	public boolean evaluateAnswer(int clickedResourceId) {
		Map<KeySignature, Integer> answerMap = minorKeyQuestion ? minorKeyButtonMap : majorKeyButtonMap;

		KeySignature correctKey = getCurrentQuestion().getKeySignature();

		return answerMap.get(correctKey).intValue() == clickedResourceId;
	}

	@Override
	public Score generateQuestion() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		minorKeyQuestion = !prefs.getString("keyType", "Major").equals("Major");

		Clef clef = QuizUtil.getRandomClefFromPreferences(prefs, PREFERENCE_KEY_PREFIX);
		KeySignature key = QuizUtil.getRandomKeySignatureFromPreferences(prefs, PREFERENCE_KEY_PREFIX);

		return new Score(clef, new ArrayList<NoteGroup>(0), key, null);
	}

	private void setupAnswers() {
		majorKeyButtonMap = new HashMap<KeySignature, Integer>();
		minorKeyButtonMap = new HashMap<KeySignature, Integer>();

		majorKeyButtonMap.put(KeySignature.A, R.id.btn_answer_a);
		minorKeyButtonMap.put(KeySignature.A, R.id.btn_answer_f_sharp_g_flat);

		majorKeyButtonMap.put(KeySignature.A_FLAT, R.id.btn_answer_g_sharp_a_flat);
		minorKeyButtonMap.put(KeySignature.A_FLAT, R.id.btn_answer_g);

		majorKeyButtonMap.put(KeySignature.B, R.id.btn_answer_b);
		minorKeyButtonMap.put(KeySignature.B, R.id.btn_answer_g_sharp_a_flat);

		majorKeyButtonMap.put(KeySignature.B_FLAT, R.id.btn_answer_a_sharp_b_flat);
		minorKeyButtonMap.put(KeySignature.B_FLAT, R.id.btn_answer_g);

		majorKeyButtonMap.put(KeySignature.C, R.id.btn_answer_c);
		minorKeyButtonMap.put(KeySignature.C, R.id.btn_answer_a);

		majorKeyButtonMap.put(KeySignature.C_FLAT, R.id.btn_answer_b);
		minorKeyButtonMap.put(KeySignature.C_FLAT, R.id.btn_answer_g_sharp_a_flat);

		majorKeyButtonMap.put(KeySignature.C_SHARP, R.id.btn_answer_c_sharp_d_flat);
		minorKeyButtonMap.put(KeySignature.C_SHARP, R.id.btn_answer_a_sharp_b_flat);

		majorKeyButtonMap.put(KeySignature.D, R.id.btn_answer_d);
		minorKeyButtonMap.put(KeySignature.D, R.id.btn_answer_b);

		majorKeyButtonMap.put(KeySignature.D_FLAT, R.id.btn_answer_c_sharp_d_flat);
		minorKeyButtonMap.put(KeySignature.D_FLAT, R.id.btn_answer_a_sharp_b_flat);

		majorKeyButtonMap.put(KeySignature.E, R.id.btn_answer_e);
		minorKeyButtonMap.put(KeySignature.E, R.id.btn_answer_c_sharp_d_flat);

		majorKeyButtonMap.put(KeySignature.E_FLAT, R.id.btn_answer_d_sharp_e_flat);
		minorKeyButtonMap.put(KeySignature.E_FLAT, R.id.btn_answer_c);

		majorKeyButtonMap.put(KeySignature.F, R.id.btn_answer_f);
		minorKeyButtonMap.put(KeySignature.F, R.id.btn_answer_d);

		majorKeyButtonMap.put(KeySignature.F_SHARP, R.id.btn_answer_f_sharp_g_flat);
		minorKeyButtonMap.put(KeySignature.F_SHARP, R.id.btn_answer_d_sharp_e_flat);

		majorKeyButtonMap.put(KeySignature.G, R.id.btn_answer_g);
		minorKeyButtonMap.put(KeySignature.G, R.id.btn_answer_e);

		majorKeyButtonMap.put(KeySignature.G_FLAT, R.id.btn_answer_f_sharp_g_flat);
		minorKeyButtonMap.put(KeySignature.G_FLAT, R.id.btn_answer_d_sharp_e_flat);
	}

	private void setupListeners() {
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/DroidSerif2.ttf");
		for (int id : btnIds) {
			Button button = (Button) findViewById(id);
			button.setOnClickListener(this);
			button.setTypeface(font);
		}
	}

	private void updateButtonText() {
		Accidental keyType = getCurrentQuestion().getKeySignature().getAccidentalType();

		String[] btnText = keyType.equals(Accidental.FLAT) ? btnTextFlat : btnTextSharp;
		char accidentalChar = keyType.equals(Accidental.FLAT) ? FLAT_CHAR : SHARP_CHAR;

		for (int i = 0; i < btnIds.length; i++) {
			String text = minorKeyQuestion ? btnText[i].toLowerCase() : btnText[i];
			Button button = (Button) findViewById(btnIds[i]);
			if (btnAccidental[i]) {
				button.setText(text + accidentalChar);
			} else {
				button.setText(text);
			}
		}
	}

	private void updateQuestionText() {
		TextView tv = (TextView) findViewById(R.id.question_text);
		if (minorKeyQuestion) {
			tv.setText("Identify the Minor Key");
		} else {
			tv.setText("Identify the Major Key");
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.key_identification);

		setupListeners();

		setupAnswers();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		minorKeyQuestion = savedInstanceState.getBoolean("minorKeyQuestion");

		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("minorKeyQuestion", minorKeyQuestion);

		super.onSaveInstanceState(outState);
	}

}
