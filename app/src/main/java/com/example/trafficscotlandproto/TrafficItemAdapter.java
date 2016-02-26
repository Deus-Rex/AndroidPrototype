package com.example.trafficscotlandproto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ryan on 26/02/16.
 */
public class TrafficItemAdapter extends ArrayAdapter<TrafficItem> {

    public TrafficItemAdapter(Context context, ArrayList<TrafficItem> trafficItem) {
        super(context, 0, trafficItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TrafficItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.txtDescription);
        TextView tvDate = (TextView) convertView.findViewById(R.id.txtDate);
        TextView tvLink = (TextView) convertView.findViewById(R.id.txtLink);
        TextView tvCoord = (TextView) convertView.findViewById(R.id.txtCoord);
        // Populate the data into the template view using the data object
        tvTitle.setText(item.getTitle());
        tvDescription.setText(item.getDescription());
        tvDate.setText(item.getDate());
        tvLink.setText(item.getLink());
        tvCoord.setText(item.getGeorss());
        // Return the completed view to render on screen
        return convertView;
    }
}
