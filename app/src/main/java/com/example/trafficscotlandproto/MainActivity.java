package com.example.trafficscotlandproto;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MainActivity extends FragmentActivity {

    private TextView header;
    private ListView trafficListView;
    private Toast currentToast;

    private RssParser rssRoadworks;
    private RssParser rssPlanned;
    private RssParser rssIncidents;

    public static LocalDate selectedDate;
    private int datePickerID = 999;
    private int year, month, day;

    private ActionBar actionBar;
    private ActionBar.TabListener tabListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup feeds, action bar and views
        actionBar = getActionBar();
        header = (TextView) findViewById(R.id.txtHeader);
        trafficListView = (ListView) findViewById(R.id.listTrafficView);
        rssRoadworks = new RssParser(getString(R.string.rssRoadworks), this);
        rssPlanned = new RssParser(getString(R.string.rssPlanned), this);
        rssIncidents = new RssParser(getString(R.string.rssIncidents), this);

        // When items from ListView are clicked, show a Toast with the title of the item
        trafficListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrafficItem clickedItem = (TrafficItem) parent.getItemAtPosition(position);
                String output = "You clicked on: " + clickedItem.getTitle();
                showToast(output);
            }
        });

        setupDatePicker();
        setupTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSelectDate:
                showDialog(datePickerID);
                break;
            case R.id.btnRefresh:
                refreshTabs();
                break;
            case R.id.btnResetDate:
                setupDatePicker();
                break;
        }
        return true;
    }

    private void setupTabs() {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); // Add tabs to navbar

        // Create a tab listener that is called when tabs are changed
        tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                setTab(tab);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                setTab(tab);
            }

            private void setTab(ActionBar.Tab tab) {
                String currentTab = tab.getText().toString();
                header.setText(currentTab);

                switch (currentTab) {
                    case "Incident":
                        header.setText(R.string.Incidents);
                        getItems(rssIncidents, false);
                        break;
                    case "Planned" :
                        header.setText(R.string.PlannedRoadworks);
                        getItems(rssPlanned, true);
                        break;
                    case "Roadwork" :
                        header.setText(R.string.CurrentRoadworks);
                        getItems(rssRoadworks, true);
                        break;
                    default : break;
                }
            }
        };

        actionBar.addTab(actionBar.newTab().setText(R.string.button_Incident).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(R.string.button_Planned).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(R.string.button_Roadworks).setTabListener(tabListener));
    }

    public void refreshTabs() {
        actionBar.selectTab(actionBar.getSelectedTab());
    }

    private void setListView(ArrayList<TrafficItem> inputTrafficList) {
        // Ensure the array is not null
        if (inputTrafficList == null) {
            showToast(getString(R.string.rssLoadingMessage));
            return;
        }
        // Creates an adapter to convert the array to a list view
        TrafficItemAdapter adapter = new TrafficItemAdapter(this, inputTrafficList);

        if (inputTrafficList.size() == 0) header.setText("Loading...");

        // Attach the adapter to a ListView
        trafficListView.setAdapter(adapter);
    }

    // Shows and returns items from a specific date from a feed
    private ArrayList<TrafficItem> getItems(RssParser trafficType, Boolean canHaveFilter) {
        if (trafficType == null) return null;

        ArrayList<TrafficItem> listOfItems;

        Boolean filterActive = selectedDate.compareTo(new LocalDate()) != 0;

        if (canHaveFilter && filterActive) {
            // If filter is active and items can be filtered, send filter criteria
            listOfItems = trafficType.getTrafficItems(selectedDate);
            header.setText(String.format("%s [%d/%d]", header.getText(), listOfItems.size(), trafficType.getLength()));
        } else {
            // Else just get all of the items
            listOfItems = trafficType.getTrafficItems();
            header.setText(String.format("%s [%d]", header.getText(), listOfItems.size()));
        }

        // Get and display the traffic items based on the date filter
        setListView(listOfItems);

        // Set header to show feed type and the number of displayed items compared to total items
        return listOfItems;
    }

    // Show a toast message easier and more robustly
    private void showToast(String toastText) {
        if (currentToast != null)
            currentToast.cancel(); // Immediately closes open toasts to avoid delay and spam
        currentToast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT); // Create Toast object
        currentToast.show();
    }

    private void setupDatePicker() {
        // Prepare Date Picker by setting up current date
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
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
            showDate(inputYear, inputMonth + 1, inputDay);
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
        refreshTabs();
    }
}
