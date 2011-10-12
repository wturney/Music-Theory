package com.mt.keys;

import java.util.Arrays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.mt.R;

public class KeySignatureQuizPreferenceActivity extends PreferenceActivity implements OnClickListener,
		OnPreferenceClickListener, OnPreferenceChangeListener {

	public static final String[] CLEF_KEYS = {
		"keyClefAlto", "keyClefBass", "keyClefTenor", "keyClefTreble" };

	public static final String[] DISPLAY_TYPE_KEYS = {
		"keyFlats", "keySharps" };

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(v.getContext(), KeySignatureQuizActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		preference.setSummary((String) newValue);

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

			if (Arrays.binarySearch(DISPLAY_TYPE_KEYS, chkPref.getKey()) >= 0) {
				evaluateCheckboxGroup(prefs, chkPref, DISPLAY_TYPE_KEYS);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.key_identification_preferences);
		setContentView(R.layout.preferences);

		Button playButton = (Button) findViewById(R.id.btn_play);
		playButton.setOnClickListener(this);

		for (String key : CLEF_KEYS) {
			findPreference(key).setOnPreferenceClickListener(this);
		}

		Preference keyTypePref = findPreference("keyType");
		keyTypePref.setOnPreferenceChangeListener(this);
		keyTypePref.setSummary(PreferenceManager.getDefaultSharedPreferences(this).getString("keyType", "Major"));
	}
}
