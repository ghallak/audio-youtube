package com.ss.proj.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import com.ss.proj.activities.MainActivity;

import java.io.File;
import java.io.IOException;

public class AudioPlayerService extends Service
		implements OnPreparedListener, OnCompletionListener, OnErrorListener {

	public static final String ACTION_TOGGLE_PLAYBACK = "com.ss.proj.services.action.TOGGLE_PLAYBACK";
	public static final String ACTION_PLAY = "com.ss.proj.services.action.PLAY";
	public static final String ACTION_PAUSE = "com.ss.proj.services.action.PAUSE";
	public static final String ACTION_STOP = "com.ss.proj.services.action.STOP";
	public static final String ACTION_SKIP = "com.ss.proj.services.action.SKIP";
	public static final String ACTION_REWIND = "com.ss.proj.services.action.REWIND";

	public static final float DUCK_VOLUME = 0.1f;

	private final int NOTIFICATION_ID = 1;

	private MediaPlayer player = null;

	private AudioManager audioManager;
	private NotificationManager notificationManager;
	private Notification.Builder notificationBuilder = null;

	private enum State {
		Preparing,
		Playing,
		Paused,
		Stopped
	}
	private State state = State.Stopped; // TODO: check what this should be

	private enum AudioFocus {
		NoFocusNoDuck,
		NoFocusCanDuck,
		Focused
	}
	private AudioFocus mAudioFocus = AudioFocus.NoFocusNoDuck;

	private void createMediaPlayer() {
		if (player == null) {
			player = new MediaPlayer();
			player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
			player.setOnPreparedListener(this);
			player.setOnCompletionListener(this);
			player.setOnErrorListener(this);
		} else {
			player.reset();
		}
	}

	@Override
	public void onCreate() {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getAction();
		if (action.equals(ACTION_TOGGLE_PLAYBACK)) {
			processTogglePlaybackRequest();
		} else if (action.equals(ACTION_PLAY)) {
			processPlayRequest();
		} else if (action.equals(ACTION_PAUSE)) {
			processPauseRequest();
		} else if (action.equals(ACTION_SKIP)) {
			processSkipRequest();
		} else if (action.equals(ACTION_STOP)) {
			processStopRequest();
		} else if (action.equals(ACTION_REWIND)) {
			processRewindRequest();
		}
		return START_NOT_STICKY;
	}

	private void processTogglePlaybackRequest() {
		if (state == State.Paused || state == State.Stopped) {
			processPlayRequest();
		} else {
			processPauseRequest();
		}
	}

	private void processPlayRequest() {
		tryToGetAudioFocus();
		if (state == State.Stopped) {
			playNextSong(null);
		}
		else if (state == State.Paused) {
			state = State.Playing;
			setUpAsForeground("song title" + " (playing)"); // TODO: change song title to the real value
			configAndStartMediaPlayer();
		}
	}

	private void processPauseRequest() {
		if (state == State.Playing) {
			state = State.Paused;
			player.pause();
			relaxResources(false);
		}
	}

	private void processRewindRequest() {
		if (state == State.Playing || state == State.Paused)
			player.seekTo(0);
	}

	private void processSkipRequest() {
		if (state == State.Playing || state == State.Paused) {
			tryToGetAudioFocus();
			playNextSong(null);
		}
	}

	private void processStopRequest() {
		processStopRequest(false);
	}

	private void processStopRequest(boolean force) {
		if (state == State.Playing || state == State.Paused || force) {
			state = State.Stopped;
			relaxResources(true);
			giveUpAudioFocus();
			stopSelf();
		}
	}

	private void relaxResources(boolean releaseMediaPlayer) {
		stopForeground(true);
		if (releaseMediaPlayer && player != null) {
			player.reset();
			player.release();
			player = null;
		}
	}

	private void giveUpAudioFocus() {
		// TODO: implement this
	}

	private void configAndStartMediaPlayer() {
		if (mAudioFocus == AudioFocus.NoFocusNoDuck) {
			if (player.isPlaying()) {
				player.pause();
			}
			return;
		} else if (mAudioFocus == AudioFocus.NoFocusCanDuck) {
			player.setVolume(DUCK_VOLUME, DUCK_VOLUME);
		} else {
			player.setVolume(1.0f, 1.0f);
		}

		if (!player.isPlaying()) {
			player.start();
		}
	}

	private void processAddRequest(Intent intent) {
		if (state == State.Playing || state == State.Paused || state == State.Stopped) {
			tryToGetAudioFocus();
			playNextSong(intent.getData().toString());
		}
	}

	private void tryToGetAudioFocus() {
		// TODO: implement this
	}

	private void playNextSong(String manualUrl) {
		// TODO: remove this and add the real code
		Intent intent = new Intent(this, YouTubeDownloaderService.class);
		intent.putExtra(YouTubeDownloaderService.EXTRA_STRING, "Ulw-Ccuf29Y");
		startService(intent);
		File cacheDir = new File(getCacheDir(), "com.ss.proj.video_cache");
		File audioFile = new File(cacheDir, "Ulw-Ccuf29Y");
		String filepath = audioFile.getPath();
		System.err.println(audioFile.getPath());
		createMediaPlayer();
		try {
			player.setDataSource(filepath);
			player.prepareAsync();
		} catch (IOException e) {
			System.err.println("ERRRRRROR");
		}
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		playNextSong(null);
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		// The media player is done preparing. That means we can start playing!
		state = State.Playing;
		updateNotification("song title" + " (playing)"); // TODO: change song title to the real value
		configAndStartMediaPlayer();
	}

	private void updateNotification(String text) {
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
		                                             new Intent(getApplicationContext(), MainActivity.class),
		                                             PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setContentText(text)
				.setContentIntent(pi);
		notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
	}

	private void setUpAsForeground(String text) {
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
		                                             new Intent(getApplicationContext(), MainActivity.class),
		                                             PendingIntent.FLAG_UPDATE_CURRENT);
		// Build the notification object.
		notificationBuilder = new Notification.Builder(getApplicationContext())
				.setSmallIcon(android.R.drawable.ic_media_play)
				.setTicker(text)
				.setWhen(System.currentTimeMillis())
				.setContentTitle("AudioPlayer")
				.setContentText(text)
				.setContentIntent(pi)
				.setOngoing(true);
		startForeground(NOTIFICATION_ID, notificationBuilder.build());
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Toast.makeText(getApplicationContext(), "Media player error! Resetting.",
		               Toast.LENGTH_SHORT).show();
		state = State.Stopped;
		relaxResources(true);
		giveUpAudioFocus();
		return true;
	}

	public void onGainedAudioFocus() {
		Toast.makeText(getApplicationContext(), "gained audio focus.", Toast.LENGTH_SHORT).show();
		mAudioFocus = AudioFocus.Focused;
		if (state == State.Playing)
			configAndStartMediaPlayer();
	}
	public void onLostAudioFocus(boolean canDuck) {
		Toast.makeText(getApplicationContext(), "lost audio focus." + (canDuck ? "can duck" :
				"no duck"), Toast.LENGTH_SHORT).show();
		mAudioFocus = canDuck ? AudioFocus.NoFocusCanDuck : AudioFocus.NoFocusNoDuck;
		if (player != null && player.isPlaying())
			configAndStartMediaPlayer();
	}

	@Override
	public void onDestroy() {
		state = State.Stopped;
		relaxResources(true);
		giveUpAudioFocus();
	}
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
