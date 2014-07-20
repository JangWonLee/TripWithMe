package com.example.tripwithme;

import android.support.v4.app.Fragment;
import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.os.Build;

public class tourDetail extends Activity {
	   TourDBHelper mHelper;
	   String name;
	   String intro;
	   String time;
	   String tel;
	   String menu;
	   TextView nameText;
	   TextView introText;
	   TextView telText;
	   TextView menuText;
	   
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.restdetail);		      
		      
		      name = getIntent().getExtras().getString("name");
		      intro = getIntent().getExtras().getString("intro");
		      tel = getIntent().getExtras().getString("tel");
		      menu = getIntent().getExtras().getString("menu");
		      		   
		     
		      nameText = (TextView)findViewById(R.id.name2);
		      introText=(TextView)findViewById(R.id.intro);
		      telText=(TextView)findViewById(R.id.tel);
		      menuText=(TextView)findViewById(R.id.menu3);
		      
		      nameText.setText(name);
		      introText.setText(intro);
		      telText.setText(tel);
		      menuText.setText(menu);
		      

	   }

}

  