package com.example.trafficscotlandproto;

//import org.joda.time.LocalDate;

import org.joda.time.LocalDate;

// Ryan Sharp - S1517442

public class TrafficItem {
    private String title;
    private String description;
    private String link;
    private Float geoRssX;
    private Float geoRssY;
    private LocalDate date;

    public TrafficItem(){
        
    }

    public TrafficItem(String newTitle, String newDescription, String newLink, String newCoord, String newDate){
        setTitle(newTitle);
        setDescription(newDescription);
        setLink(newLink);
        setGeorss(newCoord);
        setDate(newDate);
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

    public LocalDate getDate() {
        return date;
    }

    public String getDateString() {
        return date.toString("dd/MM/yyyy");
    }

    public void setDate(String newDate) {
        date = Utils.ConvertDate(newDate);
    }
    //endregion



}
