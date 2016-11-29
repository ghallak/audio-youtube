package com.ss.proj.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.ss.proj.R;
import com.ss.proj.fragments.PlaylistsFragment;

public class AddToPlaylistActivity extends Activity {

	public static final String EXTRA_TRACK = "com.ss.proj.activities.track";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_to_playlist);
		Fragment fragment = new PlaylistsFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.add_to_playlist_child_fragment, fragment);
		ft.commit();

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
