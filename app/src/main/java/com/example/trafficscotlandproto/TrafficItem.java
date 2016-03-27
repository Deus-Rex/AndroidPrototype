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
    private LocalDate dateStart;
    private LocalDate dateEnd;

    public static String dateFormat = "yyyy-MM-dd";

    public TrafficItem() {}

    public TrafficItem(String newTitle, String newDescription, String newLink, String newCoord, String newDate){
        // Constructor to create item
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

    public void setDescription(String desc) {
        try {
            // Split spring with each break tag
            String[] splitDesc = desc.split("<br />");

            // Parse start/end dates
            dateStart = Utils.ConvertDate(splitDesc[0].replace("Start Date: ", "").replace(" - 00:00", ""), dateFormat);
            dateEnd = Utils.ConvertDate(splitDesc[1].replace("End Date: ", "").replace(" - 00:00", ""), dateFormat);

            // Set description if there is any
            if (splitDesc.length > 2) this.description = splitDesc[2];
            else this.description = "";
        } catch(Exception e) {
            // If parsing failed, assume there are no dates
            dateStart = null;
            dateEnd = null ;
            this.description = desc;
        }
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
        // Split the coordinates into X and Y, for Google Map feature in future
        String coords [] = georss.split(" ");
        this.geoRssX = Float.valueOf(coords[0]);
        this.geoRssY = Float.valueOf(coords[1]);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateString() {
        return date.toString(dateFormat);
    }

    public void setDate(String newDate) {
        date = Utils.ConvertDate(newDate, dateFormat);
    }

    public LocalDate getStartDate() {
        return dateStart;
    }

    public String getStartDateString() {
        return dateStart.toString(dateFormat);
    }

    public void setStartDate(String newDate) {
        dateStart = Utils.ConvertDate(newDate, dateFormat);
    }

    public LocalDate getEndDate() {
        return dateEnd;
    }

    public String getEndDateString() {
        return dateEnd.toString(dateFormat);
    }

    public void setEndDate(String newDate) {
        dateEnd = Utils.ConvertDate(newDate, dateFormat);
    }
    //endregion



}
