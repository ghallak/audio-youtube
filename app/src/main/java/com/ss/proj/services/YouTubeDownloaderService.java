package com.ss.proj.services;

import android.content.Intent;

import com.ss.proj.utils.YouTubeParser;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class YouTubeDownloaderService extends DownloaderService {

	public static final String EXTRA_STRING = "str";

	private List<URL> urlList;

	public YouTubeDownloaderService() {
		super("YouTubeDownloaderService");
	}

	@Override
	public void onHandleIntent(Intent intent) {
		try {
			String videoId = intent.getStringExtra(EXTRA_STRING);
			urlList = new YouTubeParser(videoId).getAudioURLs();

			fileName = videoId;
			url = chooseBestURL();
			size = 0;
			downloaded = 0;

		} catch (IOException e) {
			// TODO: see what can we do here
		}
	}

	// TODO: check how to choose the best URL
	private URL chooseBestURL() {
		for (URL url : urlList) {
			if (url.toString().contains("mp4")) {
				return url;
			}
		}
		System.err.println("Couldn't choose mp4 file URL");
		for (URL url : urlList) {
			// choose the second best url
		}

		// see what will happen if nothing was found
		return null;
	}

}
