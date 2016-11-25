package com.ss.proj.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.ss.proj.R;
import com.ss.proj.fragments.PlaylistsFragment;

public class AddToPlaylistActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_to_playlist);
		Fragment fragment = new PlaylistsFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.add_to_playlist_child_fragment, fragment);
		ft.commit();
	}
}
