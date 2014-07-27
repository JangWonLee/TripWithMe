package com.example.tripwithme;

import android.app.Activity;
import android.app.Fragment;
import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.os.Build;

public class menu extends Activity{
    
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Button mapbtn = (Button)findViewById(R.id.MAP);
        Button resbtn = (Button)findViewById(R.id.RESTAURENT);
        Button tourbtn = (Button)findViewById(R.id.TOUR);
        
        
    }
	
	
	public void mOnClick(View v) {
		switch(v.getId()) {
		case R.id.MAP :
			Intent intent = new Intent(this, map.class);
			startActivity(intent);
			break;
		case R.id.RESTAURENT :
			Intent intent2 = new Intent(this, Restaurant.class);
			startActivity(intent2);
			break;
		case R.id.TOUR :
			Intent intent3 = new Intent(this, Tour.class);
			startActivity(intent3);
			break;
		}
	}
}
	

