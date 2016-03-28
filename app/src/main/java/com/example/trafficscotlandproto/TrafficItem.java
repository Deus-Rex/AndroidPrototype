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

    // Blank constructor to allow class to be created and info set separately
    public TrafficItem() {}

    // Constructor to create item
    public TrafficItem(String newTitle, String newDescription, String newLink, String newCoord, String newDate){
        setTitle(newTitle);
        setDescription(newDescription);
        setLink(newLink);
        setGeorss(newCoord);
        setDate(newDate);
    }

    //region Getters and Setters

    // Title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Description
    public String getDescription() {
        return description;
    }

    // Set description and also parse any dates from it to populate Start/End Dates
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

    // Item URL
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    // Coordinates
    public String getGeorss() {
        return geoRssX.toString() + ", " + geoRssY.toString();
    }

    public void setGeorss(String georss) {
        // Split the coordinates into X and Y, for Google Map feature in future
        String coords [] = georss.split(" ");
        this.geoRssX = Float.valueOf(coords[0]);
        this.geoRssY = Float.valueOf(coords[1]);
    }

    // Publish date
    public LocalDate getDate() {
        return date;
    }

    public String getDateString() {
        return date.toString(dateFormat);
    }

    public void setDate(String newDate) {
        date = Utils.ConvertDate(newDate, dateFormat);
    }

    // Start date
    public LocalDate getStartDate() {
        return dateStart;
    }

    public String getStartDateString() {
        return dateStart.toString(dateFormat);
    }

    public void setStartDate(String newDate) {
        dateStart = Utils.ConvertDate(newDate, dateFormat);
    }

    // End Date
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
