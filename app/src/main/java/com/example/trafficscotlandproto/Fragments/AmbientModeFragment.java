package com.example.trafficscotlandproto.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trafficscotlandproto.MainActivity;
import com.example.trafficscotlandproto.R;


// Ryan Sharp - S1517442

public class AmbientModeFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Get views
        View view = inflater.inflate(R.layout.fragment_ambient_mode, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imgHeartbeat);
        TextView tvOutput = (TextView) view.findViewById(R.id.txtOutput);
;
        // Get number of traffic reports
        int noOfRoadworks = MainActivity.rssRoadworks.getLength();
        int noOfIncidents = MainActivity.rssIncidents.getLength();
        int totalTraffic = noOfIncidents + noOfRoadworks;

        // Show a different colour of heart depending on no of reports
        int resource;
        int animation = R.anim.pulse_heart;
        String output = Integer.toString(totalTraffic) + " total reports\n";

        if (totalTraffic > 300) {
            resource = R.drawable.heart_rage;
            animation = R.anim.pulse_rage;
            output += getString(R.string.msgRage);
        } else if (totalTraffic > 150) {
            resource = R.drawable.heart_stress;
            animation = R.anim.pulse_stress;
            output += getString(R.string.msgStress);
        } else if (totalTraffic > 50) {
            resource = R.drawable.heart_mild;
            animation = R.anim.pulse_mild;
            output += getString(R.string.msgMild);
        } else {
            resource = R.drawable.heart_calm;
            animation = R.anim.pulse_calm;
            output += getString(R.string.msgCalm);
        }

        // Set resource and start animation
        imageView.setBackgroundResource(resource);
        imageView.startAnimation(AnimationUtils.loadAnimation(getActivity(), animation));

        tvOutput.setText(output);

        // Add items to view and create dialog
        builder.setView(view);
        return builder.create();
    }
}
