package com.ss.proj.activities;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.ss.proj.adapters.AudioTrackCursorAdapter;
import com.ss.proj.database.AudioPlayerContract.AudioTrackEntry;
import com.ss.proj.database.AudioPlayerContract.PlaylistAudioTrackEntry;
import com.ss.proj.database.AudioPlayerHelper;

public class AudioTracksListActivity extends ListActivity {

	public static final String EXTRA_PLAYLIST_ID = "com.ss.proj.activities.playlist_id";

	// TODO: close the helper, cursor and the database at some point
	private AudioPlayerHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int playlistId = getIntent().getIntExtra(EXTRA_PLAYLIST_ID, -1);

		if (playlistId == -1) {
			// TODO: some error has happened here, it shouldn't. check it
		}

		helper = new AudioPlayerHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "SELECT " + AudioTrackEntry._ID + "," +
				AudioTrackEntry.COLUMN_NAME_VIDEO_ID + "," +
				AudioTrackEntry.COLUMN_NAME_TITLE +
				" FROM " + AudioTrackEntry.TABLE_NAME +
				" WHERE " + AudioTrackEntry._ID + " IN " +
				"(" +
				"SELECT " + PlaylistAudioTrackEntry.COLUMN_NAME_AUDIO_ID +
				" FROM " + PlaylistAudioTrackEntry.TABLE_NAME +
				" WHERE " + PlaylistAudioTrackEntry.COLUMN_NAME_PLAYLIST_ID + " = ?" +
				")";
		Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(playlistId)});
		AudioTrackCursorAdapter adapter = new AudioTrackCursorAdapter(this, cursor);
		getListView().setAdapter(adapter);
	}
}
