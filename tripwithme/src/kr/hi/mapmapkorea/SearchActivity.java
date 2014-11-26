package kr.hi.mapmapkorea;

import java.io.File;
import java.util.ArrayList;

import kr.hi.mapmapkorea.util.ViewHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripwithme.R;

public class SearchActivity extends Activity {
	private ViewHelper mViewHelper;

	private Typeface mFont;
	private Typeface jFont;
	private Typeface kFont;

	private TextView autoText;
	private TextView selectCityText;
	private TextView orText;
	private TextView searchCityText;

	private Button searchButton;
	private Button cityMapButton1;
	private Button cityMapButton2;
	private Button cityMapButton3;
	private Button cityDeleteButton;

	private AutoCompleteTextView autoEdit;
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;

	private File seoul;
	private File seoulDB;
	private File busan;
	private File busanDB;
	private File incheon;
	private File incheonDB;
	private boolean isMap;

	private CheckBox checkBox1;
	private CheckBox checkBox2;
	private CheckBox checkBox3;

	private ProgressBar progressBar;

	private Integer mProg;
	private ListView listView;

	private Boolean seoulIsDownloading;
	private Boolean busanIsDownloading;

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
		cityDeleteButton = (Button) findViewById(R.id.citydeletebutton);

		checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
		checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
		checkBox3 = (CheckBox) findViewById(R.id.checkbox3);

		checkBox1.setVisibility(View.INVISIBLE);
		checkBox2.setVisibility(View.INVISIBLE);
		checkBox3.setVisibility(View.INVISIBLE);
		
		listView = (ListView) findViewById(R.id.listview);
		
		selectCityText.setTypeface(kFont);
		orText.setTypeface(kFont);
		searchCityText.setTypeface(kFont);


		seoulIsDownloading = false;
		busanIsDownloading = false;

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
		seoulDB = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "MapMapKorea.sqlite");
		busan = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Busan.sqlitedb");
		busanDB = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "MapMapKorea2.sqlite");
		incheon = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Incheon.sqlitedb");

		checkBox1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkBox1.isChecked() || checkBox2.isChecked()
						|| checkBox3.isChecked()) {
					cityDeleteButton.setVisibility(View.VISIBLE);
				} else {
					cityDeleteButton.setVisibility(View.INVISIBLE);
				}
			}
		});

		checkBox2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkBox1.isChecked() || checkBox2.isChecked()
						|| checkBox3.isChecked()) {
					cityDeleteButton.setVisibility(View.VISIBLE);
				} else {
					cityDeleteButton.setVisibility(View.INVISIBLE);
				}
			}
		});

		checkBox3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkBox1.isChecked() || checkBox2.isChecked()
						|| checkBox3.isChecked()) {
					cityDeleteButton.setVisibility(View.VISIBLE);
				} else {
					cityDeleteButton.setVisibility(View.INVISIBLE);
				}
			}
		});

		/**
		 * 맵 이름 변환
		 */
		File from = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download",
				"0B0vdbaa0j01ySkl5RzVIV1dtRzA.bin");
		File to = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Seoul.sqlitedb");
		if (from.exists())
			from.renameTo(to);
		File map = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Seoul.sqlitedb");
		if (!map.exists())
			Toast.makeText(this, "Please download map", Toast.LENGTH_SHORT)
					.show();
		
		
		from = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download",
				"0B0vdbaa0j01yVEh3MG9PckJRTEk.bin");
		to = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "MapMapKorea.sqlite");
		if (from.exists())
			from.renameTo(to);
		
		File from1 = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download",
				"0B0vdbaa0j01yOTRadXVBWWxHeGs.bin");
		File to1 = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Busan.sqlitedb");
		if (from1.exists())
			from1.renameTo(to1);
		
		File map1 = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "Busan.sqlitedb");
		if (!map.exists())
			Toast.makeText(this, "Please download map", Toast.LENGTH_SHORT)
					.show();
		from1 = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download",
				"0B0vdbaa0j01ydldXZklzLWs5SEE.bin");
		to1 = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Download", "MapMapKorea2.sqlite");
		if (from1.exists())
			from1.renameTo(to1);
			
		

		Log.i("busan", busan.exists() + "");

		setButtonVisiblity();

			
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, list);

		autoEdit.setAdapter(adapter);


	}

	public void setButtonVisiblity() {
		if (seoul.exists() && seoulDB.exists() && busan.exists()) {
			cityMapButton1.setVisibility(View.VISIBLE);
			checkBox1.setVisibility(View.VISIBLE);
			cityMapButton1.setText("Seoul");

			cityMapButton2.setVisibility(View.VISIBLE);
			checkBox2.setVisibility(View.VISIBLE);
			cityMapButton2.setText("Busan");
			Log.i("??","??1");

			// cityDeleteButton.setVisibility(View.VISIBLE);

		} else if (seoul.exists() && seoulDB.exists() ) {
			cityMapButton1.setVisibility(View.VISIBLE);
			checkBox1.setVisibility(View.VISIBLE);
			cityMapButton1.setText("Seoul");
			Log.i("??","??2");
			// cityDeleteButton.setVisibility(View.VISIBLE);

		} else if (busan.exists()) {
			cityMapButton1.setVisibility(View.VISIBLE);
			checkBox1.setVisibility(View.VISIBLE);
			cityMapButton1.setText("Busan");
			Log.i("??","??3");
			// cityDeleteButton.setVisibility(View.VISIBLE);
		}
		// if (checkBox1.isChecked() || checkBox2.isChecked() ||
		// checkBox3.isChecked()) {
		// cityDeleteButton.setVisibility(View.VISIBLE);
		// }
	}

	private void setDialogDelete(final File file, final File file1) {
		AlertDialog.Builder ab = new AlertDialog.Builder(SearchActivity.this);
		ab.setTitle("You choose the city").setMessage("Are you sure you want to Delete ?");
		ab.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (file.delete()) {

					if (file1 == null) {
						file.delete();
						if(file == seoul)
							seoulDB.delete();
						else if(file == busan)
							busanDB.delete();
					} else {
						file.delete();
						file1.delete();
						seoulDB.delete();
						busanDB.delete();
					}
					

					Intent intent = new Intent(SearchActivity.this,
							SearchActivity.class);
					startActivity(intent);
					finish();
					Log.i("fi", "yes");
				} else {
					Log.i("fi", "nono");
				}
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		ab.show();
	}

	private void setDialogDownload(final int cityNumber) {
		String cityName = null;
		switch (cityNumber) {
		case 0:
			cityName = "SEOUL";
			break;
		case 1:
			cityName = "BUSAN";
			break;
		case 2:
			cityName = "INCHEON";
			break;
		default:
			break;
		}

		AlertDialog.Builder ab = new AlertDialog.Builder(SearchActivity.this);
		ab.setTitle(cityName).setMessage(
				"  You choose the city named * " + cityName
						+ "*.\n\n Do you want to Download this city_map?\n");
		ab.setPositiveButton("Map Download", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// setOfList.add(String.valueOf(cityNumber));
				// setOfPut.add(String.valueOf(cityNumber));

				Intent intent = new Intent(SearchActivity.this, WebViews.class);
				intent.putExtra("CityToWebview", cityNumber);
				isMap=true;
				intent.putExtra("isMap", isMap);
				startActivity(intent);
				dialog.cancel();
			}
		});
		ab.setNeutralButton("Data Download",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(
					DialogInterface dialog,
					int which) {
				Intent intent = new Intent(SearchActivity.this, WebViews.class);
				intent.putExtra("CityToWebview", cityNumber);
				isMap=false;
				intent.putExtra("isMap", isMap);
				startActivity(intent);
				dialog.cancel();				
			}
		});
		ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		ab.show();
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
				setDialogDownload(0);
			} else if (msg.equals("Busan") || msg.equals("busan")
					|| msg.equals("BUSAN")) {
				setDialogDownload(1);
			} else if (msg.equals("Incheon") || msg.equals("incheon")
					|| msg.equals("INCHEON")) {
				setDialogDownload(2);
			} else {
				Toast.makeText(SearchActivity.this,
						"Wrong City Name, Please Check again",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.citymapbutton1:
			Intent intent1 = new Intent(this, MenuActivity.class);
			if (seoul.exists() || (seoul.exists() && busan.exists())) {
				intent1.putExtra("CityToMenuActivity", 0);
			} else if (busan.exists()) {
				intent1.putExtra("CityToMenuActivity", 1);
			}
			startActivity(intent1);
			break;

		case R.id.citymapbutton2:
			Intent intent2 = new Intent(this, MenuActivity.class);
			if (busan.exists()) {
				intent2.putExtra("CityToMenuActivity", 1);
				startActivity(intent2);
			}
			break;

		case R.id.citymapbutton3:
			if (seoul.exists()) {
				Intent intent = new Intent(this, MenuActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(SearchActivity.this,
						"No map file, Please Download first", Toast.LENGTH_LONG)
						.show();
			}
			break;

		case R.id.citydeletebutton:
			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Download", "Busan.sqlite");

			Log.i("file", file.exists() + "");

			if (checkBox1.isChecked() && checkBox2.isChecked()) {
			    setDialogDelete(seoul, busan);

			} else if (checkBox1.isChecked() && seoul.exists()) {
				setDialogDelete(seoul, null);
				
			} else if (checkBox1.isChecked() && seoul.exists()==false) {
				setDialogDelete(busan, null);
				
			} else if (checkBox2.isChecked()) {
				setDialogDelete(busan, null);
				Log.i("aa", "2222");
			}

			// setButtonVisiblity();
			break;

		}
	}
}
