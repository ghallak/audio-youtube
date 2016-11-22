package com.ss.proj;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.google.api.services.youtube.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class ListSearchResultsActivity extends ListActivity
		implements LoaderManager.LoaderCallbacks<List<SearchResult>> {

	public static final String EXTRA_SEARCH_QUERY = "com.ss.proj.searchQuery";
	private static final int SEARCH_RESULTS_LOADER_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getLoaderManager().initLoader(SEARCH_RESULTS_LOADER_ID, null, this);
	}

	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);

		String selectedItem = (String) getListView().getItemAtPosition(position);

		System.out.println(selectedItem);
	}

	@Override
	public Loader<List<SearchResult>> onCreateLoader(int i, Bundle bundle) {
		String searchTerm = getIntent().getStringExtra(EXTRA_SEARCH_QUERY);

		return new SearchResultsLoader(this, searchTerm);
	}

	@Override
	public void onLoadFinished(Loader<List<SearchResult>> searchResultsLoader,
	                           List<SearchResult> searchResults) {
		List<String> titles = new ArrayList<>(searchResults.size());
		for (SearchResult result : searchResults) {
			titles.add(result.getSnippet().getTitle());
		}
		SearchResultAdapter adapter = new SearchResultAdapter(
				this,
				R.layout.search_result,
				searchResults);
		ListView listView = getListView();
		listView.setAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<List<SearchResult>> searchLoader) {

	}

	public void showPopup(View view) {
		PopupMenu popup = new PopupMenu(this, view);
		popup.inflate(R.menu.seach_result_menu);
		popup.show();
	}
}
