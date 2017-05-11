package uk.org.myutcreading.utcr_travel;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import static uk.org.myutcreading.utcr_travel.R.id.map;

/**
 * Created by ashcon on 5/10/17.
 */

public class app_maps extends Fragment{
    public static View myView;
    public static MapView mMapView;
    public static LatLngBounds bounds;
    public static int marker_id=0;
    public static ArrayList<Integer> marker_ids =new ArrayList<>();
    public static LatLngBounds.Builder builder;
    public static GoogleMap googleMap;
    public static TextView debug;
    public static boolean completed = false;
    public static ArrayList<String> BusID = new ArrayList<>();
    public static ArrayList<String> service = new ArrayList<>();
    public static ArrayList<String> Last_updated = new ArrayList<>();
    public static ArrayList<Double> location_long = new ArrayList<>();
    public static ArrayList<Double> location_lat = new ArrayList<>();
    public static ArrayList<String> bearing = new ArrayList<>();
    public static ProgressDialog progress;
    public static Timer timer;
    public static int runs=0;
    public static Context mContext;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        update_map_auto();
        mContext = getContext();
        progress = new ProgressDialog(getContext());
        progress.setMessage("Downloading, please wait");
        progress.show();
        myView = inflater.inflate(R.layout.app_maps, container, false);
        mMapView = (MapView) myView.findViewById(map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        final Button button_setting = (Button) myView.findViewById(R.id.options);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(getContext(), map_settings.class);
                startActivity(setting_intent);
            }
        });
        return myView;
    }
    public void update_map_auto() {
        timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {
                try{

                    new app_http().execute("http://siri.buscms.com/reading/vehicles.json");

                }
                catch (Exception e) {
                }

            }
        }, 0, 5000);
    }
    public int decode_marker(SharedPreferences prefs, GoogleMap gmap){
        builder = new LatLngBounds.Builder();
        boolean four = prefs.getBoolean("pref4", true);
        boolean two = prefs.getBoolean("pref2", true);
        boolean seventeen = prefs.getBoolean("pref17", true);
        boolean not_service = prefs.getBoolean("prefnotinservice", true);
        boolean twenty1_5 = prefs.getBoolean("pref21_5",true);
        boolean thirty3 = prefs.getBoolean("pref33",true);
        boolean orange = prefs.getBoolean("preforange",true);
        marker_ids.clear();
        marker_id = 0;
        try {
            for (int i = 0; i < BusID.size(); i++) {
                String colour;
                colour = "#ffffff";
                if(four && (service.get(i).contains("X4")|| service.get(i).equals("4"))){
                    colour = "#FAD407";
                }
                else if(seventeen && service.get(i).contains("17")){
                    colour = "#8a2be2";
                }
                else if(two && (service.get(i).equals("2")||service.get(i).equals("2a"))){
                    colour = "#00FF00";
                }
                else if(thirty3 &&(service.get(i).equals("33")||service.get(i).equals("33a"))) {
                    colour = "#00008b";
                }
                else{
                    continue;
                }
                //"bearing="+bearing.get(i)+" bus no="+BusID.get(i)+"\n Last Updated at "+Last_updated.get(i)
                drawMarker(new LatLng(location_lat.get(i), location_long.get(i)), service.get(i),colour,i,gmap);
                if (runs <= 2) {
                    try {
                        bounds = builder.build();
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                        gmap.animateCamera(cu);
                    } catch(Exception ignored){
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        runs++;
        return 0;
    }
    private void drawMarker(LatLng point, String text, final String colour, int id, GoogleMap gmap) {
        Marker temp;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(point)
                .icon(getMarkerIcon(colour));
        temp = gmap.addMarker(markerOptions);
        temp.setTag(id);
        builder.include(markerOptions.getPosition());
    }
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
        if(timer!=null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(timer!=null) {
            timer.cancel();
            timer = null;
        }
        mMapView.invalidate();
    }
    @Override
    public void onResume() {
        mContext = getContext();
        Toast.makeText(getContext(), "updating every 10 seconds", Toast.LENGTH_SHORT).show();
        mMapView.invalidate();
        super.onResume();
        Log.e("EE","found number of entries:"+BusID.size());
        mMapView.invalidate();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                debug = (TextView) myView.findViewById(R.id.debug);
                try {
                    googleMap.clear();
                } catch (Exception e) {
                }
                try {
                    googleMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    Toast.makeText(getContext(), "Cannot show current location", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                SharedPreferences prefs = getContext().getSharedPreferences(getContext().getPackageName() + "_preferences", Context.MODE_PRIVATE);
                decode_marker(prefs,googleMap);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        int position = (int)(marker.getTag());
                        try {
                            String colour="#ffffff";
                            String services = service.get(position);
                            if(services.equals("X4")||services.equals("4")){
                                colour = "#FAD407";
                            }
                            Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.content_frame), "Bus service:" + service.get(position) + " heading:" + bearing.get(position) + "\nLast updated at: " + Last_updated.get(position) + " GMT", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setActionTextColor(Color.YELLOW);
                            snackbar.show();
                        }
                        catch (Exception ignored){
                        }
                            return false;
                    }
                });
            }
        });
    }
}


