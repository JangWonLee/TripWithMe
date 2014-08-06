package com.example.tripwithme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class SearchActivity extends Activity{
	
	private Typeface mFont;
	private EditText searchEdit;
	private TextView selectCityText;
	private TextView orText;
	private TextView searchCityText;
	private Intent intent;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        mFont = Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");  
        searchEdit = (EditText)findViewById(R.id.searchedit);
        selectCityText = (TextView)findViewById(R.id.selectcitytext);
        orText = (TextView)findViewById(R.id.ortext);
        searchCityText = (TextView)findViewById(R.id.searchcitytext);
        
        selectCityText.setTypeface(mFont);
        orText.setTypeface(mFont);
        searchCityText.setTypeface(mFont);
        searchEdit.setTypeface(mFont);
        searchEdit.setHint("Search");
    }
	
	public void mOnClick(View v) {
    	intent = new Intent(this, MenuActivity.class);
    	startActivity(intent);
    }	
}
	

