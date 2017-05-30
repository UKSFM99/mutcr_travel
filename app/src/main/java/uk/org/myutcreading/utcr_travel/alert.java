package uk.org.myutcreading.utcr_travel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ashcon on 5/17/17.
 */

public class alert extends Fragment{
    public static View myView;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String text="";
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyy");
        String time_str = time.format(new Date());
        String date_str = date.format(new Date());
        if(OverlayActivity.warn_type==0) {
            myView = inflater.inflate(R.layout.alert_info, container, false);
        }
        if(OverlayActivity.warn_type==1) {
            myView = inflater.inflate(R.layout.alert_fire, container, false);
            text=("A fire alert was issued by UTC Reading staff at "+time_str+" on the "+date_str+
                    "\n\nPlease evacuate the building immediately, and meet with your tutor at the fire assembly point (outside the science block).");
        }
        if(OverlayActivity.warn_type==2) {
            myView = inflater.inflate(R.layout.alert_lock, container, false);
            text=("An lockdown alert was issued by UTC Reading staff at "+time_str+" on the "+date_str+"\n\n"+
                    "Please close (and if possible lock) all doors and windows and find a place to hide. If you are in room with others, take a vote to barricade  the door with large objects (such as tables or chairs). " +
                    "During the lockdown, we advise you to listen out for instructions from the authorities or UTC Reading staff.");
        }
        TextView header = (TextView) myView.findViewById(R.id.header);
        TextView subheader = (TextView) myView.findViewById(R.id.subheader);
        TextView dismiss = (TextView) myView.findViewById(R.id.dismiss);
        TextView body = (TextView) myView.findViewById(R.id.text);
        Typeface bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Bold.otf");
        Typeface med = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Regular.otf");
        Typeface norm = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Light.otf");
        header.setTypeface(norm);
        dismiss.setTypeface(norm);
        subheader.setTypeface(norm);
        body.setTypeface(norm);
        body.setText(text);
        FloatingActionButton unlock = (FloatingActionButton) myView.findViewById(R.id.unlock);
        unlock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Have you read this message?")
                        .setTitle("Caution");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), OverlayService.class);
                        getActivity().stopService(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return myView;
    }
}
