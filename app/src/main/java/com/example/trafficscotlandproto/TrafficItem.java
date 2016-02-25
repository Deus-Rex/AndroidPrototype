package com.example.trafficscotlandproto;

/**
 * Created by ryan on 25/02/16.
 */
public class TrafficItem {
    private String title;
    private String description;
    private String link;
    private Float geoRssX;
    private Float geoRssY;
    private String author;
    private String comments;
    private String date; // Find a date datatype

    @Override
    public String toString(){
        String output = "";
        output += "Title: " + getTitle() + "\n";
        output += "Description: " + getDescription() + "\n";
        output += "Date: " + getDate() + "\n";
        output += "Georss: " + getGeorss() + "\n";
        output += "Link: " + getLink() + "\n\n";
        return output;
    }


    //region Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGeorss() {
        return geoRssX.toString() + ", " + geoRssY.toString();
    }

    public void setGeorss(String georss) {
        String coords [] = georss.split(" ");
        this.geoRssX = Float.valueOf(coords[0]);
        this.geoRssY = Float.valueOf(coords[1]);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    //endregion

}
