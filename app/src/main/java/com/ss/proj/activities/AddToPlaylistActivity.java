package com.ss.proj.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ss.proj.R;
import com.ss.proj.database.AudioPlayerContract.PlaylistEntry;
import com.ss.proj.database.AudioPlayerHelper;
import com.ss.proj.fragments.PlaylistsFragment;

public class AddToPlaylistActivity extends Activity {

	public static final String EXTRA_TRACK = "com.ss.proj.activities.track";

	AudioPlayerHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_to_playlist);
		Fragment fragment = new PlaylistsFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.add_to_playlist_child_fragment, fragment);
		ft.commit();

		getActionBar().setDisplayHomeAsUpEnabled(true);

		helper = new AudioPlayerHelper(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		helper.close();
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

	public void onClickNewPlaylist(View view) {
		// TODO: handle rotating the screen here and for all the activity even before clicking the menu
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.add_to_playlist_activity);
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		final ViewGroup container = (ViewGroup) inflater.inflate(R.layout.popup_window_create_playlist, null);

		// TODO: dismiss menu when clicking out of it
		// TODO: dismiss menu when clicking back button
		final PopupWindow popupWindow = new PopupWindow(container, 900, 600, true);

		// set listeners for ok and cancel buttons
		Button okButton = (Button) container.findViewById(R.id.popup_menu_ok_button);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
				EditText playlistEditText = (EditText) container.findViewById(R.id.playlist_name_edit_text);
				String playlistTitle = playlistEditText.getText().toString();
				if (!playlistTitle.isEmpty()) {
					createNewPlaylist(playlistTitle);
				}
			}
		});
		Button cancelButton = (Button) container.findViewById(R.id.popup_menu_cancel_button);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});

		// display the popup window
		popupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
	}

	private void createNewPlaylist(String playlistTitle) {
		// TODO: should this be in a separate thread ??
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PlaylistEntry.COLUMN_NAME_TITLE, playlistTitle);
		db.insert(PlaylistEntry.TABLE_NAME, null, values);
		db.close();

		// notify playlists fragment about the new fragment
		Fragment fragment = getFragmentManager().findFragmentById(R.id.add_to_playlist_child_fragment);
		if (fragment != null) {
			if (fragment instanceof PlaylistsFragment) {
				((PlaylistsFragment) fragment).refreshList();
			}
		}
	}
}
