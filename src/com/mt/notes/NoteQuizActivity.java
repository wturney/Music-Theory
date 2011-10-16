package com.mt.notes;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.mt.QuizActivity;
import com.mt.R;
import com.mt.audio.MidiTrack;
import com.mt.staff.ScoreView;
import com.mt.theory.Clef;
import com.mt.theory.Note;
import com.mt.theory.NoteGroup;
import com.mt.theory.Score;
import com.mt.theory.Tone;
import com.mt.utils.NoteUtil;
import com.mt.utils.QuizUtil;

public class NoteQuizActivity extends QuizActivity {

	private static final String PREFERENCE_KEY_PREFIX = "note";

	private Map<Tone, Integer> noteButtonMap;

	@Override
	public void displayQuestion() {
		ScoreView staff = new ScoreView(this, getCurrentQuestion());
		FrameLayout frame = (FrameLayout) findViewById(R.id.question_frame);
		frame.removeAllViews();
		frame.addView(staff);
	}

	@Override
	public boolean evaluateAnswer(int clickedResourceId) {
		Note note = getCurrentQuestion().getNoteGroups().get(0).getNotes().get(0);
		return noteButtonMap.get(note.getTone()).intValue() == clickedResourceId;
	}

	@Override
	public Score generateQuestion() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		Clef clef = QuizUtil.getRandomClefFromPreferences(prefs, PREFERENCE_KEY_PREFIX);

		Note note = QuizUtil.getRandomNoteFromPreferenceRange(clef, prefs, PREFERENCE_KEY_PREFIX);

		NoteGroup group = new NoteGroup(note);
		return new Score(clef, Collections.singletonList(group), null, null);
	}

	@Override
	public void onCorrectAnswer() {
		generateNoteAudio();
		playNoteAudio();
		super.onCorrectAnswer();
	}

	private void generateNoteAudio() {
		try {
			MidiTrack midi = new MidiTrack();
			midi.setBPM(80);
			midi.progChange(1);
			Note note = getCurrentQuestion().getNoteGroups().get(0).getNotes().get(0);
			int position = NoteUtil.getMidiPositionValue(note.getOctave(), note.getTone());
			midi.addNote(8, position, 127);
			midi.writeToFile("note.mid", getCacheDir());
		} catch (Exception e) {

		}
	}

	private void playNoteAudio() {
		try {
			MediaPlayer mp = getMediaPlayer();
			if (mp.isPlaying() || mp.isLooping()) {
				mp.stop();
			}
			mp.reset();
			FileInputStream fis = new FileInputStream(getCacheDir() + "/note.mid");
			mp.setDataSource(fis.getFD());
			mp.prepare();
			mp.start();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupListeners() {
		noteButtonMap = new HashMap<Tone, Integer>();

		Button button = (Button) findViewById(R.id.answer_a);
		noteButtonMap.put(Tone.A, R.id.answer_a);
		button.setOnClickListener(this);

		button = (Button) findViewById(R.id.answer_b);
		noteButtonMap.put(Tone.B, R.id.answer_b);
		button.setOnClickListener(this);

		button = (Button) findViewById(R.id.answer_c);
		noteButtonMap.put(Tone.C, R.id.answer_c);
		button.setOnClickListener(this);

		button = (Button) findViewById(R.id.answer_d);
		noteButtonMap.put(Tone.D, R.id.answer_d);
		button.setOnClickListener(this);

		button = (Button) findViewById(R.id.answer_e);
		noteButtonMap.put(Tone.E, R.id.answer_e);
		button.setOnClickListener(this);

		button = (Button) findViewById(R.id.answer_f);
		noteButtonMap.put(Tone.F, R.id.answer_f);
		button.setOnClickListener(this);

		button = (Button) findViewById(R.id.answer_g);
		noteButtonMap.put(Tone.G, R.id.answer_g);
		button.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.note_identification);

		setupListeners();
	}

}
