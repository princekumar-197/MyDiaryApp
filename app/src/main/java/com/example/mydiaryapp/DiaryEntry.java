package com.example.mydiaryapp;

import android.os.Parcel;
import android.os.Parcelable;

public class DiaryEntry implements Parcelable {
    private String title;
    private String content;

    public DiaryEntry(String title, String content) {
        this.title = title;
        this.content = content;
    }

    protected DiaryEntry(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<DiaryEntry> CREATOR = new Creator<DiaryEntry>() {
        @Override
        public DiaryEntry createFromParcel(Parcel in) {
            return new DiaryEntry(in);
        }

        @Override
        public DiaryEntry[] newArray(int size) {
            return new DiaryEntry[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
    }

    @Override
    public String toString() {
        return title;
    }
}