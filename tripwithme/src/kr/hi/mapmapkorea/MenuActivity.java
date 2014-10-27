package kr.hi.mapmapkorea;

import kr.hi.mapmapkorea.util.ViewHelper;

import com.example.tripwithme.R;

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

public class MenuActivity extends Activity {
	private ViewHelper mViewHelper;
	
	private Button mapButton;
	private Button restaurantButton;
	private Button tourButton;
	private Typeface mFont;
	private Typeface jFont;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		
	    jFont=Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
	    mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");

		mViewHelper = new ViewHelper(this);
		View menuLayout = findViewById(R.id.menu_layout);
		mViewHelper.setGlobalSize((ViewGroup) menuLayout);
		
		mapButton = (Button) findViewById(R.id.mapbutton);
		restaurantButton = (Button) findViewById(R.id.restaurantbutton);
		tourButton = (Button) findViewById(R.id.tourbutton);
	}

	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.mapbutton:
			Intent intent = new Intent(this, MapActivity.class);
			intent.putExtra("latitude", (double) 0);
			intent.putExtra("longitude", (double) 0);

			startActivity(intent);
			break;
		case R.id.restaurantbutton:
			Intent intent2 = new Intent(this, RestaurantActivity.class);
			startActivity(intent2);
			break;
		case R.id.tourbutton:
			Intent intent3 = new Intent(this, TourActivity.class);
			startActivity(intent3);
			break;
		}
	}
}