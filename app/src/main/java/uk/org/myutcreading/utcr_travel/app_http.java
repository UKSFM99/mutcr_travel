package uk.org.myutcreading.utcr_travel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import static java.lang.Double.parseDouble;
import static uk.org.myutcreading.utcr_travel.app_maps.BusID;
import static uk.org.myutcreading.utcr_travel.app_maps.Last_updated;
import static uk.org.myutcreading.utcr_travel.app_maps.bearing;
import static uk.org.myutcreading.utcr_travel.app_maps.googleMap;
import static uk.org.myutcreading.utcr_travel.app_maps.location_lat;
import static uk.org.myutcreading.utcr_travel.app_maps.location_long;
import static uk.org.myutcreading.utcr_travel.app_maps.progress;
import static uk.org.myutcreading.utcr_travel.app_maps.service;
import static uk.org.myutcreading.utcr_travel.app_maps.completed;
import static uk.org.myutcreading.utcr_travel.app_maps.mMapView;
import static uk.org.myutcreading.utcr_travel.app_maps.runs;
import static uk.org.myutcreading.utcr_travel.app_maps.mContext;
/**
 * Created by ashcon on 5/10/17.
 */

public class app_http extends AsyncTask<String, String, String> {
    @Override
    protected void onPreExecute() {
        service.clear();
        location_lat.clear();
        location_long.clear();
        BusID.clear();
        Last_updated.clear();
        bearing.clear();
    }
    private void fallback() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("sdcard/myutcreading/bus_location.txt"));
            String result = br.readLine();
            result = result.substring(3, result.length() - 3);
            String[] parts_temp = result.split("\\},\\{");
            for (int i = 0; i < parts_temp.length; i++) {
                System.out.println(parts_temp[i]);
                String[] temp_temp = parts_temp[i].split(",");
                temp_temp[1] = temp_temp[1].replace("vehicle", "");
                temp_temp[2] = temp_temp[2].replace("service", "");
                temp_temp[3] = temp_temp[3].replace("observed", "");
                temp_temp[4] = temp_temp[4].replace("latitude", "");
                temp_temp[5] = temp_temp[5].replace("longitude", "");
                temp_temp[6] = temp_temp[6].replace("bearing", "");
                for (int k = 1; k < temp_temp.length; k++) {
                    temp_temp[k] = temp_temp[k].substring(4, temp_temp[k].length() - 1);
                    if (temp_temp[2].isEmpty()) {
                        temp_temp[2] = "not in service";
                    }
                    if (temp_temp[6].isEmpty()) {
                        temp_temp[6] = "N/A";
                    }
                    System.out.println(temp_temp[k]);
                }
                BusID.add(temp_temp[1]);
                service.add(temp_temp[2]);
                Last_updated.add(temp_temp[3]);
                location_lat.add(parseDouble(temp_temp[4]));
                location_long.add(parseDouble(temp_temp[5]));
                bearing.add(temp_temp[6]);
            }
        }
        catch (Exception e){
        }
    }
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setConnectTimeout(5000);
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);
            }
            String result = buffer.toString();
            result = result.substring(3,result.length()-3);
            String[] parts_temp = result.split("\\},\\{");
            for(int i = 0; i < parts_temp.length; i++) {
                String[] temp_temp = parts_temp[i].split(",");
                temp_temp[1] = temp_temp[1].replace("vehicle", "");
                temp_temp[2] = temp_temp[2].replace("service", "");
                temp_temp[3] = temp_temp[3].replace("observed", "");
                temp_temp[4] = temp_temp[4].replace("latitude", "");
                temp_temp[5] = temp_temp[5].replace("longitude", "");
                temp_temp[6] = temp_temp[6].replace("bearing", "");
                for (int k = 1; k < temp_temp.length; k++) {
                    temp_temp[k] = temp_temp[k].substring(4, temp_temp[k].length() - 1);
                    if (temp_temp[2].isEmpty()) {
                        temp_temp[2] = "not in service";
                    }
                    if (temp_temp[6].isEmpty()) {
                        temp_temp[6] = "0";
                    }
                }
                BusID.add(temp_temp[1]);
                service.add(temp_temp[2]);
                Last_updated.add(temp_temp[3]);
                location_lat.add(parseDouble(temp_temp[4]));
                location_long.add(parseDouble(temp_temp[5]));
                bearing.add(temp_temp[6]);
            }
            Log.e("EE","number of entried found: "+BusID.size());
            progress.dismiss();
            try{
                file_handler.write_log_File(buffer.toString(),mContext);
            }
            catch (Exception e){
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fallback();
        } catch (IOException e) {
            e.printStackTrace();
            fallback();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                fallback();
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(String result) {
        completed = true;
        mMapView.invalidate();
        try{
            googleMap.clear();
        }
        catch (Exception e){
        }
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        int i = new app_maps().decode_marker(prefs,googleMap);

    }
}
