package kr.hi.mapmapkorea;

import java.io.File;
import java.util.ArrayList;

import kr.hi.mapmapkorea.util.ViewHelper;

import com.example.tripwithme.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
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

public class SearchActivity extends Activity {
	private ViewHelper mViewHelper;

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

	private Typeface jFont;
	private Typeface kFont;
	private File seoul;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		mViewHelper = new ViewHelper(this);
		View searchLayout = findViewById(R.id.search_layout);
		mViewHelper.setGlobalSize((ViewGroup) searchLayout);

		
        jFont=Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
        mFont=Typeface.createFromAsset(getAssets(), "fonts/Anysome Italic.otf");
        kFont=Typeface.createFromAsset(getAssets(), "fonts/dearJoe 6 TRIAL.otf");

		selectCityText = (TextView) findViewById(R.id.selectcitytext);
		orText = (TextView) findViewById(R.id.ortext);
		searchCityText = (TextView) findViewById(R.id.searchcitytext);
		autoEdit = (AutoCompleteTextView) findViewById(R.id.autoedit);
		searchButton = (Button) findViewById(R.id.searchbutton);
		seoulButton = (Button) findViewById(R.id.seoulbutton);

		selectCityText.setTypeface(kFont);
		orText.setTypeface(kFont);
		searchCityText.setTypeface(kFont);

		list = new ArrayList<String>();
		list.add("Seoul");
		list.add("Busan");
		list.add("Jeonju");
		list.add("Jeju");
		list.add("Incheon");
		list.add("Naju");
		
		autoEdit.setTypeface(kFont);
		searchButton.setTypeface(jFont);
		seoulButton.setTypeface(mFont);
		
		
		seoul = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Seoul.sqlitedb");
		
		if(seoul.exists()) {
			seoulButton.setVisibility(View.VISIBLE);
		}

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, list);

		autoEdit.setAdapter(adapter);

	}

	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.searchbutton:
			String msg = autoEdit.getText().toString();
			intent = new Intent(this, DownloadActivity.class);
			intent.putExtra("msg", msg);
			startActivity(intent);
			break;

		case R.id.seoulbutton:
			if (seoul.exists()) {
				intent = new Intent(this, MenuActivity.class);
				startActivity(intent);
				break;
			} else {
				Toast.makeText(SearchActivity.this, "No map file, Please Download first", Toast.LENGTH_LONG).show();
			}
		}
	}
}
