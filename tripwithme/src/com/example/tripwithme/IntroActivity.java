package com.example.tripwithme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class IntroActivity extends Activity {
	private Handler handler_intro;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introactivity);

		handler_intro=new Handler();
		handler_intro.postDelayed(run_intro, 500);
	}
	
	Runnable run_intro = new Runnable(){
		public void run(){
			Intent intent = new Intent(IntroActivity.this, MainActivity.class);
			startActivity(intent);
			finish();

			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	};

}
