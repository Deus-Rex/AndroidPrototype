package com.example.trafficscotlandproto;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// Ryan Sharp - S1517442

public class RssParser {

    // Instantiated to avoid null before asynctask completes
    private ArrayList<TrafficItem> trafficItems = new ArrayList<>();

    private Date currentBuildDate = null; // Date of RSS build

    // Constructor - setup the feed using the input string
    public RssParser(String inputRssUrl) {
        if (inputRssUrl == null) return; // Return if no rss string exists

        try {
            URL rssURL = new URL(inputRssUrl); // Create URL from string
            new RawRssFeed().execute(rssURL); // Start the RSS AsyncTask
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Return all items
    public ArrayList<TrafficItem> getTrafficItems() {
        return trafficItems;
    }

    // Return items based on date filter criteria
    public ArrayList<TrafficItem> getTrafficItems(LocalDate inputDate) {
        ArrayList<TrafficItem> filteredItems = new ArrayList<>(); // Reset List of items

        // Loop through each traffic item to determine if it meets date filter criteria
        for (int i = 0; i < trafficItems.size(); i++) {
            TrafficItem currentItem = trafficItems.get(i);

            // If date is between the filter criteria, add to filtered item list
            Boolean isEqual = compareDates(inputDate, currentItem.getStartDate(), currentItem.getEndDate());
            if(isEqual) {
               filteredItems.add(currentItem);
            }
        }

        return filteredItems;
    }


    // Check if date is between two other dates
    private Boolean compareDates(LocalDate dateSearch, LocalDate dateStart, LocalDate dateEnd) {
        // > 0 means after dateStart, < 0 means before dateEnd. = 0 would mean they are the same
        // if selected date is after start date AND before end date
        return dateSearch.compareTo(dateStart) > 0 && dateSearch.compareTo(dateEnd) < 0;
    }

    // Converts String to a document able to be parsed as XML
    private Document loadXmlFromString(String stringXML) {
        // Return document from parsed string, otherwise return null
        try {
            // Create document
            DocumentBuilder dBuilder;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();

            // Parse document from string
            InputSource iSource = new InputSource(new StringReader(stringXML));
            return dBuilder.parse(iSource);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Store the RSS feeds build date, to avoid reparsing the same content
    private Date toDate(String newBuildDate) {
        try {
            // parse the date from string
            DateFormat fmt = DateFormat.getDateInstance(DateFormat.FULL, Locale.UK);
            return fmt.parse(newBuildDate);
        } catch (ParseException e) {
            // if RSS build date string is ever corrupt, return todays date instead
            e.printStackTrace();
            return new Date();
        }
    }

    public Integer getLength() {
        return trafficItems.size();
    }

    private class RawRssFeed extends AsyncTask<URL, Void, String> {

        // Runs in background to get the RSS feed
        protected String doInBackground(URL... rssFeedUrl) {
            if (rssFeedUrl == null) return null;
            String rawRssFeed;

            // Get and return RSS feed using Apaches IOUtils library
            try {
                rawRssFeed = IOUtils.toString(rssFeedUrl[0], (Charset) null);
            } catch (IOException e) {
                e.printStackTrace();
                rawRssFeed = null;
            }

            // Start the parser
            parseRssData(rawRssFeed);

            return rawRssFeed;
        }

        // Parse the rss feed
        private void parseRssData(String rawRss) {
            trafficItems = new ArrayList<>(); // reset arraylist

            // Convert RSS feed string to Document for parsing
            Document rssData = loadXmlFromString(rawRss);
            if (rssData == null) return;
            rssData.getDocumentElement().normalize();

            // Get date from RSS feed, if it's the same as current, stop, otherwise set new date and continue
            Date newBuildDate = toDate(rssData.getElementsByTagName("lastBuildDate").item(0).getTextContent());
            if (newBuildDate == currentBuildDate) return;
            currentBuildDate = newBuildDate;

            // Split feed into individual items
            NodeList rssFeedItems = rssData.getElementsByTagName("item");

            // Parse each item for data
            for (int i = 0; i < rssFeedItems.getLength(); i++) {
                Node currentNode = rssFeedItems.item(i);

                // Cycle through each item and extract data to data class
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    // Create new TrafficItem object
                    TrafficItem newItem = new TrafficItem();

                    // Get currently focused item and get each tag
                    Element currentItem = (Element) currentNode;

                    // Set item info from feed
                    newItem.setTitle(currentItem.getElementsByTagName("title").item(0).getTextContent());
                    newItem.setDescription(currentItem.getElementsByTagName("description").item(0).getTextContent());
                    newItem.setLink(currentItem.getElementsByTagName("link").item(0).getTextContent());
                    newItem.setDate(currentItem.getElementsByTagName("pubDate").item(0).getTextContent());
                    newItem.setGeorss(currentItem.getElementsByTagName("georss:point").item(0).getTextContent());

                    // Add current item to ArrayList
                    trafficItems.add(newItem);
                }
            }
        }

        // Runs on main UI thread after the background task finishes
        protected void onPostExecute(String feed) {

        }
    }
}
