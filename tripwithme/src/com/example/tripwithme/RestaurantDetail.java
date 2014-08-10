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
	
	private String name;
	private String intro;
	private String time;
	private String tel;
	private String menu1;
	private String menu2;
	private String menu3;
	
	private TextView listDetailTitleText;
	private TextView introText;
	private TextView timeText;
	private TextView telephoneText;
	private TextView listDetailMenuText1;
	private TextView listDetailMenuText2;
	private TextView listDetailMenuText3;
	
	private Button locationButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restourantlistdetail);	

		locationButton = (Button)findViewById(R.id.restourantlocationbutton);
		
		name = getIntent().getExtras().getString("name");
		intro = getIntent().getExtras().getString("intro");
		time = getIntent().getExtras().getString("time");
		tel = getIntent().getExtras().getString("tel");
		menu1 = getIntent().getExtras().getString("menu1");
		menu2 = getIntent().getExtras().getString("menu2");
		menu3 = getIntent().getExtras().getString("menu3");


		listDetailTitleText = (TextView)findViewById(R.id.restourantlistdetailtitletext);
		introText=(TextView)findViewById(R.id.restourantintrotext);
		timeText=(TextView)findViewById(R.id.restouranttimetext);
		telephoneText=(TextView)findViewById(R.id.restouranttelephonetext);
		listDetailMenuText1=(TextView)findViewById(R.id.restourantlistdetailmenu1text);
		listDetailMenuText2=(TextView)findViewById(R.id.restourantlistdetailmenu2text);
		listDetailMenuText3=(TextView)findViewById(R.id.restourantlistdetailmenu3text);

		listDetailTitleText.setText(name);
		introText.setText(intro);
		timeText.setText(time);
		telephoneText.setText(tel);
		listDetailMenuText1.setText(menu1);
		listDetailMenuText2.setText(menu2);
		listDetailMenuText3.setText(menu3);
	}

	public void mOnClick(View v){
		Intent intent = new Intent(this, Map.class);
		startActivity(intent);

	}
}

