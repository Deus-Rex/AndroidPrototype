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

    private RssParser rssRoadworks;
    private RssParser rssPlanned;
    private RssParser rssIncidents;

    public static LocalDate selectedDate;
    private int datePickerID = 999;
    private int year, month, day;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        header = (TextView) findViewById(R.id.txtHeader);
        trafficListView = (ListView) findViewById(R.id.listTrafficView);

        // Setup the RSS feeds
        rssRoadworks = new RssParser(getString(R.string.rssRoadworks));
        rssPlanned = new RssParser(getString(R.string.rssPlanned));
        rssIncidents = new RssParser(getString(R.string.rssIncidents));

        // When items from ListView are clicked, show a Toast with the title of the item
        trafficListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrafficItem clickedItem = (TrafficItem) parent.getItemAtPosition(position);
                String output = "You clicked on: " + clickedItem.getTitle();
                showToast(output);
            }
        });

        // Prepare Date Picker by setting up current date
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
    }

    private void setListView(ArrayList<TrafficItem> inputTrafficList) {
        // Ensure the array is not null
        if (inputTrafficList == null) {
            showToast(getString(R.string.rssLoadingMessage));
            return;
        }
        // Creates an adapter to convert the array to a list view
        TrafficItemAdapter adapter = new TrafficItemAdapter(this, inputTrafficList);

        // Attach the adapter to a ListView
        trafficListView.setAdapter(adapter);
    }

    // Shows and returns all items from a feed
    private ArrayList<TrafficItem> getItems(RssParser trafficType) {
        if (trafficType == null) return null;

        // Get and display the traffic items
        ArrayList<TrafficItem> listOfItems = trafficType.getTrafficItems();
        setListView(listOfItems);

        // Set header to show feed type and number of items
        header.setText(String.format("%s [%d]", header.getText(), listOfItems.size()));
        return listOfItems;
    }

    // Shows and returns items from a specific date from a feed
    private ArrayList<TrafficItem> getItems(RssParser trafficType, LocalDate date) {
        if (trafficType == null) return null;

        // Get and display the traffic items based on the date filter
        ArrayList<TrafficItem> listOfItems = trafficType.getTrafficItems(date);
        setListView(listOfItems);

        // Set header to show feed type and the number of displayed items compared to total items
        header.setText(String.format("%s [%d/%d]", header.getText(), listOfItems.size(), trafficType.getLength()));
        return listOfItems;
    }

    // Show a toast message easier and more robustly
    private void showToast(String toastText) {
        if (currentToast != null) currentToast.cancel(); // Immediately closes open toasts to avoid delay and spam
        currentToast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT); // Create Toast object
        currentToast.show();
    }

    // Show the Date Picker dialog
    public void setDate(View view) {
        showDialog(datePickerID);
    }

    @Override // Show the Date Picker
    protected Dialog onCreateDialog(int id) {
        if (id == datePickerID) {
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePickerView, int inputYear, int inputMonth, int inputDay) {
            // When date picker has new date selected, set new date
            showDate(inputYear, inputMonth+1, inputDay);
        }
    };

    // update SelectedDate variable
    private void showDate(int inputYear, int inputMonth, int inputDay) {
        // Parse input to string
        String year = Integer.toString(inputYear);
        String day = Integer.toString(inputDay);
        String month = new DecimalFormat("00").format(inputMonth); // Ensure month always has 2 digits

        // Create date using string, convert to LocalDate then update selectedDate
        selectedDate = Utils.ConvertDate(day + "/" + month + "/" + year, TrafficItem.dateFormat);
    }

    // Buttons to show specific RSS feeds
    public void showRoadworks(View view) {
        header.setText(R.string.CurrentRoadworks);
        getItems(rssRoadworks, selectedDate);
    }

    public void showPlanned(View view) {
        header.setText(R.string.PlannedRoadworks);
        getItems(rssPlanned, selectedDate);
    }

    public void showIncidents(View view) {
        header.setText(R.string.Incidents);
        getItems(rssIncidents);
    }
}
