package kr.hi.mapmapkorea;

import kr.hi.mapmapkorea.util.ViewHelper;

import com.example.tripwithme.R;

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

public class RestaurantActivity extends Activity {
	private ViewHelper mViewHelper;

	private Cursor cursor;
	private Typeface mFont;

	private ListView listView;
	private TextView titleText;

	private String geonameDatabaseFile = "/sdcard/Download/mapmapkorea.sqlite";

	private SQLiteDatabase db;
	private Typeface jFont;
	private Typeface kFont;

	private Integer cityNumber;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		mViewHelper = new ViewHelper(this);
		View listLayout = findViewById(R.id.list_layout);
		mViewHelper.setGlobalSize((ViewGroup) listLayout);

		jFont = Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
		mFont = Typeface.createFromAsset(getAssets(),
				"fonts/Anysome Italic.otf");
		kFont = Typeface.createFromAsset(getAssets(),
				"fonts/dearJoe 6 TRIAL.otf");

		titleText = (TextView) findViewById(R.id.titletext);
		titleText.setText("Restaurant List");
		titleText.setTypeface(mFont);

		cityNumber = getIntent().getIntExtra("CityToRestaurantActivity", 9);
		/**
		 * 부산은 샘플입니다
		 */
		if (cityNumber == 1) {
			Toast.makeText(RestaurantActivity.this,
					"부산은 단지 샘플입니다. 서울을 선택하여 주세요 *^^* ~~", Toast.LENGTH_LONG)
					.show();
		} else {

			db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
					SQLiteDatabase.OPEN_READWRITE
							+ SQLiteDatabase.CREATE_IF_NECESSARY);

			cursor = db.rawQuery("SELECT * FROM restaurant", null);
			startManagingCursor(cursor);

			SimpleCursorAdapter Adapter = null;
			Adapter = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_2, cursor, new String[] {
							"name", "address" }, new int[] {
							android.R.id.text1, android.R.id.text2 });

			listView = (ListView) findViewById(R.id.listview);
			listView.setAdapter(Adapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Intent intent = new Intent(RestaurantActivity.this,
							RestaurantDetailActivity.class);
					intent.putExtra("name",
							cursor.getString(cursor.getColumnIndex("name")));
					intent.putExtra("intro",
							cursor.getString(cursor.getColumnIndex("intro")));
					intent.putExtra("tel",
							cursor.getString(cursor.getColumnIndex("tel")));
					intent.putExtra("menu1",
							cursor.getString(cursor.getColumnIndex("menu1")));
					intent.putExtra("menu2",
							cursor.getString(cursor.getColumnIndex("menu2")));
					intent.putExtra("menu3",
							cursor.getString(cursor.getColumnIndex("menu3")));
					intent.putExtra("time",
							cursor.getString(cursor.getColumnIndex("time")));
					intent.putExtra("latitude",
							cursor.getDouble(cursor.getColumnIndex("latitude")));
					intent.putExtra("longitude", cursor.getDouble(cursor
							.getColumnIndex("longitude")));
					intent.putExtra("RastaurantToDetailActivity", cityNumber);
					startActivity(intent);
				}
			});
		}
	}
}
