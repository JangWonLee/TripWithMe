package com.example.tripwithme;

import android.support.v4.app.Fragment;
import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.os.Build;

public class Restaurant extends Activity {
	private Cursor cursor;
	private Typeface mFont;

	private ListView listView;

	private TextView titleText;
	
	private String geonameDatabaseFile = "/sdcard/TripWithMe/DATA.sqlite";
	
	private SQLiteDatabase db;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		mFont = Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");

		titleText = (TextView)findViewById(R.id.titletext);
		titleText.setText("Restaurant List");
		titleText.setTypeface(mFont);

		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null, SQLiteDatabase.OPEN_READWRITE+SQLiteDatabase.CREATE_IF_NECESSARY);	

		cursor = db.rawQuery("SELECT * FROM restaurant", null);
		startManagingCursor(cursor);

		SimpleCursorAdapter Adapter = null;
		Adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[] { "name", "address" }, 
				new int[] { android.R.id.text1, android.R.id.text2});

		listView = (ListView)findViewById(R.id.listview);
		listView.setAdapter(Adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent = new Intent(Restaurant.this, RestaurantDetail.class);
				intent.putExtra("name", cursor.getString(cursor.getColumnIndex("name")));
				intent.putExtra("intro", cursor.getString(cursor.getColumnIndex("intro")));
				intent.putExtra("tel", cursor.getString(cursor.getColumnIndex("tel")));
				intent.putExtra("menu1", cursor.getString(cursor.getColumnIndex("menu1")));
				intent.putExtra("menu2", cursor.getString(cursor.getColumnIndex("menu2")));
				intent.putExtra("menu3", cursor.getString(cursor.getColumnIndex("menu3")));
				intent.putExtra("time", cursor.getString(cursor.getColumnIndex("time")));
				startActivity(intent);
			}
		});
	}
}
