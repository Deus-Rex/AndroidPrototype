package com.example.trafficscotlandproto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends Activity {

    TextView output;

    private URL urlRoadworks;
    private URL urlPlannedRoadworks;
    private URL urlCurrentIncidents;

    private String rssRoadworks;
    private String rssPlannedRoadworks;
    private String rssCurrentIncidents;

    public MainActivity() throws MalformedURLException {
        urlRoadworks = new URL("http://www.trafficscotland.org/rss/feeds/roadworks.aspx");
        urlPlannedRoadworks = new URL("http://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
        urlCurrentIncidents = new URL("http://trafficscotland.org/rss/feeds/currentincidents.aspx");
    }


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		output = (TextView) findViewById(R.id.txtOutput);

        try {
            LoadRss();
        } catch (IOException e) {
            output.setText(e.toString());
        } finally {
            output.setText(rssPlannedRoadworks);
        }

    }

    private void LoadRss() throws IOException {
        rssCurrentIncidents = getRawRssFeed("Incident");
        rssPlannedRoadworks = getRawRssFeed("Planned");
        rssRoadworks = getRawRssFeed("Roadworks");
    }
    
    private String getRawRssFeed(String rssType) throws IOException {
        // Get RSS feed URL
        URL rssFeedUrl = null;
        switch(rssType) {
            case "Roadworks":
                rssFeedUrl = urlRoadworks;
                break;
            case "Planned":
                rssFeedUrl = urlPlannedRoadworks;
                break;
            case "Incident":
                rssFeedUrl = urlCurrentIncidents;
                break;
        }

        // Return error message if switch breaks to aid debugging and skip rest of code
        if (rssFeedUrl == null) return "Error";

        // Get and return RSS feed using Apaches IOUtils library
        String rssFeedData = IOUtils.toString(rssFeedUrl, (Charset) null);
        return rssFeedData;
    }

    public void showRoadworks(View view) {
        output.setText(rssRoadworks);
    }

    public void showPlanned(View view) {
        output.setText(rssPlannedRoadworks);
    }

    public void showIncidents(View view) {
        output.setText(rssCurrentIncidents);
    }
}
