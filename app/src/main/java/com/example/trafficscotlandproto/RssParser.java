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

    private Date currentBuildDate = null;
    private String rawRss;
    private ArrayList<TrafficItem> trafficItems = new ArrayList<>(); // Instantiated to avoid null before asynctask completes

    public RssParser(String inputRssUrl) {
        if (inputRssUrl == null) return;

        try {
            URL rssURL = new URL(inputRssUrl);
            new RawRssFeed().execute(rssURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TrafficItem> getTrafficItems() {
        return trafficItems;
    }


    public ArrayList<TrafficItem> getTrafficItems(LocalDate inputDate) {
        ArrayList<TrafficItem> filteredItems = new ArrayList<>();

        for (int i = 0; i < trafficItems.size(); i++) {
            TrafficItem currentItem = trafficItems.get(i);
            LocalDate currentItemDate = currentItem.getDate();
            Boolean isEqual = compareDates(inputDate, currentItemDate);
            if(isEqual) {
               filteredItems.add(currentItem);
            }
        }
        return filteredItems;
    }

    public Integer getLength() {
        return trafficItems.size();
    }

    private Boolean compareDates(LocalDate date1, LocalDate date2) {
        if(date1.compareTo(date2) == 0) {
           return true;
        }
        return false;
    }



    private Document loadXmlFromString(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return builder.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date toDate(String newBuildDate) {
        try {
            DateFormat fmt = DateFormat.getDateInstance(DateFormat.FULL, Locale.UK);
            return fmt.parse(newBuildDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
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
            rawRss = rawRssFeed;
            parseRssData();
            return rawRssFeed;
        }

        private void parseRssData() {
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

                    String itemDesc = currentItem.getElementsByTagName("description").item(0).getTextContent();

                    newItem.setTitle(currentItem.getElementsByTagName("title").item(0).getTextContent());
                    newItem.setDescription(itemDesc);
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
            //parseRssData(); // When the BG task of getting Raw Rss is finished, Start the parser.
        }
    }
}
