package com.example.trafficscotlandproto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        TextView tvLink = (TextView) convertView.findViewById(R.id.txtLink);
        TextView tvCoord = (TextView) convertView.findViewById(R.id.txtCoord);
        TextView tvStartDate = (TextView) convertView.findViewById(R.id.txtStartDate);
        TextView tvEndDate = (TextView) convertView.findViewById(R.id.txtEndDate);

        // Populate the data into the template view using the data object
        tvTitle.setText(Utils.brToNewLine(item.getTitle()));
        tvDesc.setText(Utils.brToNewLine(item.getDescription()));
        tvDate.setText(Utils.brToNewLine(item.getDateString()));
        tvLink.setText(Utils.brToNewLine(item.getLink()));
        tvCoord.setText(Utils.brToNewLine(item.getGeorss()));

        if (item.getDescription() != "") {
            tvDesc.setText(Utils.brToNewLine(item.getDescription()));
        } else {
            TextView tvDescLabel = (TextView) convertView.findViewById(R.id.txtDescriptionLabel);

            tvDesc.setVisibility(View.GONE);
            tvDescLabel.setVisibility(View.GONE);;
        }

        if (item.getStartDate() != null || item.getEndDate() != null) {
            tvStartDate.setText(Utils.brToNewLine(item.getStartDateString()));
            tvEndDate.setText(Utils.brToNewLine(item.getEndDateString()));
        } else {
            TextView tvStartDateLabel = (TextView) convertView.findViewById(R.id.txtStartDateLabel);
            TextView tvEndDateLabel = (TextView) convertView.findViewById(R.id.txtEndDateLabel);

            tvStartDate.setVisibility(View.GONE);
            tvStartDateLabel.setVisibility(View.GONE);
            tvEndDate.setVisibility(View.GONE);
            tvEndDateLabel.setVisibility(View.GONE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
