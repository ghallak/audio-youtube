package com.ss.proj.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ss.proj.R;
import com.ss.proj.activities.AddToPlaylistActivity;
import com.ss.proj.activities.AudioTracksListActivity;
import com.ss.proj.database.AudioPlayerContract.AudioTrackEntry;
import com.ss.proj.database.AudioPlayerContract.PlaylistAudioTrackEntry;
import com.ss.proj.database.AudioPlayerHelper;
import com.ss.proj.models.AudioTrack;

public class AudioTrackCursorAdapter extends CursorAdapter {

	private int playlistId;

	public AudioTrackCursorAdapter(Context context, Cursor cursor, int playlistId) {
		super(context, cursor, 0);
		this.playlistId = playlistId;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.audio_track_row, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final int trackId = cursor.getInt(cursor.getColumnIndexOrThrow(AudioTrackEntry._ID));
		final AudioTrack track = new AudioTrack();
		track.setVideoId(cursor.getString(cursor.getColumnIndexOrThrow(AudioTrackEntry.COLUMN_NAME_VIDEO_ID)));
		track.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(AudioTrackEntry.COLUMN_NAME_TITLE)));

		TextView trackTitle = (TextView) view.findViewById(R.id.audio_track_row_title);
		ImageButton optionsMenu = (ImageButton) view.findViewById(R.id.audio_track_row_menu);
		trackTitle.setText(track.getTitle());

		// set the listeners
		trackTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO: clicking this should play the specified audio track
			}
		});
		optionsMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopup(view, track, trackId);
			}
		});
	}

	private void showPopup(final View view, final AudioTrack track, final int trackId) {
		// TODO: add option in this menu to rename the specified track
		PopupMenu popup = new PopupMenu(view.getContext(), view);
		popup.inflate(R.menu.audio_track_options_menu);
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
					case R.id.remove_from_playlist:
						removeTrackFromPlaylist(view.getContext(), trackId);
						return true;
					case R.id.add_another_playlist:
						addTrackToPlaylist(view.getContext(), track);
						return true;
					default:
						return false;
				}
			}
		});
		popup.show();
	}

	private void removeTrackFromPlaylist(Context context, int trackId) {
		AudioPlayerHelper helper = new AudioPlayerHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();

		// delete track from playlist
		int deletedFromPlaylist = db.delete(
				PlaylistAudioTrackEntry.TABLE_NAME,
		        PlaylistAudioTrackEntry.COLUMN_NAME_AUDIO_ID + " = ? AND " + PlaylistAudioTrackEntry.COLUMN_NAME_PLAYLIST_ID + " = ?",
		        new String[]{Integer.toString(trackId), Integer.toString(playlistId)});

		// delete track from tracks' table if it doesn't belong to any other playlist
		int deletedFromDatabase = db.delete(
				AudioTrackEntry.TABLE_NAME,
				AudioTrackEntry._ID + " NOT IN (SELECT DISTINCT " + PlaylistAudioTrackEntry.COLUMN_NAME_AUDIO_ID + " FROM " + PlaylistAudioTrackEntry.TABLE_NAME + ")",
		        null);

		if (deletedFromPlaylist != 1 || deletedFromDatabase > 1) {
			// TODO: give a log message that something bad happened
		}
		db.close();
		helper.close();

		if (context instanceof AudioTracksListActivity) {
			((AudioTracksListActivity) context).refreshList();
		}
	}

	private void addTrackToPlaylist(Context context, AudioTrack track) {
		Intent intent = new Intent(context, AddToPlaylistActivity.class);
		intent.putExtra(AddToPlaylistActivity.EXTRA_TRACK, track);
		context.startActivity(intent);
	}
}
