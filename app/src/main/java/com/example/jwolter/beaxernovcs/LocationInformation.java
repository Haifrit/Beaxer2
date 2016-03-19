package com.example.jwolter.beaxernovcs;

/**
 * Created by J.Wolter on 15.03.2016.
 */
public class LocationInformation {

    private String header;
    private String text;
    private String open;
    private String close;
    private String imageUrl;

    public LocationInformation(String header, String text, String open, String close, String imageUrl) {
        this.header = header;
        this.text = text;
        this.open = open;
        this.close = close;
        this.imageUrl = imageUrl;
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
