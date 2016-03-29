package com.example.trafficscotlandproto.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.trafficscotlandproto.MainActivity;
import com.example.trafficscotlandproto.R;
import com.example.trafficscotlandproto.TrafficItem;
import com.example.trafficscotlandproto.Utils;

import org.joda.time.Days;
import org.joda.time.LocalDate;


public class ItemInfoFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set dialog title
        builder.setTitle("Information");

        // Inflate layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_item_info, null);

        // Add items content to view and set it to builder
        viewAdapter(view);
        builder.setView(view);

        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });



        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void viewAdapter(View view) {
        // Get clicked item
        TrafficItem clickedItem = MainActivity.clickedItem;

        // Get textviews from view
        TextView tvTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView tvDesc = (TextView) view.findViewById(R.id.txtDescription);
        TextView tvDescLabel = (TextView) view.findViewById(R.id.txtDescriptionLabel);
        TextView tvDate = (TextView) view.findViewById(R.id.txtDate);
        TextView tvDaysLeft = (TextView) view.findViewById(R.id.txtDaysLeft);
        TextView tvStartDate = (TextView) view.findViewById(R.id.txtDateStart);
        TextView tvEndDate = (TextView) view.findViewById(R.id.txtDateEnd);
        ProgressBar pbDateProgress = (ProgressBar) view.findViewById(R.id.progressDate);


        // Populate the data into the template view using the data object
        tvTitle.setText(Utils.brToNewLine(clickedItem.getTitle()));
        tvDate.setText(Utils.brToNewLine(clickedItem.getDateString()));

        // Set description if it exists, otherwise hide the labels
        if (clickedItem.getDescription() != "") {
            tvDesc.setText(Utils.brToNewLine(clickedItem.getDescription()));
        } else {
            tvDesc.setVisibility(View.GONE); // Set visibility to off
            tvDescLabel.setVisibility(View.GONE);;
        }

        // Set the start/end date only if they exist, otherwise hide the labels
        if (clickedItem.getStartDate() != null || clickedItem.getEndDate() != null) {


            tvStartDate.setText(Utils.brToNewLine(clickedItem.getStartDateString()));
            tvEndDate.setText(Utils.brToNewLine(clickedItem.getEndDateString()));

            // Set traffic progress
            pbDateProgress.setProgress((int) clickedItem.getProgress());


            int daysLeft = Days.daysBetween(new LocalDate(), clickedItem.getEndDate()).getDays();
            int length = Days.daysBetween(clickedItem.getStartDate(), clickedItem.getEndDate()).getDays();
            double progress = clickedItem.getProgress();
            int progressColour;

            tvDaysLeft.setText(String.format("%s days left", Integer.toString(daysLeft)));

            // Get colour depending on percentage of progress and length
            if (length < 3 || progress > 95) progressColour =  Color.parseColor("#78bd00");     // Green
            else if (length < 7 || progress > 80) progressColour = Color.parseColor("#dfd500"); // Yellow
            else if (length < 14) progressColour = Color.parseColor("#df9d00");                 // Orange
            else progressColour = Color.parseColor("#d22a00");                                  // Red

            // Set progress bar colour
            pbDateProgress.getProgressDrawable().setColorFilter(
                    progressColour, android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            tvStartDate.setVisibility(View.GONE); // Set visibility to off
            tvEndDate.setVisibility(View.GONE);
            pbDateProgress.setVisibility(View.GONE);

            // Show published date
            tvDate.setText(clickedItem.getDateString());
        }
    }
}
