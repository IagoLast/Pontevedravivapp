package com.pontevedravivapp.xml;

public class Entry {
    private String title;
    private String text;
    private String author;
    private String imageUrl;

    public Entry(String title, String text, String imageUrl, String author) {
        this.title = title;
        this.text = text;
        this.author = author;
        if (imageUrl == null) {
            this.imageUrl = "";
        } else {
            this.imageUrl = imageUrl;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }


}
