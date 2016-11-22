package com.ss.proj;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

/*
TODO:read again:
----------------
http://stackoverflow.com/questions/11281952/listview-with-customized-row-layout-android
http://www.ezzylearning.com/tutorial/customizing-android-listview-items-with-custom-arrayadapter
 */

public class SearchResultAdapter extends ArrayAdapter<SearchResult> {

	private Context context;
	private int resourceId;
	private List<SearchResult> searchResults;

	public SearchResultAdapter(Context context, int resourceId, List<SearchResult> searchResults) {
		super(context, resourceId, searchResults);
		this.context = context;
		this.resourceId = resourceId;
		this.searchResults = searchResults;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SearchResultHolder holder = null;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(resourceId, parent, false);

			holder = new SearchResultHolder();
			holder.videoTitle = (TextView) convertView.findViewById(R.id.video_title);
			holder.overflowMenu = (ImageButton) convertView.findViewById(R.id.result_menu);

			convertView.setTag(holder);
		} else {
			holder = (SearchResultHolder) convertView.getTag();
		}

		SearchResult searchResult = searchResults.get(position);
		holder.videoTitle.setText(searchResult.getSnippet().getTitle());

		return convertView;
	}

	static class SearchResultHolder {
		TextView videoTitle;
		ImageButton overflowMenu;
	}
}
