package com.example.trafficscotlandproto.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.trafficscotlandproto.MainActivity;
import com.example.trafficscotlandproto.R;


public class AmbientModeFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Get views
        View view = inflater.inflate(R.layout.fragment_ambient_mode, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imgHeartbeat);
;
        // Get number of traffic reports
        int noOfPlanned = MainActivity.rssPlanned.getLength();
        int noOfRoadworks = MainActivity.rssRoadworks.getLength();
        int noOfIncidents = MainActivity.rssIncidents.getLength();
        int totalTraffic = noOfIncidents + noOfPlanned + noOfRoadworks;

        // Show a different colour of heart depending on no of reports
        int resource;
        if (totalTraffic > 300) resource = R.drawable.heart_rage;
        else if (totalTraffic > 150) resource = R.drawable.heart_stress;
        else if (totalTraffic > 50) resource = R.drawable.heart_mild;
        else resource = R.drawable.heart_calm;

//        R.anim.pulse_heart

        // Set resource and start animation
        imageView.setBackgroundResource(resource);
        imageView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.pulse_heart));

        // Add items to view and create dialog
        builder.setView(view);
        return builder.create();
    }
}
