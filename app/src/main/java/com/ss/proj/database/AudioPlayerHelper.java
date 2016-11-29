package com.ss.proj.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AudioPlayerHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "AudioPlayer.db";

	private static final String TEXT_TYPE = "TEXT";
	private static final String INTEGER_TYPE = "INTEGER";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_AUDIO_TRACKS_TABLE =
			"CREATE TABLE " + AudioPlayerContract.AudioTrackEntry.TABLE_NAME + " (" +
					AudioPlayerContract.AudioTrackEntry._ID + " INTEGER PRIMARY KEY," +
					AudioPlayerContract.AudioTrackEntry.COLUMN_NAME_VIDEO_ID + TEXT_TYPE + COMMA_SEP +
					AudioPlayerContract.AudioTrackEntry.COLUMN_NAME_TITLE + TEXT_TYPE + " )";
	private static final String SQL_CREATE_PLAYLISTS_TABLE =
			"CREATE TABLE" + AudioPlayerContract.PlaylistEntry.TABLE_NAME + " (" +
					AudioPlayerContract.PlaylistEntry._ID + "INTEGER PRIMARY KEY," +
					AudioPlayerContract.PlaylistEntry.COLUMN_NAME_TITLE + TEXT_TYPE + " )";
	private static final String CREATE_PLAYLIST_AUDIO_TRACK_TABLE =
			"CREATE TABLE" + AudioPlayerContract.PlaylistAudioTrackEntry.TABLE_NAME + " (" +
					AudioPlayerContract.PlaylistAudioTrackEntry._ID + "INTEGER PRIMARY KEY," +
					AudioPlayerContract.PlaylistAudioTrackEntry.COLUMN_NAME_AUDIO_ID + INTEGER_TYPE + COMMA_SEP +
					AudioPlayerContract.PlaylistAudioTrackEntry.COLUMN_NAME_PLAYLIST_ID + INTEGER_TYPE + " )";

	public AudioPlayerHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_AUDIO_TRACKS_TABLE);
		db.execSQL(SQL_CREATE_PLAYLISTS_TABLE);
		db.execSQL(CREATE_PLAYLIST_AUDIO_TRACK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
