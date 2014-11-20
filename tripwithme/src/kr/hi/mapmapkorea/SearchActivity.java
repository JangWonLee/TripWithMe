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
	private Button searchButton;
	private Button cityMapButton1;
	private Button cityMapButton2;
	private Button cityMapButton3;

	private AutoCompleteTextView autoEdit;
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;

	private Typeface jFont;
	private Typeface kFont;
	private File seoul;
	private File busan;
	private File incheon;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		mViewHelper = new ViewHelper(this);
		View searchLayout = findViewById(R.id.search_layout);
		mViewHelper.setGlobalSize((ViewGroup) searchLayout);

		jFont = Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
		mFont = Typeface.createFromAsset(getAssets(),
				"fonts/Anysome Italic.otf");
		kFont = Typeface.createFromAsset(getAssets(),
				"fonts/dearJoe 6 TRIAL.otf");

		selectCityText = (TextView) findViewById(R.id.selectcitytext);
		orText = (TextView) findViewById(R.id.ortext);
		searchCityText = (TextView) findViewById(R.id.searchcitytext);
		autoEdit = (AutoCompleteTextView) findViewById(R.id.autoedit);
		searchButton = (Button) findViewById(R.id.searchbutton);
		cityMapButton1 = (Button) findViewById(R.id.citymapbutton1);
		cityMapButton2 = (Button) findViewById(R.id.citymapbutton2);
		cityMapButton3 = (Button) findViewById(R.id.citymapbutton3);

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
		cityMapButton1.setTypeface(mFont);
		cityMapButton2.setTypeface(mFont);
		cityMapButton3.setTypeface(mFont);

		seoul = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Seoul.sqlitedb");
		busan = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Busan.sqlitedb");
		incheon = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Incheon.sqlitedb");

		// init Offline Map
		File from = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download",
				"0B0vdbaa0j01ySkl5RzVIV1dtRzA.bin");
		File to = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Seoul.sqlitedb");
		if (from.exists())
			from.renameTo(to);
		File map = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Seoul.sqlitedb");
		if (!map.exists()) {
			Toast.makeText(this, "Please download map", Toast.LENGTH_SHORT)
					.show();
			this.finish();
		}

		if (seoul.exists()) {
			cityMapButton1.setVisibility(View.VISIBLE);
		}

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, list);

		autoEdit.setAdapter(adapter);

	}

	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.searchbutton:
			/**
			 * Intent값 서울:0 부산:1 인천:2
			 */
			String msg = autoEdit.getText().toString();
			if (msg.equals("Seoul") || msg.equals("seoul")
					|| msg.equals("SEOUL")) {
				Intent intent = new Intent(this, WebViews.class);
				intent.putExtra("City", 0);
				startActivity(intent);
			} else if (msg.equals("Busan") || msg.equals("busan")
					|| msg.equals("BUSAN")) {
				Intent intent = new Intent(this, WebViews.class);
				intent.putExtra("City", 1);
				startActivity(intent);
			} else if (msg.equals("Incheon") || msg.equals("incheon")
					|| msg.equals("INCHEON")) {
				Intent intent = new Intent(this, WebViews.class);
				intent.putExtra("City", 2);
				startActivity(intent);
			} else {
				Toast.makeText(SearchActivity.this,
						"Wrong City Name, Please Check again",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.citymapbutton1:
			if (seoul.exists()) {
				Intent intent = new Intent(this, MenuActivity.class);
				startActivity(intent);
				break;
			} else {
				Toast.makeText(SearchActivity.this,
						"No map file, Please Download first", Toast.LENGTH_LONG)
						.show();
			}
		case R.id.citymapbutton2:
			if (seoul.exists()) {
				Intent intent = new Intent(this, MenuActivity.class);
				startActivity(intent);
				break;
			} else {
				Toast.makeText(SearchActivity.this,
						"No map file, Please Download first", Toast.LENGTH_LONG)
						.show();
			}
		case R.id.citymapbutton3:
			if (seoul.exists()) {
				Intent intent = new Intent(this, MenuActivity.class);
				startActivity(intent);
				break;
			} else {
				Toast.makeText(SearchActivity.this,
						"No map file, Please Download first", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
}
