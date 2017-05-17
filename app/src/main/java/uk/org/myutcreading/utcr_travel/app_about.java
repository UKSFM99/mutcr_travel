package uk.org.myutcreading.utcr_travel;

/**
 * Created by ashcon on 5/14/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static uk.org.myutcreading.utcr_travel.app_maps.timer;

/**
 * Created by ashcon on 5/10/17.
 */

public class app_about extends Fragment {
    public static View myView;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.app_about, container, false);
        Button btnShowOverlay = (Button) myView.findViewById(R.id.enable);
        btnShowOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



