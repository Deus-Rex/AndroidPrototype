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
    }

    RssParser rssRoadworks = new RssParser("http://www.trafficscotland.org/rss/feeds/roadworks.aspx");
    RssParser rssPlanned = new RssParser("http://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
    RssParser rssIncidents = new RssParser("http://trafficscotland.org/rss/feeds/currentincidents.aspx");


    // Buttons to show specific RSS feeds
    public void showRoadworks(View view) {output.setText(rssRoadworks.getItems()); }
    public void showPlanned(View view) { output.setText(rssPlanned.getItems()); }
    public void showIncidents(View view) { output.setText(rssIncidents.getItems()); }

}
