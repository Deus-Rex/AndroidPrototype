package com.example.trafficscotlandproto;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
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
import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RssParser {

    Date currentBuildDate = null;
    String rawRss;

    ArrayList<TrafficItem> Items;

    public RssParser(String inputRssUrl) {
        if (inputRssUrl == null) return;

        try {
            URL rssURL = new URL(inputRssUrl);
            new RawRssFeed().execute(rssURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void parseRssData() {
        Items = new ArrayList<>(); // reset arraylist

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
                newItem.setTitle(currentItem.getElementsByTagName("title").item(0).getTextContent());
                newItem.setDescription(currentItem.getElementsByTagName("description").item(0).getTextContent());
                newItem.setLink(currentItem.getElementsByTagName("link").item(0).getTextContent());
                newItem.setDate(currentItem.getElementsByTagName("pubDate").item(0).getTextContent());
                newItem.setGeorss(currentItem.getElementsByTagName("georss:point").item(0).getTextContent());

                // Add current item to ArrayList
                Items.add(newItem);
            }
        }
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

    //region Getters
    public String getItems() {
        String output = "";
        for (TrafficItem item: Items) {
            output += item.toString() + "\n\n";
        }
        return output;
    }
    //endregion

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
            return rawRssFeed;
        }

        // Runs on main UI thread after the background task finishes
        protected void onPostExecute(String feed) {
            parseRssData(); // When the BG task of getting Raw Rss is finished, Start the parser.
        }
    }
}
