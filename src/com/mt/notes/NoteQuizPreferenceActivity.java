package com.mt.notes;

import java.util.Arrays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.mt.R;

public class NoteQuizPreferenceActivity extends PreferenceActivity implements OnClickListener,
		OnPreferenceClickListener, OnPreferenceChangeListener {

	public static final String[] CLEF_KEYS = {
		"noteClefAlto", "noteClefBass", "noteClefTenor", "noteClefTreble" };

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(v.getContext(), NoteQuizActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		preference.setSummary(getLedgerSummary((String) newValue));

		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		SharedPreferences prefs = preference.getSharedPreferences();

		if (preference instanceof CheckBoxPreference) {
			CheckBoxPreference chkPref = (CheckBoxPreference) preference;
			if (Arrays.binarySearch(CLEF_KEYS, chkPref.getKey()) >= 0) {
				evaluateCheckboxGroup(prefs, chkPref, CLEF_KEYS);
			}
		}

		return true;
	}

	private void evaluateCheckboxGroup(SharedPreferences prefs, CheckBoxPreference pref, String[] keys) {
		boolean atLeastOneActive = false;
		for (String key : keys) {
			if (prefs.getBoolean(key, true)) {
				atLeastOneActive = true;
				break;
			}
		}

		if (!atLeastOneActive) {
			pref.setChecked(true);
		}
	}

	private String getLedgerSummary(String value) {
		if (value.equals("0")) {
			return "No Ledger Lines";
		} else if (value.equals("1")) {
			return "1 Ledger Line";
		} else {
			return value + " Ledger Lines";
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.note_identification_preferences);
		setContentView(R.layout.preferences);

		for (String key : CLEF_KEYS) {
			findPreference(key).setOnPreferenceClickListener(this);
		}

		Button playButton = (Button) findViewById(R.id.btn_play);
		playButton.setOnClickListener(this);

		Preference pref = findPreference("noteUpperLedgers");
		pref.setOnPreferenceChangeListener(this);
		pref.setSummary(getLedgerSummary(pref.getSharedPreferences().getString("noteUpperLedgers", "2")));

		pref = findPreference("noteLowerLedgers");
		pref.setOnPreferenceChangeListener(this);
		pref.setSummary(getLedgerSummary(pref.getSharedPreferences().getString("noteLowerLedgers", "2")));
	}

}
