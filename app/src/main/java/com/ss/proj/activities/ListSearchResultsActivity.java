package com.ss.proj.activities;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;

import com.google.api.services.youtube.model.SearchResult;
import com.ss.proj.R;
import com.ss.proj.adapters.SearchResultAdapter;
import com.ss.proj.loaders.SearchResultsLoader;

import java.util.List;

public class ListSearchResultsActivity extends ListActivity
		implements LoaderManager.LoaderCallbacks<List<SearchResult>> {

	private static final int SEARCH_RESULTS_LOADER_ID = 0;

	private String query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// TODO: check if destroying the loader is the only solution (search for better solution)
		getLoaderManager().destroyLoader(SEARCH_RESULTS_LOADER_ID);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
			getLoaderManager().initLoader(SEARCH_RESULTS_LOADER_ID, null, this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options_menu, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		// TODO: handle clicking
	}

	@Override
	public Loader<List<SearchResult>> onCreateLoader(int i, Bundle bundle) {
		return new SearchResultsLoader(this, query);
	}

	@Override
	public void onLoadFinished(Loader<List<SearchResult>> searchResultsLoader,
	                           List<SearchResult> searchResults) {
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
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
					case R.id.add_to_playlist:
						Intent intent = new Intent(ListSearchResultsActivity.this, AddToPlaylistActivity.class);
						startActivity(intent);
						break;
				}
				return true;
			}
		});
		popup.show();
	}
}
