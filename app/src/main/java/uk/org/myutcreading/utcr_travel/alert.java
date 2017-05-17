package uk.org.myutcreading.utcr_travel;

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

/**
 * Created by ashcon on 5/17/17.
 */

public class alert extends Fragment{
    public static View myView;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(OverlayActivity.warn_type==0) {
            myView = inflater.inflate(R.layout.alert_info, container, false);
        }
        if(OverlayActivity.warn_type==1) {
            myView = inflater.inflate(R.layout.alert_fire, container, false);
        }
        if(OverlayActivity.warn_type==2) {
            myView = inflater.inflate(R.layout.alert_lock, container, false);
        }
        TextView header = (TextView) myView.findViewById(R.id.header);
        TextView subheader = (TextView) myView.findViewById(R.id.subheader);
        Typeface bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Bold.otf");
        header.setTypeface(bold);
        subheader.setTypeface(bold);
        FloatingActionButton unlock = (FloatingActionButton) myView.findViewById(R.id.unlock);
        unlock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OverlayService.class);
                getActivity().stopService(intent);
                getActivity().finish();
            }
        });
        return myView;
    }
}
