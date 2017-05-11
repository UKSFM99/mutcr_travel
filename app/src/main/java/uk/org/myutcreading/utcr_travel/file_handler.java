package uk.org.myutcreading.utcr_travel;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ashcon on 5/11/17.
 */

public class file_handler {
    public static void write_log_File(String append_str, Context mContext) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/myutcreading");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            //finished creating the folder
        } else {
            //didn't make the folder - because no permission or folder already exists on device
        }
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        File logFile = new File("sdcard/myutcreading/bus_location.txt");
        if (!logFile.exists()) {
            try{
                logFile.createNewFile();
            }
            catch (IOException Ignored) {
            }
        }
        try {
            //true to append to file rather than overwrite
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(append_str);
            buf.newLine();
            buf.close();
        } catch (IOException ignored) {
        }
    }
}
