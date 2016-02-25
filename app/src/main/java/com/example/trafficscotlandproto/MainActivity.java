package com.example.trafficscotlandproto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends Activity {

    TextView output;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		output = (TextView) findViewById(R.id.txtOutput);
        new RssFeedHandler();
    }

    // Buttons to show specific RSS feeds
    public void showRoadworks(View view) {
//        showRawFeed(RssFeedHandler.getRssRoadworks());

        try {
            RssParser testParser = new RssParser();
            output.setText(testParser.output);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            output.setText("IT FUCKED UP");
        }

    }


    public void showPlanned(View view) { showRawFeed(RssFeedHandler.getRssPlannedRoadworks()); }
    public void showIncidents(View view) { showRawFeed(RssFeedHandler.getRssCurrentIncidents()); }

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
