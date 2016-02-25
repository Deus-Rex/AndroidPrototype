package com.example.trafficscotlandproto;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class RssFeedHandler {

    // RSS feed URLS
    private URL urlRoadworks;
    private URL urlPlannedRoadworks;
    private URL urlCurrentIncidents;

    // Raw RSS data
    public static String rssRoadworks = null;
    public static String rssPlannedRoadworks = null;
    public static String rssCurrentIncidents = null;


    // Default: get all RSS feeds
    public RssFeedHandler() {
        setUrls();
        new GetRssFeed().execute("Incident");
        new GetRssFeed().execute("Planned");
        new GetRssFeed().execute("Roadworks");
    }

    // Optional: get a single RSS feed
    public RssFeedHandler(String rssType) {
        setUrls();
        new GetRssFeed().execute(rssType);
    }

    // Populates the URL variables with the RSS feed URLs
    private void setUrls() {
        try {
            urlRoadworks = new URL("http://www.trafficscotland.org/rss/feeds/roadworks.aspx");
            urlPlannedRoadworks = new URL("http://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
            urlCurrentIncidents = new URL("http://trafficscotland.org/rss/feeds/currentincidents.aspx");
        } catch (MalformedURLException e) {
            rssCurrentIncidents = null;
            rssPlannedRoadworks = null;
            rssRoadworks = null;
        }
    }

    // AsyncTask for getting the RSS feed as a background operation
    private class GetRssFeed extends AsyncTask<String, Void, String> {

        // Runs in background to get the RSS feed
        protected String doInBackground(String... rssType) {
            URL rssFeedUrl = null;

                switch(rssType[0]) {
                    case "Roadworks":
                        rssFeedUrl = urlRoadworks;
                        if (rssFeedUrl == null) return null;
                        rssRoadworks = getRawRssFeed(rssFeedUrl);
                        break;
                    case "Planned":
                        rssFeedUrl = urlPlannedRoadworks;
                        if (rssFeedUrl == null) return null;
                        rssPlannedRoadworks = getRawRssFeed(urlPlannedRoadworks);
                        break;
                    case "Incident":
                        rssFeedUrl = urlCurrentIncidents;
                        if (rssFeedUrl == null) return null;
                        rssCurrentIncidents = getRawRssFeed(rssFeedUrl);
                        break;
                }
            return null;
        }

        // Runs on main UI thread after the background task finishes
        protected void onPostExecute(String feed) {

        }

        //
        private String getRawRssFeed(URL rssFeedUrl) {
            // Return error message if switch breaks to aid debugging and skip rest of code
            if (rssFeedUrl == null) return null;
            String rssFeed;
            // Get and return RSS feed using Apaches IOUtils library
            try {
                rssFeed = IOUtils.toString(rssFeedUrl, (Charset) null);
            } catch (IOException e) {
                e.printStackTrace();
                rssFeed = null;
            }

            return rssFeed;
        }
    }
}