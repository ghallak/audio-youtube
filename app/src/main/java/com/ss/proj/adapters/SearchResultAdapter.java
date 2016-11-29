package com.ss.proj.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.api.services.youtube.model.SearchResult;
import com.ss.proj.R;
import com.ss.proj.activities.AddToPlaylistActivity;
import com.ss.proj.models.AudioTrack;

import java.util.List;

/*
TODO:read again:
----------------
http://stackoverflow.com/questions/11281952/listview-with-customized-row-layout-android
http://www.ezzylearning.com/tutorial/customizing-android-listview-items-with-custom-arrayadapter
 */

public class SearchResultAdapter extends ArrayAdapter<SearchResult> {

	private int resourceId;
	private List<SearchResult> searchResults;

	public SearchResultAdapter(Context context, int resourceId, List<SearchResult> searchResults) {
		super(context, resourceId, searchResults);
		this.resourceId = resourceId;
		this.searchResults = searchResults;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SearchResultHolder holder = null;
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(resourceId, parent, false);

			holder = new SearchResultHolder();
			holder.videoTitle = (TextView) convertView.findViewById(R.id.video_title);
			holder.optionsMenu = (ImageButton) convertView.findViewById(R.id.result_menu);

			convertView.setTag(holder);
		} else {
			holder = (SearchResultHolder) convertView.getTag();
		}

		SearchResult searchResult = searchResults.get(position);
		holder.videoTitle.setText(searchResult.getSnippet().getTitle());
		holder.videoTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO: play song here
			}
		});
		holder.optionsMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopup(view, position);
			}
		});

		return convertView;
	}

	// show popup menu when clicking on a list item's menu button
	private void showPopup(View view, final int position) {
		PopupMenu popup = new PopupMenu(getContext(), view);
		popup.inflate(R.menu.seach_result_menu);

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
					case R.id.add_to_playlist:
						addTrackToPlaylist(searchResults.get(position));
						return true;
					default:
						return false;
				}
			}
		});
		popup.show();
	}

	private void addTrackToPlaylist(SearchResult result) {
		// TODO: create constructor to convert SearchResult object to AudioTrack
		AudioTrack track = new AudioTrack();
		track.setTitle(result.getId().getVideoId());
		track.setTitle(result.getSnippet().getTitle());

		Intent intent = new Intent(getContext(), AddToPlaylistActivity.class);
		intent.putExtra(AddToPlaylistActivity.EXTRA_TRACK, track);
		getContext().startActivity(intent);
	}

	static class SearchResultHolder {
		TextView videoTitle;
		ImageButton optionsMenu;
	}
}
