package com.ss.proj.fragments;

import android.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ss.proj.adapters.PlaylistCursorAdapter;
import com.ss.proj.database.AudioPlayerContract.PlaylistEntry;
import com.ss.proj.database.AudioPlayerHelper;

public class PlaylistsFragment extends ListFragment {

	// TODO: database, cursor and helper, all should be closed!!

	private AudioPlayerHelper helper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// TODO: should I create the helper somewhere else ?
		helper = new AudioPlayerHelper(getActivity());

		// TODO: should I access the data from a separate thread ??
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(PlaylistEntry.TABLE_NAME,
		                         new String[]{PlaylistEntry._ID, PlaylistEntry.COLUMN_NAME_TITLE},
		                         null, null, null, null, null);
		PlaylistCursorAdapter adapter = new PlaylistCursorAdapter(getActivity(), cursor);
		setListAdapter(adapter);
	}

	public void refreshList() {
		PlaylistCursorAdapter adapter = (PlaylistCursorAdapter) getListAdapter();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor newCursor = db.query(PlaylistEntry.TABLE_NAME,
		                            new String[]{PlaylistEntry._ID, PlaylistEntry.COLUMN_NAME_TITLE},
		                            null, null, null, null, null);
		adapter.changeCursor(newCursor);
	}


}
