package com.mt;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

import com.mt.keys.KeySignatureQuizPreferenceActivity;
import com.mt.notes.NoteQuizPreferenceActivity;

public class HomeActivity extends TabActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.home_layout);

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		intent = new Intent().setClass(this, NoteQuizPreferenceActivity.class);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("noteIdentificationConfig")
			.setIndicator("Notes", res.getDrawable(R.drawable.ic_tab_notes))
			.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, KeySignatureQuizPreferenceActivity.class);
		spec = tabHost.newTabSpec("keyIdentificationConfig")
			.setIndicator("Keys", res.getDrawable(R.drawable.ic_tab_keys))
			.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

	}

}