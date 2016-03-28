package com.example.trafficscotlandproto;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.Days;

import java.util.ArrayList;

// Ryan Sharp - S1517442

public class TrafficItemAdapter extends ArrayAdapter<TrafficItem> {

    public TrafficItemAdapter(Context context, ArrayList<TrafficItem> trafficItem) {
        super(context, 0, trafficItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the Traffic Item
        TrafficItem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get textviews from view
        TextView tvTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.txtDescription);
        TextView tvDate = (TextView) convertView.findViewById(R.id.txtDate);
        TextView tvDateLabel = (TextView) convertView.findViewById(R.id.txtDateLabel);
        TextView tvLink = (TextView) convertView.findViewById(R.id.txtLink);
        TextView tvCoord = (TextView) convertView.findViewById(R.id.txtCoord);
        TextView tvStartDate = (TextView) convertView.findViewById(R.id.txtStartDate);
        TextView tvEndDate = (TextView) convertView.findViewById(R.id.txtEndDate);
        ProgressBar pbDateProgress = (ProgressBar) convertView.findViewById(R.id.progressDate);
        View viewHighlight = convertView.findViewById(R.id.itemHighlight);

        // Populate the data into the template view using the data object
        tvTitle.setText(Utils.brToNewLine(item.getTitle()));
        tvDesc.setText(Utils.brToNewLine(item.getDescription()));
        tvDate.setText(Utils.brToNewLine(item.getDateString()));
        tvLink.setText(Utils.brToNewLine(item.getLink()));
        tvCoord.setText(Utils.brToNewLine(item.getGeorss()));

        // Set description if it exists, otherwise hide the labels
        if (item.getDescription() != "") {
            tvDesc.setText(Utils.brToNewLine(item.getDescription()));
        } else {
            TextView tvDescLabel = (TextView) convertView.findViewById(R.id.txtDescriptionLabel);
            tvDesc.setVisibility(View.GONE); // Set visibility to off
            tvDescLabel.setVisibility(View.GONE);;
        }

        GradientDrawable gdHighlight = (GradientDrawable) viewHighlight.getBackground();


        // Set the start/end date only if they exist, otherwise hide the labels
        if (item.getStartDate() != null || item.getEndDate() != null) {
            tvDate.setVisibility(View.GONE);
            tvDateLabel.setVisibility(View.GONE);

            tvStartDate.setText(Utils.brToNewLine(item.getStartDateString()));
            tvEndDate.setText(Utils.brToNewLine(item.getEndDateString()));

            // Set traffic progress
            pbDateProgress.setProgress((int) item.getProgress());


            int length = Days.daysBetween(item.getStartDate(), item.getEndDate()).getDays();
            double progress = item.getProgress();
            int progressColour;

            // Get colour depending on percentage of progress and length
            if (length < 3 || progress > 95) progressColour =  Color.parseColor("#78bd00");     // Green
            else if (length < 7 || progress > 80) progressColour = Color.parseColor("#dfd500"); // Yellow
            else if (length < 14) progressColour = Color.parseColor("#df9d00");                 // Orange
            else progressColour = Color.parseColor("#d22a00");                                  // Red

            // Set progress bar colour
            pbDateProgress.getProgressDrawable().setColorFilter(
                    progressColour, android.graphics.PorterDuff.Mode.SRC_IN);

            // Set item feed_item_highlight colour
            gdHighlight.setColor(progressColour);

        } else {
            TextView tvEndDateLabel = (TextView) convertView.findViewById(R.id.txtEndDateLabel);
            tvStartDate.setVisibility(View.GONE); // Set visibility to off
            tvEndDate.setVisibility(View.GONE);
            tvEndDateLabel.setVisibility(View.GONE);
            pbDateProgress.setVisibility(View.GONE);

            // Show published date
            tvDate.setText(item.getDateString());

            // Set item feed_item_highlight colour
            gdHighlight.setColor(Color.parseColor("#df9d00"));      // Orange
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
