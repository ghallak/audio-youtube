package com.ss.proj.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ss.proj.activities.AddToPlaylistActivity;
import com.ss.proj.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		List<String> items = new ArrayList<>();
		items.add("first playlist");
		items.add("second playlist");
		items.add("third playlist");
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
				getActivity(),
				android.R.layout.simple_list_item_1,
				items);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (getActivity() instanceof MainActivity) {

		} else if (getActivity() instanceof AddToPlaylistActivity) {
			addToPlaylist();
		}
	}

	private void addToPlaylist() {
		getActivity().finish();
	}
}
