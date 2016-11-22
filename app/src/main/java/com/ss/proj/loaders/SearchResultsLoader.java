package com.ss.proj.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.List;

public class SearchResultsLoader extends AsyncTaskLoader<List<SearchResult>> {
	private static final long NUMBER_OF_VIDEOS_RETURNED = 20;

	private List<SearchResult> searchResults;
	private String searchTerm;

	private static final YouTube youtube;

	static {
		// TODO: check if this will throw any exceptions
		youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName("YouTubeSearcher").build();
	}

	public SearchResultsLoader(Context context, String searchTerm) {
		super(context);

		this.searchTerm = searchTerm;
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();

		if (searchResults != null) {
			deliverResult(searchResults);
		}

		if (searchResults == null || takeContentChanged()) {
			forceLoad();
		}
	}

	@Override
	public List<SearchResult> loadInBackground() {
		try {
			YouTube.Search.List search = youtube.search().list("snippet");

			String apiKey = "AIzaSyBwvsplqtb_mQiMSRWbvGQTlYnDwgZjp5U";
			search.setKey(apiKey);
			search.setQ(searchTerm);
			search.setType("video");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			System.out.println(searchTerm);

			SearchListResponse searchResponse = search.execute();
			return searchResponse.getItems();
		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
					                   + e.getDetails().getMessage());
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			System.err.println("error");
			t.printStackTrace();
		}
		System.err.println("null return");
		return null;
	}

	@Override
	public void deliverResult(List<SearchResult> data) {
		if (isReset()) {
			return;
		}

		searchResults = data;

		if (isStarted()) {
			super.deliverResult(data);
		}
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();
		clearResources();
	}

	private void clearResources() {
		searchResults = null;
	}
}
