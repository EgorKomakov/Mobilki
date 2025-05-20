package com.example.kursovaya3;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import androidx.test.services.events.TimeStamp;

import java.util.Objects;

public class NewsItem {
    private String imageUrl;
    private String title;
    private String content;

    private Timestamp date;

    public NewsItem(){}

    // Конструктор
    public NewsItem(String imageUrl, String title, String content) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    // Геттеры
    public String getImageUrl() {
        return imageUrl;
    }


    public String getTitle() {
        return title;
    }


    public String getContent() {
        return content;
    }

    public Timestamp getDate(){return date;}



    // equals() и hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsItem newsItem = (NewsItem) o;
        return Objects.equals(imageUrl, newsItem.imageUrl) &&
                Objects.equals(title, newsItem.title) &&
                Objects.equals(content, newsItem.content) &&
                Objects.equals(date, newsItem.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl, title, content, date);
    }
}
