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
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ryan on 25/02/16.
 */
public class RssParser {

    String output = "";

    String rawRss;
    ArrayList<TrafficItem> testArray;

    public RssParser(String inputFeed) {
        if (inputFeed == null) return;

        try {
            // Get Data
            URL rssURL = new URL(inputFeed);
            new RawRssFeed().execute(rssURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public RssParser(String rssData) {
//        if (rssData == null) return;
//        parseRssData(rssData);
//    }

    private void parseRssData() {
        // Create new TrafficItem ArrayList
        testArray = new ArrayList<TrafficItem>();

        // Convert RSS feed string to Document for parsing
        Document rssData = loadXmlFromString(rawRss);
        rssData.getDocumentElement().normalize();

        // Parse the RSS feed for the individual items
        NodeList rssFeedItems = rssData.getElementsByTagName("item");

        // Parse each item for data
        for (int i = 0; i < rssFeedItems.getLength(); i++) {
            // Item currently focused on
            Node currentItem = rssFeedItems.item(i);



            if (currentItem.getNodeType() == Node.ELEMENT_NODE) {
                // Create new TrafficItem object
                TrafficItem newItem = new TrafficItem();

                Element eElement = (Element) currentItem;
                newItem.setTitle(eElement.getElementsByTagName("title").item(0).getTextContent());
                newItem.setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
                newItem.setLink(eElement.getElementsByTagName("link").item(0).getTextContent());
                newItem.setDate(eElement.getElementsByTagName("pubDate").item(0).getTextContent());
                newItem.setGeorss(eElement.getElementsByTagName("georss:point").item(0).getTextContent());

                testArray.add(newItem);
                output += newItem.toString();
            }
        }
    }



    private Document loadXmlFromString(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return builder.parse(is);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            return rawRssFeed;
        }

        // Runs on main UI thread after the background task finishes
        protected void onPostExecute(String feed) {
            parseRssData();

        }
    }
}
