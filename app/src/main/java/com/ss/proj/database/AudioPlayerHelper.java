package com.ss.proj.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ss.proj.database.AudioPlayerContract.AudioTrackEntry;
import com.ss.proj.database.AudioPlayerContract.PlaylistAudioTrackEntry;
import com.ss.proj.database.AudioPlayerContract.PlaylistEntry;

public class AudioPlayerHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "AudioPlayer.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_AUDIO_TRACKS_TABLE =
			"CREATE TABLE " + AudioTrackEntry.TABLE_NAME + " (" +
					AudioTrackEntry._ID + " INTEGER PRIMARY KEY," +
					AudioTrackEntry.COLUMN_NAME_VIDEO_ID + TEXT_TYPE + COMMA_SEP +
					AudioTrackEntry.COLUMN_NAME_TITLE + TEXT_TYPE + " )";
	private static final String SQL_CREATE_PLAYLISTS_TABLE =
			"CREATE TABLE " + PlaylistEntry.TABLE_NAME + " (" +
					PlaylistEntry._ID + " INTEGER PRIMARY KEY," +
					PlaylistEntry.COLUMN_NAME_TITLE + TEXT_TYPE + " )";
	private static final String CREATE_PLAYLIST_AUDIO_TRACK_TABLE =
			"CREATE TABLE " + PlaylistAudioTrackEntry.TABLE_NAME + " (" +
					PlaylistAudioTrackEntry._ID + " INTEGER PRIMARY KEY," +
					PlaylistAudioTrackEntry.COLUMN_NAME_AUDIO_ID + INTEGER_TYPE + COMMA_SEP +
					PlaylistAudioTrackEntry.COLUMN_NAME_PLAYLIST_ID + INTEGER_TYPE + " )";

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
