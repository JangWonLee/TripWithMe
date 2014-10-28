package kr.hi.mapmapkorea;

import kr.hi.mapmapkorea.util.ViewHelper;

import com.example.tripwithme.R;

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

public class RestaurantDetailActivity extends Activity {
	private ViewHelper mViewHelper;
	
	private String name;
	private String intro;
	private String time;
	private String tel;
	private String menu1;
	private String menu2;
	private String menu3;
	private Double latitude;
	private Double longitude;

	private TextView listDetailTitleText;
	private TextView introText;
	private TextView timeText;
	private TextView telephoneText;
	private TextView listDetailMenuText1;
	private TextView listDetailMenuText2;
	private TextView listDetailMenuText3;
	
	private Typeface mFont;
	private Typeface jFont;
	private Typeface kFont;

	private Button locationButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurantlistdetail);
		
		mViewHelper = new ViewHelper(this);
		View retaurantdetailLayout = findViewById(R.id.retaurantdetail_layout);
		mViewHelper.setGlobalSize((ViewGroup) retaurantdetailLayout);
		
		
        jFont=Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
        mFont=Typeface.createFromAsset(getAssets(), "fonts/Anysome Italic.otf");
        kFont=Typeface.createFromAsset(getAssets(), "fonts/dearJoe 6 TRIAL.otf");


		locationButton = (Button) findViewById(R.id.restourantlocationbutton);

		name = getIntent().getExtras().getString("name");
		intro = getIntent().getExtras().getString("intro");
		time = getIntent().getExtras().getString("time");
		tel = getIntent().getExtras().getString("tel");
		menu1 = getIntent().getExtras().getString("menu1");
		menu2 = getIntent().getExtras().getString("menu2");
		menu3 = getIntent().getExtras().getString("menu3");
		latitude = getIntent().getExtras().getDouble("latitude");
		longitude = getIntent().getExtras().getDouble("longitude");

		listDetailTitleText = (TextView) findViewById(R.id.restourantlistdetailtitletext);
		introText = (TextView) findViewById(R.id.restourantintrotext);
		timeText = (TextView) findViewById(R.id.restouranttimetext);
		telephoneText = (TextView) findViewById(R.id.restouranttelephonetext);
		listDetailMenuText1 = (TextView) findViewById(R.id.restourantlistdetailmenu1text);
		listDetailMenuText2 = (TextView) findViewById(R.id.restourantlistdetailmenu2text);
		listDetailMenuText3 = (TextView) findViewById(R.id.restourantlistdetailmenu3text);

		listDetailTitleText.setText(name);
		introText.setText(intro);
		timeText.setText(time);
		telephoneText.setText(tel);
		listDetailMenuText1.setText(menu1);
		listDetailMenuText2.setText(menu2);
		listDetailMenuText3.setText(menu3);
		
		listDetailTitleText.setTypeface(mFont);
		introText.setTypeface(mFont);
		timeText.setTypeface(mFont);
		telephoneText.setTypeface(mFont);
		listDetailMenuText1.setTypeface(mFont);
		listDetailMenuText2.setTypeface(mFont);
		listDetailMenuText3.setTypeface(mFont);
		
		
	}

	public void mOnClick(View v) {
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		startActivity(intent);

	}
}
