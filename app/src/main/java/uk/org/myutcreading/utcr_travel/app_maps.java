package uk.org.myutcreading.utcr_travel;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
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
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
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

public class app_maps extends Fragment {
    public static View myView;
    public static MapView mMapView;
    public static Bitmap[] icons = new Bitmap[100];
    public static LatLngBounds bounds;
    public static int marker_id = 0;
    public static ArrayList<Integer> marker_ids = new ArrayList<>();
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
    public static int runs = 0;
    public static Context mContext;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        if (timer == null) {
            update_map_auto();
            System.out.println("starting timer");
        }
        Toast.makeText(getContext(), "updating every 10 seconds", Toast.LENGTH_SHORT).show();
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
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    new app_http().execute("http://siri.buscms.com/reading/vehicles.json");
                } catch (Exception e) {
                }

            }
        }, 0, 5000);
    }

    public int decode_marker(SharedPreferences prefs, GoogleMap gmap) {
        builder = new LatLngBounds.Builder();
        boolean one = prefs.getBoolean("pref1", true);
        boolean two = prefs.getBoolean("pref2", true);
        boolean three = prefs.getBoolean("pref3", true);
        boolean four = prefs.getBoolean("pref4", true);
        boolean five = prefs.getBoolean("pref5", true);
        boolean six = prefs.getBoolean("pref6", true);
        boolean seven = prefs.getBoolean("pref7", true);
        boolean nine = prefs.getBoolean("pref9", true);
        boolean eleven = prefs.getBoolean("pref11", true);
        boolean twelve = prefs.getBoolean("pref12", true);
        boolean thirteen = prefs.getBoolean("pref13", true);
        boolean fourteen = prefs.getBoolean("pref14", true);
        boolean fifteen = prefs.getBoolean("pref15", true);
        boolean sixteen = prefs.getBoolean("pref16", true);
        boolean seventeen = prefs.getBoolean("pref17", true);
        boolean nineteen = prefs.getBoolean("pref19", true);
        boolean twentyone = prefs.getBoolean("pref21", true);
        boolean twentytwo = prefs.getBoolean("pref22", true);
        boolean twentythree = prefs.getBoolean("pref23", true);
        boolean twentyfour = prefs.getBoolean("pref24", true);
        boolean twentyfive = prefs.getBoolean("pref25", true);
        boolean twentysix = prefs.getBoolean("pref26", true);
        boolean twentyseven = prefs.getBoolean("pref27", true);
        boolean twentyeight = prefs.getBoolean("pref28", true);
        boolean twentynine = prefs.getBoolean("pref29", true);
        boolean thirtythree = prefs.getBoolean("pref33", true);
        boolean fifty = prefs.getBoolean("pref50", true);
        boolean fiftythree = prefs.getBoolean("pref53", true);
        boolean sixtey = prefs.getBoolean("pref60", true);
        boolean fivehundred = prefs.getBoolean("pref500", true);
        String line = "";
        int markerid = 0;
        int all = 0;
        try {
            for (int i = 0; i < BusID.size(); i++) {
                String colour;
                colour = "#ffffff";
                if (one && (service.get(i).equals("1"))) {
                    colour = "#000000";
                    markerid = 1;
                }
                else if (four && (service.get(i).contains("X4") || service.get(i).equals("4"))) {
                    colour = "#D6AE6E";
                    markerid = 4;
                } else if (seventeen && service.get(i).contains("17")) {
                    colour = "#4D2B81";
                    markerid = 17;
                } else if (two && (service.get(i).equals("2") || service.get(i).equals("2a"))) {
                    colour = "#6FCC58";
                    markerid = 2;
                } else if (three && (service.get(i).equals("3"))) {
                    colour = "#FEBD31";
                    markerid = 3;
                } else if (five && (service.get(i).equals("5"))) {
                    colour = "#187370";
                    markerid = 5;
                } else if (six && (service.get(i).equals("6") || service.get(i).equals("6a"))) {
                    colour = "#187370";
                    markerid = 6;
                } else if (seven && (service.get(i).equals("7"))) {
                    colour = "#C0C6C4";
                    markerid = 7;
                } else if (nine && (service.get(i).equals("9"))) {
                    colour = "#E80812";
                    markerid = 9;
                } else if (eleven && (service.get(i).equals("11"))) {
                    colour = "#D49749";
                    markerid = 11;
                } else if (twelve && (service.get(i).equals("12"))) {
                    colour = "#E0612F";
                    markerid = 12;
                } else if (thirteen && (service.get(i).equals("13"))) {
                    colour = "#E0612F";
                    markerid = 13;
                } else if (fourteen && (service.get(i).equals("14"))) {
                    colour = "#E0612F";
                    markerid = 14;
                } else if (fifteen && (service.get(i).equals("15"))) {
                    colour = "#099BE9";
                    markerid = 15;
                } else if (sixteen && (service.get(i).equals("16"))) {
                    colour = "#099BE9";
                    markerid = 16;
                } else if (nineteen && (service.get(i).equals("19a") || service.get(i).equals("19b") || service.get(i).equals("19c"))) {
                    colour = "#E0612F";
                    markerid = 19;
                } else if (twentyone && (service.get(i).equals("21"))) {
                    colour = "#8E1557";
                    markerid = 21;
                } else if (twentytwo && (service.get(i).equals("22"))) {
                    colour = "#A1166C";
                    markerid = 22;
                } else if (twentythree && (service.get(i).equals("23"))) {
                    colour = "#A1166C";
                    markerid = 23;
                } else if (twentyfour && (service.get(i).equals("24"))) {
                    colour = "#A1166C";
                    markerid = 24;
                } else if (twentyfive && (service.get(i).equals("25"))) {
                    colour = "#A1166C";
                    markerid = 25;
                } else if (twentysix && (service.get(i).equals("26"))) {
                    colour = "#FDCD1E";
                    markerid = 26;
                } else if (twentyseven && (service.get(i).equals("27"))) {
                    colour = "#AF2673";
                    markerid = 27;
                } else if (twentyeight && (service.get(i).equals("28"))) {
                    colour = "#45A433";
                    markerid = 28;
                } else if (twentynine && (service.get(i).equals("29"))) {
                    colour = "#AF2673";
                    markerid = 29;
                } else if (thirtythree && (service.get(i).equals("33")||service.get(i).equals("33a"))) {
                    colour = "#032470";
                    markerid = 33;
                } else if (fifty && (service.get(i).equals("50a")||service.get(i).equals("50"))) {
                    colour = "#032470";
                    markerid = 50;
                } else if (fiftythree && (service.get(i).equals("53a")||service.get(i).equals("53"))) {
                    colour = "#044040";
                    markerid = 53;
                } else if (sixtey && (service.get(i).equals("60c")||service.get(i).equals("60m")||service.get(i).equals("60"))) {
                    colour = "#044040";
                    markerid = 60;
                } else if (fivehundred && (service.get(i).equals("500")||service.get(i).equals("500a"))) {
                    colour = "#719CC3";
                    markerid = 99;
                }
                else{
                    continue;
                }
                drawMarker(new LatLng(location_lat.get(i), location_long.get(i)), colour,markerid,i, Float.parseFloat(bearing.get(i)), gmap);
                all++;
                if (runs <= 1) {
                    try {
                        bounds = builder.build();
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                        gmap.animateCamera(cu);
                    } catch (Exception ignored) {
                    }
                }
                debug.setText("Displaying " + all + " buses");
                if(all == 0){
                    Toast.makeText(getContext(),"There are no buses for this route running right now",Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        runs++;
        return 0;
    }
    private void drawMarker(LatLng point, final String colour, int id,int i,float rotation, GoogleMap gmap) {
        Marker temp;
        Bitmap bm;
        if(icons[id] != null){
            bm = icons[id];
        }
        else{
            bm = tintImage(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.marker), colour);
            icons[id] = bm;
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(point)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(bm));
        temp = gmap.addMarker(markerOptions);
        temp.setRotation(rotation);
        temp.setTag(i);
        builder.include(markerOptions.getPosition());
    }
    public static Bitmap tintImage(Bitmap bitmap, String color) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN));
        Bitmap bitmapResult = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapResult);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmapResult;
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
        if(timer!=null) {
            System.out.println("stopping timer");
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(timer!=null) {
            System.out.println("stopping timer");
            timer.cancel();
            timer = null;
        }
        mMapView.invalidate();
    }
    @Override
    public void onResume() {
        runs =0;
        if(timer==null) {
            System.out.println("starting timer");
            update_map_auto();
        }
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
                            Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.content_frame),
                                    "Route: "+service.get(position)+"Bus Number: "+BusID.get(position)+"\nLast updated at "+Last_updated.get(position), Snackbar.LENGTH_LONG);
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


