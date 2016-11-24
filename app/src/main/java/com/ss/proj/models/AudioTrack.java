package com.ss.proj.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AudioTrack implements Parcelable {

	private String videoId;
	private String title;
	private int timesPlayed;
	private int length;
	private boolean favorite;

	public String getVideoId() {
		return videoId;
	}

	public String getTitle() {
		return title;
	}

	public int getTimesPlayed() {
		return timesPlayed;
	}

	public int getLength() {
		return length;
	}

	public boolean isFavorite() {
		return favorite;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(videoId);
		out.writeString(title);
		out.writeInt(timesPlayed);
		out.writeInt(length);
		out.writeByte((byte) (favorite ? 1 : 0));
	}

	public static final Parcelable.Creator<AudioTrack> CREATOR
			= new Parcelable.Creator<AudioTrack>() {
		public AudioTrack createFromParcel(Parcel in) {
			return new AudioTrack(in);
		}

		public AudioTrack[] newArray(int size) {
			return new AudioTrack[size];
		}
	};

	private AudioTrack(Parcel in) {
		videoId = in.readString();
		title = in.readString();
		timesPlayed = in.readInt();
		length = in.readInt();
		favorite = in.readByte() != 0;
	}
}
