package com.example.trafficscotlandproto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    TextView header;
    ListView lv;

    RssParser rssRoadworks = new RssParser("http://www.trafficscotland.org/rss/feeds/roadworks.aspx");
    RssParser rssPlanned = new RssParser("http://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
    RssParser rssIncidents = new RssParser("http://trafficscotland.org/rss/feeds/currentincidents.aspx");

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        header = (TextView) findViewById(R.id.txtHeader);
        lv = (ListView) findViewById(R.id.listTrafficView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrafficItem clickedItem = (TrafficItem) parent.getItemAtPosition(position);
                String output = "You clicked on: " + clickedItem.getTitle();

                Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListView(ArrayList<TrafficItem> inputTrafficList) {
        // Create the adapter to convert the array to views
        TrafficItemAdapter adapter = new TrafficItemAdapter(this, inputTrafficList);
        // Attach the adapter to a ListView
        lv.setAdapter(adapter);
    }

    // Buttons to show specific RSS feeds
    public void showRoadworks(View view) {
        header.setText("Current Roadworks");
        setListView(rssRoadworks.Items);
    }

    public void showPlanned(View view) {
        header.setText("Planned Roadworks");
        setListView(rssPlanned.Items);
    }

    public void showIncidents(View view) {
        header.setText("Incidents");
        setListView(rssIncidents.Items);
    }

}
