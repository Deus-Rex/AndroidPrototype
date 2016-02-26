package com.example.trafficscotlandproto;

//import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ryan on 25/02/16.
 */
public class TrafficItem {
    private String title;
    private String description;
    private String link;
    private Float geoRssX;
    private Float geoRssY;
    private Date date; // Find a date datatype

    @Override
    public String toString(){
        String output = "";
        output += "Title: " + getTitle() + "\n";
        output += "Description: " + getDescription() + "\n";
        output += "Date: " + getDate() + "\n";
        output += "Georss: " + getGeorss() + "\n";
        output += "Link: " + getLink();
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

    public String getDate() {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(date);
    }

    public void setDate(String newDate) {
        try {
            DateFormat fmt = DateFormat.getDateInstance(DateFormat.FULL, Locale.UK);
            date = fmt.parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
    }
    //endregion

}
