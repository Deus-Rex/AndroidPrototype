package com.example.trafficscotlandproto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView output;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		output = (TextView) findViewById(R.id.txtOutput);
//        new RssFeedHandler();

    }

    RssParser rssRoadworks = new RssParser("http://www.trafficscotland.org/rss/feeds/roadworks.aspx");
    RssParser rssPlanned = new RssParser("http://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
    RssParser rssIncidents = new RssParser("http://trafficscotland.org/rss/feeds/currentincidents.aspx");


    // Buttons to show specific RSS feeds
    public void showRoadworks(View view) {

        output.setText(rssRoadworks.output);

    }
    public void showPlanned(View view) { output.setText(rssPlanned.output); }
    public void showIncidents(View view) { output.setText(rssIncidents.output); }

    // Validate and show RSS feed
    private void showRawFeed(String rssInput) {
        if (rssInput == null) {
            output.setText("Problem loading RSS feed.");
            return;
        }
        else {
            output.setText(rssInput);
            return;
        }
    }
}
