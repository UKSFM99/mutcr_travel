package uk.org.myutcreading.utcr_travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by ashcon on 5/14/17.
 */

public class app_update extends Fragment {
    public static View myView;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.app_update, container, false);
        final Button button_setting = (Button) myView.findViewById(R.id.html);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/UKSFM99/mutcr_travel/releases"));
                startActivity(browserIntent);
            }
        });
        return myView;
    }
}
