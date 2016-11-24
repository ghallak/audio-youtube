package com.ss.proj.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.ss.proj.R;
import com.ss.proj.fragments.HistoryFragment;
import com.ss.proj.fragments.NowPlayingFragment;

public class MainActivity extends Activity {

	private String[] drawerTitles;
	private ListView drawerList;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		drawerTitles = getResources().getStringArray(R.array.drawer_titles);
		drawerList = (ListView) findViewById(R.id.drawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		drawerList.setAdapter(new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_activated_1,
				drawerTitles));
		drawerList.setOnItemClickListener(new DrawerItemClickListener());
		if (savedInstanceState == null) {
			selectItem(0);
		}

		drawerToggle = new ActionBarDrawerToggle(this,
		                                         drawerLayout,
		                                         R.string.open_drawer,
		                                         R.string.close_drawer) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.addDrawerListener(drawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options_menu, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return true;
	}

	public void onSearch(View view) {
		//Intent intent = new Intent(this, ListSearchResultsActivity.class);
		//EditText searchQuery = (EditText) findViewById(R.id.search);
		//intent.putExtra(ListSearchResultsActivity.EXTRA_SEARCH_QUERY, searchQuery.getText().toString());
		//startActivity(intent);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		Fragment fragment;
		switch (position) {
			case 1:
				fragment = new HistoryFragment();
				break;
			default:
				fragment = new NowPlayingFragment();
		}
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, fragment);
		ft.addToBackStack(null);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

		setActionBarTitle(position);
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.closeDrawer(drawerList);
	}

	private void setActionBarTitle(int position) {
		String title;
		if (position == 0) {
			title = getResources().getString(R.string.app_name);
		} else {
			title = drawerTitles[position];
		}
		getActionBar().setTitle(title);
	}
}
