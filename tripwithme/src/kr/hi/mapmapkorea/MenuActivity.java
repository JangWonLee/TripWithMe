package kr.hi.mapmapkorea;

import kr.hi.mapmapkorea.util.ViewHelper;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tripwithme.R;

public class MenuActivity extends Activity {
	private ViewHelper mViewHelper;
	
	private Button mapButton;
	private Button restaurantButton;
	private Button tourButton;
	private Typeface mFont;
	private Typeface jFont;
	private Typeface kFont;
	private TextView selectText;
	
	private Integer cityNumber;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
        jFont=Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
        mFont=Typeface.createFromAsset(getAssets(), "fonts/Anysome Italic.otf");
        kFont=Typeface.createFromAsset(getAssets(), "fonts/dearJoe 6 TRIAL.otf");

		mViewHelper = new ViewHelper(this);
		View menuLayout = findViewById(R.id.menu_layout);
		mViewHelper.setGlobalSize((ViewGroup) menuLayout);
		
		mapButton = (Button) findViewById(R.id.mapbutton);
		restaurantButton = (Button) findViewById(R.id.restaurantbutton);
		tourButton = (Button) findViewById(R.id.tourbutton);
		selectText = (TextView)findViewById(R.id.selectmenu2);
		
		selectText.setTypeface(kFont);
		
		Log.i("Menu", getIntent().getIntExtra("CityToMenuActivity", 9) + "");
		cityNumber = getIntent().getIntExtra("CityToMenuActivity", 9);
		
	}

	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.mapbutton:
			Intent intent = new Intent(this, MapActivity.class);
			intent.putExtra("latitude", (double) 0);
			intent.putExtra("longitude", (double) 0);
			intent.putExtra("CityToMapActivity", cityNumber);
			startActivity(intent);
			break;
		case R.id.restaurantbutton:
			Intent intent2 = new Intent(this, RestaurantActivity.class);
			intent2.putExtra("CityToRestaurantActivity", cityNumber);
			startActivity(intent2);
			break;
		case R.id.tourbutton:
			Intent intent3 = new Intent(this, TourActivity.class);
			intent3.putExtra("CityToTourActivity", cityNumber);
			startActivity(intent3);
			break;
		}
	}
}