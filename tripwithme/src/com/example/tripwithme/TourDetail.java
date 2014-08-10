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

public class TourDetail extends Activity {

	private String name;
	private String intro;
	private String time;
	private String tel;

	private TextView listDetailTitleText;
	private TextView introText;
	private TextView timeText;
	private TextView telephoneText;
	private Double latitude;
	private Double longitude;

	private Button locationButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourlistlistdetail);		      

		locationButton = (Button)findViewById(R.id.locationbutton);
		
		name = getIntent().getExtras().getString("name");
		intro = getIntent().getExtras().getString("intro");
		time = getIntent().getExtras().getString("time");
		tel = getIntent().getExtras().getString("tel");
		latitude = getIntent().getExtras().getDouble("latitude");
		longitude = getIntent().getExtras().getDouble("longitude");


		listDetailTitleText = (TextView)findViewById(R.id.tourlistlistdetailtitletext);
		introText=(TextView)findViewById(R.id.tourlistintrotext);
		timeText=(TextView)findViewById(R.id.tourlisttimetext);
		telephoneText=(TextView)findViewById(R.id.tourlisttelephonetext);

		listDetailTitleText.setText(name);
		introText.setText(intro);
		timeText.setText(time);
		telephoneText.setText(tel);
	}
	
	public void mOnClick(View v){
		Intent intent = new Intent(this, Map.class);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		startActivity(intent);

	}
}

