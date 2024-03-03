package com.exploratory.fact_o_pedia.Models;

public class MyData {
    private String text;
    private String imageUrl;

    public MyData(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
