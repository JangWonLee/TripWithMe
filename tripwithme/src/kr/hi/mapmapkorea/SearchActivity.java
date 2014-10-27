package kr.hi.mapmapkorea;

import java.util.ArrayList;

import kr.hi.mapmapkorea.util.ViewHelper;

import com.example.tripwithme.R;

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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		mViewHelper = new ViewHelper(this);
		View searchLayout = findViewById(R.id.search_layout);
		mViewHelper.setGlobalSize((ViewGroup) searchLayout);

		
	    jFont=Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
	    mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");

		selectCityText = (TextView) findViewById(R.id.selectcitytext);
		orText = (TextView) findViewById(R.id.ortext);
		searchCityText = (TextView) findViewById(R.id.searchcitytext);
		autoEdit = (AutoCompleteTextView) findViewById(R.id.autoedit);
		searchButton = (Button) findViewById(R.id.searchbutton);
		seoulButton = (Button) findViewById(R.id.seoulbutton);

		selectCityText.setTypeface(mFont);
		orText.setTypeface(mFont);
		searchCityText.setTypeface(mFont);

		list = new ArrayList<String>();
		list.add("Seoul");
		list.add("Busan");
		list.add("Jeonju");
		list.add("Jeju");
		list.add("Incheon");
		list.add("Naju");

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
			intent = new Intent(this, MenuActivity.class);
			startActivity(intent);
			break;
		}
	}
}
