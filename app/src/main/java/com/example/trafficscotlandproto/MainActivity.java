package com.example.trafficscotlandproto;

import android.app.Activity;
import android.os.AsyncTask;
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

//        try {
////            LoadRss();
//            new RssTest().execute();
//        } finally {
//         //   output.setText(rssPlannedRoadworks);
//        }

        try {
            LoadRss();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadRss() throws IOException {
        new RssTest().execute("Incident");
        new RssTest().execute("Planned");
        new RssTest().execute("Roadworks");
    }

    class RssTest extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... rssType) {
            URL rssFeedUrl = null;
            try {
            switch(rssType[0]) {
                case "Roadworks":
                    rssFeedUrl = urlRoadworks;
                    rssRoadworks = getRawRssFeed(rssFeedUrl);
                    break;
                case "Planned":
                    rssFeedUrl = urlPlannedRoadworks;
                    rssPlannedRoadworks = getRawRssFeed(urlPlannedRoadworks);
                    break;
                case "Incident":
                    rssFeedUrl = urlCurrentIncidents;
                    rssCurrentIncidents = getRawRssFeed(rssFeedUrl);
                    break;
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            // Do something on Main UI thread
            output.setText(rssCurrentIncidents);
        }

        private String getRawRssFeed(URL rssFeedUrl) throws IOException {
            // Return error message if switch breaks to aid debugging and skip rest of code
            if (rssFeedUrl == null) return "Error";

            // Get and return RSS feed using Apaches IOUtils library
            return IOUtils.toString(rssFeedUrl, (Charset) null);
        }
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
