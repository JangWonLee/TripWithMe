package kr.hi.mapmapkorea;

import com.example.tripwithme.R;

import kr.hi.mapmapkorea.util.ViewHelper;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TourDetailActivity extends Activity {
	private ViewHelper mViewHelper;

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
	
	private Typeface mFont;
	private Typeface jFont;
	private Typeface kFont;
	private Integer cityNumber;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourlistlistdetail);
		
        jFont=Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
        mFont=Typeface.createFromAsset(getAssets(), "fonts/Anysome Italic.otf");
        kFont=Typeface.createFromAsset(getAssets(), "fonts/dearJoe 6 TRIAL.otf");
		mViewHelper = new ViewHelper(this);
		View tourdetailLayout = findViewById(R.id.tourdetail_list);
		mViewHelper.setGlobalSize((ViewGroup) tourdetailLayout);
		
		locationButton = (Button) findViewById(R.id.locationbutton);

		name = getIntent().getExtras().getString("name");
		intro = getIntent().getExtras().getString("intro");
		time = getIntent().getExtras().getString("time");
		tel = getIntent().getExtras().getString("tel");
		latitude = getIntent().getExtras().getDouble("latitude");
		longitude = getIntent().getExtras().getDouble("longitude");
		cityNumber = getIntent().getIntExtra("TourToDetailActivity", 9);

		listDetailTitleText = (TextView) findViewById(R.id.tourlistlistdetailtitletext);
		introText = (TextView) findViewById(R.id.tourlistintrotext);
		timeText = (TextView) findViewById(R.id.tourlisttimetext);
		telephoneText = (TextView) findViewById(R.id.tourlisttelephonetext);

		listDetailTitleText.setText(name);
		introText.setText(intro);
		timeText.setText(time);
		telephoneText.setText(tel);
		
		listDetailTitleText.setTypeface(mFont);
		introText.setTypeface(mFont);
		timeText.setTypeface(mFont);
		telephoneText.setTypeface(mFont);
		
	}

	public void mOnClick(View v) {
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		intent.putExtra("CityToMapActivity", cityNumber);
		startActivity(intent);
	}
}
