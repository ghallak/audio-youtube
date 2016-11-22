package com.ss.proj.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class DownloaderService extends IntentService {

	private static final String VIDEO_CACHE_DIRECTORY = "com.ss.proj.video_cache";

	protected URL url;
	protected int size;
	protected int downloaded;
	protected String fileName;
	protected Context context;

	private InputStream inputStream;

	public DownloaderService(String name) {
		super(name);
	}

	public void download(Intent intent) {
		HttpURLConnection conn = null;
		inputStream = null;
		try {
			// use https over http
			conn = (HttpURLConnection) url.openConnection();

			// check if this is necessary
			conn.connect();

			if (conn.getResponseCode() / 100 != 2) {
				/* do something about that if this is necessary */
			}

			inputStream = conn.getInputStream();

			System.out.println("Input stream is opened");
			saveToCache();

		} catch (Exception e /* catch some exception here */) {

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {

				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private boolean saveToCache() {
		try {
			File cacheDir = new File(context.getCacheDir(), VIDEO_CACHE_DIRECTORY);

			if (!cacheDir.exists()) {
				if (!cacheDir.mkdir()) {
					return false;
				}
			}

			File file = File.createTempFile(fileName, null, cacheDir);

			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(IOUtils.toByteArray(inputStream));
			outputStream.close();

			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void listCacheFiles() {
		File cacheDir = new File(context.getCacheDir(), VIDEO_CACHE_DIRECTORY);
		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}
		if (cacheDir.exists() && cacheDir.isDirectory()) {
			for (File file : cacheDir.listFiles()) {
				System.out.println("File in the cache: " + file.getName() + " Size = " + file.length());
			}
		} else {
			System.err.println("Cache directory does not exist");
		}
	}

	public void clearCache() {
		File cacheDir = new File(context.getCacheDir(), VIDEO_CACHE_DIRECTORY);
		if (cacheDir.exists()) {
			String[] children = cacheDir.list();
			for (String child : children) {
				File file = new File(cacheDir, child);
				if (file.delete()) {
					System.out.println(child + " was deleted");
				} else {
					System.out.println(child + " couldn not be deleted");
				}
			}
		} else {
			System.err.println("Cache directory does not exist");
		}
	}
}
