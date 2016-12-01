package com.ss.proj.activities;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.ss.proj.adapters.AudioTrackCursorAdapter;
import com.ss.proj.database.AudioPlayerContract.AudioTrackEntry;
import com.ss.proj.database.AudioPlayerContract.PlaylistAudioTrackEntry;
import com.ss.proj.database.AudioPlayerHelper;

public class AudioTracksListActivity extends ListActivity {

	public static final String EXTRA_PLAYLIST_ID = "com.ss.proj.activities.playlist_id";
	public static final String EXTRA_PLAYLIST_TITLE = "com.ss.proj.activities.playlist_title";

	// columns and where clause to query the contents of some playlist
	private static final String[] columns;
	private static final String selection;

	// TODO: close the helper, cursor and the database at some point
	private AudioPlayerHelper helper;
	private int playlistId;

	static {
		columns = new String[]{
				AudioTrackEntry._ID,
				AudioTrackEntry.COLUMN_NAME_VIDEO_ID,
				AudioTrackEntry.COLUMN_NAME_TITLE
		};
		selection = AudioTrackEntry._ID + " IN " + "(SELECT " +
				PlaylistAudioTrackEntry.COLUMN_NAME_AUDIO_ID + " FROM " +
				PlaylistAudioTrackEntry.TABLE_NAME + " WHERE " +
				PlaylistAudioTrackEntry.COLUMN_NAME_PLAYLIST_ID + " = ?)";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		playlistId = getIntent().getIntExtra(EXTRA_PLAYLIST_ID, -1);
		String playlistTitle = getIntent().getStringExtra(EXTRA_PLAYLIST_TITLE);

		if (playlistId == -1 || playlistTitle == null) {
			// TODO: some error has happened here, it shouldn't. check it
		}

		helper = new AudioPlayerHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(AudioTrackEntry.TABLE_NAME,
		                         columns,
		                         selection,
		                         new String[]{Integer.toString(playlistId)},
		                         null, null, null);
		AudioTrackCursorAdapter adapter = new AudioTrackCursorAdapter(this, cursor, playlistId);
		getListView().setAdapter(adapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(playlistTitle);
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

	public void refreshList() {
		AudioTrackCursorAdapter adapter = (AudioTrackCursorAdapter) getListView().getAdapter();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor newCursor = db.query(AudioTrackEntry.TABLE_NAME,
		                         columns,
		                         selection,
		                         new String[]{Integer.toString(playlistId)},
		                         null, null, null);
		adapter.changeCursor(newCursor);
	}
}
