package com.example.tripwithme;

import android.app.Activity;
import android.app.Fragment;
import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.os.Build;

public class search extends Activity{
	
	Typeface mFont;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");  
        EditText searchedit = (EditText)findViewById(R.id.editsearch);
        TextView selecttext = (TextView)findViewById(R.id.textselect);
        TextView ortext = (TextView)findViewById(R.id.textor);
        TextView searchtext = (TextView)findViewById(R.id.textsearch);
        
        selecttext.setTypeface(mFont);
        ortext.setTypeface(mFont);
        searchtext.setTypeface(mFont);
        searchedit.setTypeface(mFont);
    	searchedit.setHint("Search");
    }
	public void mOnClick(View v) {
    	Intent intent = new Intent(this, menu.class);
    	startActivity(intent);
    }	
}
	

