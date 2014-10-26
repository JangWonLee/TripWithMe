package kr.hi.mapmapkorea;


import kr.hi.mapmapkorea.util.ViewHelper;

import com.example.tripwithme.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;


public class IntroActivity extends Activity {
	private ViewHelper mViewHelper;
	private Handler handler_intro;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		mViewHelper = new ViewHelper(this);
		View introLayout = findViewById(R.id.intro_layout);
		mViewHelper.setGlobalSize((ViewGroup) introLayout);

		handler_intro=new Handler();
		handler_intro.postDelayed(run_intro, 2500);
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
