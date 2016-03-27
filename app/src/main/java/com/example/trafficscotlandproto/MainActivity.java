package com.example.trafficscotlandproto;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

// Ryan Sharp - S1517442

public class MainActivity extends Activity {

    private TextView header;
    private ListView trafficListView;
    private Toast currentToast;

    private RssParser rssRoadworks = new RssParser("http://www.trafficscotland.org/rss/feeds/roadworks.aspx");
    private RssParser rssPlanned = new RssParser("http://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
    private RssParser rssIncidents = new RssParser("http://trafficscotland.org/rss/feeds/currentincidents.aspx");

    public static LocalDate selectedDate;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;



    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        header = (TextView) findViewById(R.id.txtHeader);
        trafficListView = (ListView) findViewById(R.id.listTrafficView);

        trafficListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrafficItem clickedItem = (TrafficItem) parent.getItemAtPosition(position);
                String output = "You clicked on: " + clickedItem.getTitle();
                showToast(output);
            }
        });

        //dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
    }

    private void setListView(ArrayList<TrafficItem> inputTrafficList) {
        // Ensure the array is not null
        if (inputTrafficList == null) {
            showToast("RSS Feed Still Loading");
            return;
        }
        // Creates an adapter to convert the array to a list view
        TrafficItemAdapter adapter = new TrafficItemAdapter(this, inputTrafficList);
        // Attach the adapter to a ListView
        trafficListView.setAdapter(adapter);
    }


    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = monthg

            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int inputYear, int inputMonth, int inputDay) {
        String year = Integer.toString(inputYear);
        String month;
        String day = Integer.toString(inputDay);

        DecimalFormat format = new DecimalFormat("00");
        month = format.format(inputMonth);

        selectedDate = Utils.ConvertDate(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year).toString(), TrafficItem.dateFormat);
    }

    // Buttons to show specific RSS feeds
    public void showRoadworks(View view) {
        header.setText("Current Roadworks");
        getItems(rssRoadworks, selectedDate);
    }

    public void showPlanned(View view) {
        header.setText("Planned Roadworks");
        getItems(rssPlanned, selectedDate);
    }

    public void showIncidents(View view) {
        header.setText("Incidents");
        getItems(rssIncidents);
    }

    // Shows and returns all items from a feed
    private ArrayList<TrafficItem> getItems(RssParser trafficType) {
        ArrayList<TrafficItem> listOfItems;
        listOfItems = trafficType.getTrafficItems();
        setListView(listOfItems);
        header.setText(header.getText() + " [" + listOfItems.size() + "]");
        return listOfItems;
    }

    // Shows and returns items from a specific date from a feed
    private ArrayList<TrafficItem> getItems(RssParser trafficType, LocalDate date) {
        if (trafficType == null) return null;
        ArrayList<TrafficItem> listOfItems;
        listOfItems = trafficType.getTrafficItems(selectedDate);
        setListView(listOfItems);
        header.setText(header.getText() + " [" + listOfItems.size() + "/" + trafficType.getLength() + "]");
        return listOfItems;
    }

    private void showToast(String toastText) {
        if (currentToast != null) currentToast.cancel(); // Immediately closes open toasts to avoid delay and spam
        currentToast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT); // Create Toast object
        currentToast.show();
    }

}
