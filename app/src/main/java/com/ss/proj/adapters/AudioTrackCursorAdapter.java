package com.ss.proj.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ss.proj.R;
import com.ss.proj.database.AudioPlayerContract.AudioTrackEntry;

public class AudioTrackCursorAdapter extends CursorAdapter {

	public AudioTrackCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.audio_track_row, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		String title = cursor.getString(cursor.getColumnIndexOrThrow(AudioTrackEntry.COLUMN_NAME_TITLE));

		TextView trackTitle = (TextView) view.findViewById(R.id.audio_track_row_title);
		trackTitle.setText(title);
	}
}
