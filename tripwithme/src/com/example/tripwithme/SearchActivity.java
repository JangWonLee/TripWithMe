package com.example.tripwithme;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.widget.TextView.OnEditorActionListener;
import android.widget.*;




public class SearchActivity extends Activity{
	
	private Typeface mFont;
	private TextView autoText;
	private TextView selectCityText;
	private TextView orText;
	private TextView searchCityText;
	private Intent intent;
	private Button searchButton;
	private Button seoulButton;
	
	private AutoCompleteTextView autoEdit;
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        mFont = Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");  
        selectCityText = (TextView)findViewById(R.id.selectcitytext);
        orText = (TextView)findViewById(R.id.ortext);
        searchCityText = (TextView)findViewById(R.id.searchcitytext);
        autoEdit = (AutoCompleteTextView)findViewById(R.id.autoedit);
        searchButton = (Button)findViewById(R.id.searchbutton);
        seoulButton = (Button)findViewById(R.id.seoulbutton);
        
        selectCityText.setTypeface(mFont);
        orText.setTypeface(mFont);
        searchCityText.setTypeface(mFont);
        
        list = new ArrayList<String>();
        list.add("Seoul");
        list.add("Busan");
        list.add("Jeonju");
        list.add("Jeju");
        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
        
        autoEdit.setAdapter(adapter);
             
        
   	}
	
	
	public void mOnClick(View v) {
		switch(v.getId()) {
		case R.id.searchbutton:
			String msg = autoEdit.getText().toString();
			intent = new Intent(this, DownloadActivity.class);
			startActivity(intent);
			break;
			
		case R.id.seoulbutton:
			intent = new Intent(this, MenuActivity.class);
			startActivity(intent);
			break;
		}	
	}
}
	

