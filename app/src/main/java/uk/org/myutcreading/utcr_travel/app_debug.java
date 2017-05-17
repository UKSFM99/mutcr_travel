package uk.org.myutcreading.utcr_travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by ashcon on 5/17/17.
 */

public class app_debug extends Fragment{
    public static View myView;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.app_debug, container, false);
        Button info = (Button) myView.findViewById(R.id.alert0);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverlayActivity.warn_type=0;
                launchOverlayService();
            }
        });
        Button fire = (Button) myView.findViewById(R.id.alert1);
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverlayActivity.warn_type=1;
                launchOverlayService();
            }
        });
        Button lockdown = (Button) myView.findViewById(R.id.alert2);
        lockdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverlayActivity.warn_type=2;
                launchOverlayService();
            }
        });
        return myView;
    }
    private void launchOverlayService() {
        Intent intent = new Intent(getContext(), OverlayService.class);
        getContext().startService(intent);
    }
}
