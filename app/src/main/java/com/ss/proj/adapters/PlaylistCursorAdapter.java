package com.ss.proj.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ss.proj.R;
import com.ss.proj.database.AudioPlayerContract.PlaylistEntry;

public class PlaylistCursorAdapter extends CursorAdapter {

	public PlaylistCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.playlist_row, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final int playlistId = cursor.getInt(cursor.getColumnIndexOrThrow(PlaylistEntry._ID));
		String playlistTitle =
				cursor.getString(cursor.getColumnIndexOrThrow(PlaylistEntry.COLUMN_NAME_TITLE));

		TextView playlistTitleTextView = (TextView) view.findViewById(R.id.playlist_row_title);
		ImageButton optionsMenu = (ImageButton) view.findViewById(R.id.playlist_row_menu);
		playlistTitleTextView.setText(playlistTitle);

		// set the listeners
		playlistTitleTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO: this function should open a new Activity or Fragment to display the contents of the playlist
			}
		});
		optionsMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopup(view, playlistId);
			}
		});
	}

	private void showPopup(View view, final int playlistId) {
		// TODO: check if view.getContext() is the same as the context passed to the fragment in the constructor
		PopupMenu popup = new PopupMenu(view.getContext(), view);
		popup.inflate(R.menu.playlist_options_menu);
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
					case R.id.rename_playlist:
						renamePlaylist(playlistId);
						return true;
					case R.id.delete_playlist:
						deletePlaylist(playlistId);
						return true;
					default:
						return false;
				}
			}
		});
		popup.show();
	}

	private void renamePlaylist(int playlistId) {
		// TODO: implement this function
		// TODO: this function should create a popup window to enter the new name
	}

	private void deletePlaylist(int playlistId) {
		// TODO: implement this function
		// TODO: this function should delete the specified playlist and then check if there's
		// TODO: a track that belongs only to this playlist and delete it from tracks' table
	}
}
