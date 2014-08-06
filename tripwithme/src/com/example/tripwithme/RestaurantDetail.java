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

public class RestaurantDetail extends Activity {
	private RestaurantDBHelper mHelper;
	
	private String name;
	private String intro;
	private String time;
	private String tel;
	private String menu;
	
	private TextView listDetailTitleText;
	private TextView introText;
	private TextView timeText;
	private TextView telephoneText;
	private TextView listDetailMenuText;
	
	private Button locationButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listdetail);	

		locationButton = (Button)findViewById(R.id.locationbutton);
		
		name = getIntent().getExtras().getString("name");
		intro = getIntent().getExtras().getString("intro");
		time = getIntent().getExtras().getString("time");
		tel = getIntent().getExtras().getString("tel");
		menu = getIntent().getExtras().getString("menu");


		listDetailTitleText = (TextView)findViewById(R.id.listdetailtitletext);
		introText=(TextView)findViewById(R.id.introtext);
		timeText=(TextView)findViewById(R.id.timetext);
		telephoneText=(TextView)findViewById(R.id.telephonetext);
		listDetailMenuText=(TextView)findViewById(R.id.listdetailmenutext);

		listDetailTitleText.setText(name);
		introText.setText(intro);
		timeText.setText(time);
		telephoneText.setText(tel);
		listDetailMenuText.setText(menu);
	}

	public void mOnClick(View v){
		Intent intent = new Intent(this, Map.class);
		startActivity(intent);

	}
}

