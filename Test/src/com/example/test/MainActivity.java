package com.example.test;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        byte[] data = null;
        
        
        Parse.initialize(this, "25rbla14anLrOIAdVsGQswtSUj57UQC4nbkUVGNu", "HqPFCPJur2YX81eXmWcavSxvOoDulC5QzOB4Adkf");
        
//        Path path
        File f = new File("c:\\Seoul.sqlitedb");
        FileInputStream fin = null;
        FileChannel ch = null;
        try {
            fin = new FileInputStream(f);
            ch = fin.getChannel();
            int size = (int) ch.size();
            MappedByteBuffer buf = ch.map(MapMode.READ_ONLY, 0, size);
            data = new byte[size];
            buf.get(data);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (ch != null) {
                    ch.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        ParseFile file = new ParseFile("map.sqlitedb", data);
        file.saveInBackground();
        
        ParseObject jobApplication = new ParseObject("JobApplication");
        jobApplication.put("applicantName", "Joe Smith");
        jobApplication.put("applicantResumeFile", file);
        jobApplication.saveInBackground();
//        
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("test1.txt", "/sdcard/text.txt");
//        testObject.saveInBackground();
    }

}
