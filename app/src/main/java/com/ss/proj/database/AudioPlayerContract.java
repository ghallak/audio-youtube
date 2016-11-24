package com.ss.proj.database;

import android.provider.BaseColumns;

public final class AudioPlayerContract {

	private AudioPlayerContract() {}

	public static class AudioTrackEntry implements BaseColumns {
		public static final String TABLE_NAME = "audio_tracks";
		public static final String COLUMN_NAME_VIDEO_ID = "video_id";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_TIMES_PLAYED = "times_played";
		public static final String COLUMN_NAME_LENGTH = "length";
		public static final String COLUMN_NAME_FAVORITE = "favorite";
	}

	public static class PlaylistEntry implements BaseColumns {
		public static final String TABLE_NAME = "playlists";
		public static final String COLUMN_NAME_TITLE = "title";
	}

	public static class PlaylistAudioTrackEntry implements BaseColumns {
		public static final String TABLE_NAME = "playlists_audio";
		public static final String COLUMN_NAME_AUDIO_ID = "audio_id";
		public static final String COLUMN_NAME_PLAYLIST_ID = "playlist_id";
	}
}
