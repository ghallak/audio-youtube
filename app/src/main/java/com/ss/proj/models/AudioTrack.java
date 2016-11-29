package com.ss.proj.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AudioTrack implements Parcelable {

	private String videoId;
	private String title;

	public AudioTrack() { }

	public String getVideoId() {
		return videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(videoId);
		out.writeString(title);
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
	}
}
