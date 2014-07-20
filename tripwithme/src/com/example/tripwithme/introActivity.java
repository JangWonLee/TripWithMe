package com.example.tripwithme;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class introActivity extends Activity {
	Handler handler_intro;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introactivity);
		
		handler_intro=new Handler();
		handler_intro.postDelayed(run_intro, 2500);
}
	Runnable run_intro = new Runnable(){
		public void run(){
			Intent intent = new Intent(introActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	};
	
}
