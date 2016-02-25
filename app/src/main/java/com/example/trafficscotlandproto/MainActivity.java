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
        new RssFeedHandler();
    }

    public void showRoadworks(View view) {
        showRawFeed(RssFeedHandler.rssRoadworks);
    }

    public void showPlanned(View view) {
        showRawFeed(RssFeedHandler.rssPlannedRoadworks);

    }

    public void showIncidents(View view) {
        showRawFeed(RssFeedHandler.rssCurrentIncidents);
    }

    private void showRawFeed(String rssInput) {
        if (rssInput == null) {
            output.setText("Problem loading RSS feed.");
            return;
        }
        else {
            output.setText(rssInput);
        }
    }
}
