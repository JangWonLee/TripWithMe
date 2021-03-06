package kr.hi.mapmapkorea;

import idv.hondadai.offlinemap.views.OfflineMapView;

import java.io.File;
import java.util.ArrayList;

import kr.hi.mapmapkorea.util.ViewHelper;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripwithme.R;

public class MapActivity extends Activity {
	private static int selectDialog;

	private ViewHelper mViewHelper;
	// layout
	private RelativeLayout mapLayout;

	// MapView
	private MapView mapView;
	private Drawable mRest, mTour, mNow, mLocation, mBus, mSubway, mArrival,
			mDeparture, mSelectedSubway;

	private MapController mapController;
	private ResourceProxy proxy;

	private LocationManager locationManager;
	private String mProvider;

	private String strColor;

	private double mlatitude;
	private double mlongitude;
	private double currentlatitude;
	private double currentlongitude;
	private double startlatitude;
	private double startlongitude;

	private ListView destSearchlist;
	private CharSequence desSearchEdittext;

	private ImageView dest;
	private AutoCompleteTextView desSearchEdit;
	private Button mbtn;
	private Button departureButton;
	private Button arrivalButton;
	private ImageView submenu;
	private Boolean submenuChecked;
	private ImageView restsubmenu;
	private Boolean restsubmenuChecked;
	private ImageView stationsubmenu;
	private Boolean stationsubmenuChecked;
	private ImageView attractionsub;
	private Boolean attractionsubChecked;

	private ArrayList<String> geoList;
	private ArrayAdapter<String> adapter;
	private PathOverlay myPath;	
	private SQLiteDatabase db;
	private Cursor cursor;

	private MyOwnItemizedOverlay restaurantOverlay;
	private MyOwnItemizedOverlay findRestaurantOverlay;
	private MyOwnItemizedOverlay attractionOverlay;
	private MyOwnItemizedOverlay findAttractionOverlay;
	private MyOwnItemizedOverlay currentLocationOverlay;
	private MyOwnItemizedOverlay stationOverlay;
	private MyOwnItemizedOverlay geoSearchLocation;
	private MyOwnItemizedOverlay departureOverlay;
	private MyOwnItemizedOverlay arrivalOverlay;
	private MyOwnItemizedOverlay pathOverlay;

	private Location lastLocation;
	private String geonameDatabaseFile = "/sdcard/Download/mapmapkorea.sqlite";
	private OverlayItem item = null;

	private int GPSstate; // 0=Not GPS 1=GPS

	private Subway subway;
	private Bus bus;
	private Shortest shortest;

	private Typeface mFont;
	private Typeface jFont;
	private Typeface iFont;

	private Integer cityNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init Layout
		setContentView(R.layout.activity_map);

		strColor = "#ff291c73";
		mViewHelper = new ViewHelper(this);
		View mapLayout = findViewById(R.id.map_layout);
		mViewHelper.setGlobalSize((ViewGroup) mapLayout);

		jFont = Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
		mFont = Typeface.createFromAsset(getAssets(),
				"fonts/Anysome Italic.otf");
		iFont = Typeface.createFromAsset(getAssets(),
				"fonts/Belepotan-Italic.otf");

		subway = new Subway();
		bus = new Bus();
		shortest = new Shortest();
		desSearchEdit = (AutoCompleteTextView) findViewById(R.id.dessearchedit);
		desSearchEdit.setHint("Search");
		departureButton = (Button) findViewById(R.id.departurebutton);
		arrivalButton = (Button) findViewById(R.id.arrivalbutton);
		submenu = (ImageView) findViewById(R.id.submenu);
		submenuChecked = false;
		restsubmenu = (ImageView) findViewById(R.id.restsubmenu);
		restsubmenuChecked = false;
		stationsubmenu = (ImageView) findViewById(R.id.stationsubmenu);
		stationsubmenuChecked = false;
		attractionsub = (ImageView) findViewById(R.id.attractionsub);
		attractionsubChecked = false;

		desSearchEdit.setTypeface(mFont);
		departureButton.setTypeface(mFont);
		arrivalButton.setTypeface(mFont);

		cityNumber = getIntent().getIntExtra("CityToMapActivity", 9);

		geoList = new ArrayList<String>();
		mbtn = (Button) findViewById(R.id.dessearchbutton);
		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
				SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.CREATE_IF_NECESSARY);

		cursor = db.rawQuery("SELECT * FROM seoulgeoname ", null);

		int recordCount = cursor.getCount();

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			geoList.add(cursor.getString(1));
		}

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, geoList);

		desSearchEdit.setAdapter(adapter);
		desSearchEdit.setTypeface(mFont);

		this.mapLayout = (RelativeLayout) findViewById(R.id.mapLayout);

		// // init Offline Map
		// File from = new File(Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/Download",
		// "0B0vdbaa0j01ySkl5RzVIV1dtRzA.bin");
		// File to = new File(Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/Download", "Seoul.sqlitedb");
		// if (from.exists())
		// from.renameTo(to);
		// File map = new File(Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/Download", "Seoul.sqlitedb");
		// if (!map.exists()) {
		// Toast.makeText(this, "Please download map", Toast.LENGTH_SHORT)
		// .show();
		// this.finish();
		// }

		/**
		 * 서울 : 0 부산 : 1
		 */
		if (cityNumber == 0) {
			this.mapView = new OfflineMapView(this, "Download/Seoul.sqlitedb");
		} else if (cityNumber == 1) {
			this.mapView = new OfflineMapView(this, "Download/Busan.sqlitedb");
		}

		this.mapController = mapView.getController();

		// set Zoom Countrol
		this.mapView.setBuiltInZoomControls(false); // ?��보기 모양 false
		this.mapView.setMultiTouchControls(true);

		// zoom to 15
		this.mapController.setZoom(15);

		// add mapview
		this.mapLayout.addView(this.mapView, new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		mProvider = locationManager.getBestProvider(criteria, true);

		if (!mProvider.equals(locationManager.GPS_PROVIDER)) {
			GPSstate = 0;
			Toast.makeText(this, "Please Check Your GPS !!", Toast.LENGTH_LONG)
					.show();
			startActivityForResult(new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					0);
		}

		// 초기 ?���? �??��

		startlatitude = getIntent().getExtras().getDouble("latitude");
		startlongitude = getIntent().getExtras().getDouble("longitude");

		/**
		 * cityNumber == 1일때 부산,   나머지는 서울이라 가정.
		 */
		if (cityNumber == 1) {
			Toast.makeText(MapActivity.this, "부산은 단지 샘플입니다. 서울을 선택해주세요 *^^*~~ ", Toast.LENGTH_LONG).show();
			if (startlatitude == (double) 0) {
				if (mProvider.equals(locationManager.GPS_PROVIDER)) {
					getLocation();
					GeoPoint geoPoint = new GeoPoint(currentlatitude,
							currentlongitude);
					// this.mapController.setCenter(geoPoint);
					this.mapController.setCenter(new GeoPoint(35.138063,
							129.079782));
					Log.i("ddd", "ddd");
				} else {
					this.mapController.setCenter(new GeoPoint(35.138063,
							129.079782));
				}
			} else {
				GeoPoint geoPoint = new GeoPoint(startlatitude, startlongitude);
				this.mapController
						.setCenter(new GeoPoint(35.138063, 129.079782));
			}
		} else {

			if (startlatitude == (double) 0) {
				if (mProvider.equals(locationManager.GPS_PROVIDER)) {
					getLocation();
					GeoPoint geoPoint = new GeoPoint(currentlatitude,
							currentlongitude);
					this.mapController.setCenter(geoPoint);
					// this.mapController.setCenter(new GeoPoint(35.138063,
					// 129.079782));
					Log.i("ddd", "ddd");
				} else {
					this.mapController.setCenter(new GeoPoint(37.566352,
							126.978103));
				}
			} else {
				GeoPoint geoPoint = new GeoPoint(startlatitude, startlongitude);
				this.mapController.setCenter(geoPoint);
			}

			/**
			 * 야이콘 Setting
			 */
			setDrawableResources();

			restaurantOverlay = new MyOwnItemizedOverlay(mRest, this);
			findRestaurantOverlay = new MyOwnItemizedOverlay(mRest, this);

			db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
					SQLiteDatabase.OPEN_READWRITE
							+ SQLiteDatabase.CREATE_IF_NECESSARY);

			cursor = db.rawQuery("SELECT * FROM restaurant", null);

			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				if (cursor.getString(7) == null)
					item = new OverlayItem(cursor.getString(1),
							cursor.getString(2) + "\n" + "\n"
									+ cursor.getString(3) + "\n" + "\n"
									+ cursor.getString(4) + "\n" + "\n"
									+ cursor.getString(5) + "\n" + "\n"
									+ cursor.getString(6), new GeoPoint(
									cursor.getDouble(11), cursor.getDouble(12)));
				else if (cursor.getString(8) == null)
					item = new OverlayItem(cursor.getString(1),
							cursor.getString(2) + "\n" + "\n"
									+ cursor.getString(3) + "\n" + "\n"
									+ cursor.getString(4) + "\n" + "\n"
									+ cursor.getString(5) + "\n" + "\n"
									+ cursor.getString(6) + "\n" + "\n"
									+ cursor.getString(7), new GeoPoint(
									cursor.getDouble(11), cursor.getDouble(12)));
				else
					item = new OverlayItem(cursor.getString(1),
							cursor.getString(2) + "\n" + "\n"
									+ cursor.getString(3) + "\n" + "\n"
									+ cursor.getString(4) + "\n" + "\n"
									+ cursor.getString(5) + "\n" + "\n"
									+ cursor.getString(6) + "\n" + "\n"
									+ cursor.getString(7) + "\n" + "\n"
									+ cursor.getString(8), new GeoPoint(
									cursor.getDouble(11), cursor.getDouble(12)));
				Log.i("aa", cursor.getString(1));
				restaurantOverlay.addItem(item);
				if (startlatitude == cursor.getDouble(11))
					findRestaurantOverlay.addItem(item);
			}
			mapView.getOverlays().add(findRestaurantOverlay.getOverlay());

			attractionOverlay = new MyOwnItemizedOverlay(mTour, this);
			findAttractionOverlay = new MyOwnItemizedOverlay(mTour, this);
			cursor = db.rawQuery("SELECT * FROM tourlist", null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				item = new OverlayItem(cursor.getString(1), cursor.getString(2)
						+ "\n" + "\n" + cursor.getString(3) + "\n" + "\n"
						+ cursor.getString(4) + "\n" + "\n"
						+ cursor.getString(5), new GeoPoint(
						cursor.getDouble(6), cursor.getDouble(7)));
				attractionOverlay.addItem(item);
				if (startlatitude == cursor.getDouble(6))
					findAttractionOverlay.addItem(item);
			}
			mapView.getOverlays().add(findAttractionOverlay.getOverlay());

			stationOverlay = new MyOwnItemizedOverlay(mSubway, this);
			cursor = db.rawQuery("SELECT * FROM SeoulStation", null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				item = new OverlayItem(cursor.getString(2), "Line "
						+ cursor.getInt(3), new GeoPoint(cursor.getDouble(6),
						cursor.getDouble(7)));
				stationOverlay.addItem(item);
			}

			db.close();

			currentLocationOverlay = new MyOwnItemizedOverlay(mNow, this);
			if (startlatitude == (double) 0 && currentlatitude != 37.566352) {
				item = new OverlayItem("Current Location", " ", new GeoPoint(
						currentlatitude, currentlongitude));
				currentLocationOverlay.addItem(item);
			}
			mapView.getOverlays().add(currentLocationOverlay.getOverlay());

			geoSearchLocation = new MyOwnItemizedOverlay(mLocation, this);
			mapView.getOverlays().add(geoSearchLocation.getOverlay());
			departureOverlay = new MyOwnItemizedOverlay(mDeparture, this);
			mapView.getOverlays().add(departureOverlay.getOverlay());
			arrivalOverlay = new MyOwnItemizedOverlay(mArrival, this);
			mapView.getOverlays().add(arrivalOverlay.getOverlay());
			pathOverlay = new MyOwnItemizedOverlay(mSelectedSubway, this);
			mapView.getOverlays().add(pathOverlay.getOverlay());
			myPath = new PathOverlay(Color.RED, this);
			Paint paint = new Paint();
			paint.setAlpha(155);
			paint.setColor(Color.argb(150, 255, 0, 0));
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(10);
			myPath.setPaint(paint);
		}
	}

	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.gpsicon:
			if (GPSstate == 0) {
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
				criteria.setPowerRequirement(Criteria.POWER_HIGH);
				mProvider = locationManager.getBestProvider(criteria, true);
				if (mProvider.equals(locationManager.GPS_PROVIDER)) {
					GPSstate = 1;
					getLocation();
				}
			}

			if (mProvider.equals(locationManager.GPS_PROVIDER)) {
				if (mlatitude == 37.566352) {
					Toast.makeText(
							MapActivity.this,
							mProvider + "  "
									+ "failed to find current location",
							Toast.LENGTH_SHORT).show();
				} else {
					GeoPoint tgeopoint = new GeoPoint(mlatitude, mlongitude);
					mapView.getController().animateTo(tgeopoint);
				}
			} else {
				Toast.makeText(this, "Please Check Your GPS !!",
						Toast.LENGTH_SHORT).show();
				startActivityForResult(
						new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
						0);
			}
			break;

		case R.id.submenu:
			if (submenuChecked)
				submenuChecked = false;
			else
				submenuChecked = true;

			if (submenuChecked) {
				submenu.setImageResource(R.drawable.submenu);
				stationsubmenu.setVisibility(View.VISIBLE);
				restsubmenu.setVisibility(View.VISIBLE);
				attractionsub.setVisibility(View.VISIBLE);

			} else {
				submenu.setImageResource(R.drawable.submenu2);
				stationsubmenu.setVisibility(View.INVISIBLE);
				restsubmenu.setVisibility(View.INVISIBLE);
				attractionsub.setVisibility(View.INVISIBLE);
			}
			break;

		case R.id.stationsubmenu:
			if (stationsubmenuChecked) {
				stationsubmenu.setImageResource(R.drawable.stationsubmenu2);
				stationsubmenuChecked = false;
			} else {
				stationsubmenu.setImageResource(R.drawable.stationsubmenu);
				stationsubmenuChecked = true;
			}

			mapView.getOverlays().clear();

			if (stationsubmenuChecked)
				mapView.getOverlays().add(stationOverlay.getOverlay());
			if (restsubmenuChecked)
				mapView.getOverlays().add(restaurantOverlay.getOverlay());
			if (attractionsubChecked)
				mapView.getOverlays().add(attractionOverlay.getOverlay());

			mapView.getOverlays().add(findRestaurantOverlay.getOverlay());
			mapView.getOverlays().add(findAttractionOverlay.getOverlay());
			mapView.getOverlays().add(currentLocationOverlay.getOverlay());
			mapView.getOverlays().add(geoSearchLocation.getOverlay());
			mapView.getOverlays().add(departureOverlay.getOverlay());
			mapView.getOverlays().add(arrivalOverlay.getOverlay());
			mapView.getOverlays().add(pathOverlay.getOverlay());
			mapView.getOverlays().add(myPath);

			mapView.invalidate();
			break;

		case R.id.restsubmenu:
			if (restsubmenuChecked) {
				restsubmenu.setImageResource(R.drawable.restsubmenu2);
				restsubmenuChecked = false;
			} else {
				restsubmenu.setImageResource(R.drawable.restsubmenu);
				restsubmenuChecked = true;
			}

			mapView.getOverlays().clear();

			if (stationsubmenuChecked)
				mapView.getOverlays().add(stationOverlay.getOverlay());
			if (restsubmenuChecked)
				mapView.getOverlays().add(restaurantOverlay.getOverlay());
			if (attractionsubChecked)
				mapView.getOverlays().add(attractionOverlay.getOverlay());

			mapView.getOverlays().add(findRestaurantOverlay.getOverlay());
			mapView.getOverlays().add(findAttractionOverlay.getOverlay());
			mapView.getOverlays().add(currentLocationOverlay.getOverlay());
			mapView.getOverlays().add(geoSearchLocation.getOverlay());
			mapView.getOverlays().add(departureOverlay.getOverlay());
			mapView.getOverlays().add(arrivalOverlay.getOverlay());
			mapView.getOverlays().add(pathOverlay.getOverlay());
			mapView.getOverlays().add(myPath);

			mapView.invalidate();
			break;

		case R.id.attractionsub:
			if (attractionsubChecked) {
				attractionsub.setImageResource(R.drawable.attractionsub2);
				attractionsubChecked = false;
			} else {
				attractionsub.setImageResource(R.drawable.attractionsub);
				attractionsubChecked = true;
			}

			mapView.getOverlays().clear();

			if (stationsubmenuChecked)
				mapView.getOverlays().add(stationOverlay.getOverlay());
			if (restsubmenuChecked)
				mapView.getOverlays().add(restaurantOverlay.getOverlay());
			if (attractionsubChecked)
				mapView.getOverlays().add(attractionOverlay.getOverlay());

			mapView.getOverlays().add(findRestaurantOverlay.getOverlay());
			mapView.getOverlays().add(findAttractionOverlay.getOverlay());
			mapView.getOverlays().add(currentLocationOverlay.getOverlay());
			mapView.getOverlays().add(geoSearchLocation.getOverlay());
			mapView.getOverlays().add(departureOverlay.getOverlay());
			mapView.getOverlays().add(arrivalOverlay.getOverlay());
			mapView.getOverlays().add(pathOverlay.getOverlay());
			mapView.getOverlays().add(myPath);
			mapView.invalidate();
			break;

		case R.id.dessearchbutton:
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(mapView.getWindowToken(), 0);

			// db OPEN
			db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
					SQLiteDatabase.OPEN_READWRITE
							+ SQLiteDatabase.CREATE_IF_NECESSARY);

			String aSQL = "select *" + " from seoulgeoname"
					+ " where NAME like ?";

			String[] args = { "" };
			args[0] = desSearchEdit.getText() + "%";

			cursor = db.rawQuery(aSQL, args);

			int cursorCount = cursor.getCount();
			Log.i("cursorCount", cursorCount + "");

			if (cursorCount > 1) {
				// 검색결과가 여러개이면 List 만듬
				startManagingCursor(cursor);
				destSearchlist = (ListView) findViewById(R.id.list);
				destSearchlist.setBackgroundColor(Color.rgb(0, 0, 0));

				String[] columns = new String[] { "name" };
				int[] to = new int[] { R.id.name_entry };
				Log.i("to", to + "");

				SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(
						MapActivity.this, R.layout.items, cursor, columns, to);

				destSearchlist.setAdapter(mAdapter);
				destSearchlist.setOnItemClickListener(mItemClickListener);
				destSearchlist.setVisibility(View.VISIBLE);

				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(
						desSearchEdit.getWindowToken(), 0);
			} else if (cursorCount == 1) {
				// 검색결과가 한개 이면 바로 이동 , List 안만듬
				cursor.moveToNext();

				desSearchEdittext = desSearchEdit.getText();
				Log.i("desSearchEdittext", desSearchEdittext + "");

				geoSearchLocation.clean();
				GeoPoint geoPoint = new GeoPoint(cursor.getDouble(2),
						cursor.getDouble(3));
				mapController.setCenter(geoPoint);
				item = new OverlayItem(cursor.getString(1), " ", geoPoint);
				geoSearchLocation.addItem(item);

				// destSearchlist.setVisibility(View.INVISIBLE);
				// parent.setVisibility(View.INVISIBLE);

			} else {
				Toast.makeText(MapActivity.this,
						"Did not match any place.\nTry different keywords",
						Toast.LENGTH_SHORT).show();
				// destSearchlist.setVisibility(View.INVISIBLE);
			}

			// outCursor.close();
			// db.close();
			break;

		case R.id.departurebutton:
			if (!departureButton.getText().toString().equals("departure")) {
				if (!departureButton.getText().toString()
						.equals("Current Location")) {
					db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
							SQLiteDatabase.OPEN_READWRITE
									+ SQLiteDatabase.CREATE_IF_NECESSARY);
					String departSQL = "select *" + " from seoulgeoname"
							+ " where name = ?";
					String[] departArgs = { "" };
					departArgs[0] = departureButton.getText().toString();
					cursor = db.rawQuery(departSQL, departArgs);
					cursor.moveToNext();
					mapController.setCenter(new GeoPoint(cursor.getDouble(2),
							cursor.getDouble(3)));
				} else {
					mapController.setCenter(new GeoPoint(currentlatitude,
							currentlongitude));
				}
			}
			break;

		case R.id.arrivalbutton:
			if (!arrivalButton.getText().toString().equals("arrival")) {
				if (!arrivalButton.getText().toString()
						.equals("Current Location")) {
					db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
							SQLiteDatabase.OPEN_READWRITE
									+ SQLiteDatabase.CREATE_IF_NECESSARY);
					String arrivalSQL = "select *" + " from seoulgeoname"
							+ " where name = ?";
					String[] arrivalArgs = { "" };
					arrivalArgs[0] = arrivalButton.getText().toString();
					cursor = db.rawQuery(arrivalSQL, arrivalArgs);
					cursor.moveToNext();
					mapController.setCenter(new GeoPoint(cursor.getDouble(2),
							cursor.getDouble(3)));
				} else {
					mapController.setCenter(new GeoPoint(currentlatitude,
							currentlongitude));
				}
			}
			break;
		case R.id.routebutton:
			if (departureButton.getText().equals("departure")
					|| arrivalButton.getText().equals("arrival"))
				break;
			else {
				AlertDialog.Builder dialog2 = new AlertDialog.Builder(
						MapActivity.this);
				dialog2.setTitle("You");
				dialog2.setMessage("Want to find shortest path?");
				dialog2.setPositiveButton("Yes & Subway",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								findShortestSubwayPath(
										departureButton
												.getText()
												.toString(),
										arrivalButton.getText()
												.toString());
							}
						});
				dialog2.setNeutralButton("Yes & Bus",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								findShortestBusPath(
										departureButton
												.getText()
												.toString(),
										arrivalButton.getText()
												.toString());
							}
						});
				dialog2.setNegativeButton("close", null);
				dialog2.show();
			}			
			break;
		default:
			break;
		}
	}

	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			geoSearchLocation.clean();
			GeoPoint geoPoint = new GeoPoint(cursor.getDouble(2),
					cursor.getDouble(3));
			mapController.setCenter(geoPoint);
			item = new OverlayItem(cursor.getString(1), " ", geoPoint);
			geoSearchLocation.addItem(item);
			parent.setVisibility(View.INVISIBLE);
		}
	};

	public void findShortestSubwayPath(String start, String end) {

		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
				SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.CREATE_IF_NECESSARY);

		String aSQL = "select *" + " from seoulgeoname" + " where name = ?";
		String[] args = { "" };

		int endDB = 0;
		double startLa;
		double startLo;

		if (start.equals("Current Location")) {
			startLa = currentlatitude;
			startLo = currentlongitude;
		} else {
			args[0] = start;

			cursor = db.rawQuery(aSQL, args);
			cursor.moveToNext();

			startLa = cursor.getDouble(2);
			startLo = cursor.getDouble(3);

		}

		double endLa;
		double endLo;

		if (end.equals("Current Location")) {
			endLa = currentlatitude;
			endLo = currentlongitude;
		} else {
			args[0] = end;

			cursor = db.rawQuery(aSQL, args);

			cursor.moveToNext();

			endLa = cursor.getDouble(2);
			endLo = cursor.getDouble(3);
		}

		int startSubway = 0;
		int endSubway = 0;
		double startDistance = 99999;
		double endDistance = 99999;
		double la;
		double lo;

		cursor = db.rawQuery("SELECT * FROM seoulStation", null);

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();

			la = cursor.getDouble(6);
			lo = cursor.getDouble(7);

			if (((startLa - la) * (startLa - la) + (startLo - lo)
					* (startLo - lo)) < startDistance) {
				startDistance = ((startLa - la) * (startLa - la) + (startLo - lo)
						* (startLo - lo));
				startSubway = cursor.getInt(1);
			}
			if (((endLa - la) * (endLa - la) + (endLo - lo) * (endLo - lo)) < endDistance) {
				endDistance = ((endLa - la) * (endLa - la) + (endLo - lo)
						* (endLo - lo));
				endSubway = cursor.getInt(1);
			}
		}
		Log.i("start", " " + startSubway);
		Log.i("end", " " + endSubway);

		subway.Path(startSubway, endSubway, shortest);

		pathOverlay.clean();
		aSQL = "select *" + " from seoulStation" + " where num = ?";
		myPath.clearPath();
		for (int i = 0; i < shortest.pathCount; i++) {

			args[0] = shortest.pathAry[i] + "";
			cursor = db.rawQuery(aSQL, args);
			cursor.moveToNext();

			/*
			 * 출발 도착이 지하철 역인 경우 레이아웃에 추가 안하고 싶을때 if(i == 0) if(startLa ==
			 * cursor.getDouble(6) && startLo == cursor.getDouble(7)) continue;
			 * if(i == shortest.pathCount-1) if(endLa == cursor.getDouble(6) &&
			 * endLo == cursor.getDouble(7)) continue;
			 */

			item = new OverlayItem(cursor.getString(2), "Line "
					+ cursor.getInt(3), new GeoPoint(cursor.getDouble(6),
					cursor.getDouble(7)));
			myPath.addPoint(new GeoPoint(cursor.getDouble(6),cursor.getDouble(7)));
			pathOverlay.addItem(item);
		}
		mapView.getOverlays().add(myPath);
		mapView.invalidate();

		setDialogBrief(true);

	}

	public void findShortestBusPath(String start, String end) {
		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
				SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.CREATE_IF_NECESSARY);

		String aSQL = "select *" + " from seoulgeoname" + " where name = ?";
		String[] args = { "" };

		int endDB = 0;
		double startLa;
		double startLo;

		if (start.equals("Current Location")) {
			startLa = currentlatitude;
			startLo = currentlongitude;
		} else {
			args[0] = start;

			cursor = db.rawQuery(aSQL, args);
			cursor.moveToNext();

			startLa = cursor.getDouble(2);
			startLo = cursor.getDouble(3);

		}

		double endLa;
		double endLo;

		if (end.equals("Current Location")) {
			endLa = currentlatitude;
			endLo = currentlongitude;
		} else {
			args[0] = end;

			cursor = db.rawQuery(aSQL, args);

			cursor.moveToNext();

			endLa = cursor.getDouble(2);
			endLo = cursor.getDouble(3);
		}

		int startSubway1 = 0;
		int startSubway2 = 0;
		int endSubway1 = 0;
		int endSubway2 = 0;
		double startDistance1 = 99999;
		double startDistance2 = 99999;
		double endDistance1 = 99999;
		double endDistance2 = 99999;
		double la;
		double lo;

		db = SQLiteDatabase.openDatabase(geonameDatabaseFile,
				null, SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.CREATE_IF_NECESSARY);

		cursor = db.rawQuery("SELECT * FROM busstop2", null);

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();

			la = cursor.getDouble(5);
			lo = cursor.getDouble(6);

			if (((startLa - la) * (startLa - la) + (startLo - lo)
					* (startLo - lo)) <= startDistance1) {
				startDistance1 = ((startLa - la) * (startLa - la) + (startLo - lo)
						* (startLo - lo));
				startDistance2 = startDistance1;
				startSubway1 = cursor.getInt(7);
				startSubway2 = startSubway1;
			} else if (((startLa - la) * (startLa - la) + (startLo - lo)
					* (startLo - lo)) <= startDistance2) {
				startDistance2 = ((startLa - la) * (startLa - la) + (startLo - lo)
						* (startLo - lo));
				startSubway2 = cursor.getInt(7);
			}
			if (((endLa - la) * (endLa - la) + (endLo - lo) * (endLo - lo)) <= endDistance1) {
				endDistance1 = ((endLa - la) * (endLa - la) + (endLo - lo)
						* (endLo - lo));
				endDistance2 = endDistance1;
				endSubway1 = cursor.getInt(7);
				endSubway2 = endSubway1;
			} else if (((endLa - la) * (endLa - la) + (endLo - lo)
					* (endLo - lo)) <= endDistance2) {
				endDistance2 = ((endLa - la) * (endLa - la) + (endLo - lo)
						* (endLo - lo));
				endSubway2 = cursor.getInt(7);
			}
		}
		// Log.i("start", " " + startSubway);
		// Log.i("end", " " + endSubway);
		Shortest swapShortest = new Shortest();
		bus.search(startSubway1, endSubway1, shortest);
		/*
		 * bus.search(startSubway1, endSubway2, swapShortest);
		 * if(swapShortest.pathCount < shortest.pathCount) { shortest =
		 * swapShortest; swapShortest = new Shortest(); }
		 * 
		 * bus.search(startSubway2, endSubway1, swapShortest);
		 * if(swapShortest.pathCount < shortest.pathCount) { shortest =
		 * swapShortest; swapShortest = new Shortest(); }
		 * 
		 * bus.search(startSubway2, endSubway2, swapShortest);
		 * if(swapShortest.pathCount < shortest.pathCount) { shortest =
		 * swapShortest; }
		 */
		pathOverlay.clean();
		String busSql = "select * FROM busstop2 Where line_id = ? and busstop_id = ?";
		String[] busArgs = { "", "" };
		myPath.clearPath();
		if(shortest.pathCount == 9999) {
			AlertDialog.Builder dlg = new AlertDialog.Builder(MapActivity.this);
			dlg.setTitle(" Sorry").setMessage("Can't find shortest path using Bus. \nPlease use subway.");
			dlg.setPositiveButton("find Subway Way",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							findShortestSubwayPath(
									departureButton
											.getText()
											.toString(),
									arrivalButton.getText()
											.toString());
						}
					});		
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			dlg.show();
			return;
		}
		
		for (int i = 0; i < shortest.pathCount; i++) {

			busArgs[1] = shortest.pathAry[i] + "";
			busArgs[0] = shortest.lineAry[i] + "";
			cursor = db.rawQuery(busSql, busArgs);
			cursor.moveToNext();

			/*
			 * 출발 도착이 지하철 역인 경우 레이아웃에 추가 안하고 싶을때 if(i == 0) if(startLa ==
			 * cursor.getDouble(6) && startLo == cursor.getDouble(7)) continue;
			 * if(i == shortest.pathCount-1) if(endLa == cursor.getDouble(6) &&
			 * endLo == cursor.getDouble(7)) continue;
			 */

			item = new OverlayItem(cursor.getString(4), "Line "
					+ cursor.getString(2), new GeoPoint(cursor.getDouble(5),
					cursor.getDouble(6)));
			pathOverlay.addItem(item);
			myPath.addPoint(new GeoPoint(cursor.getDouble(5),cursor.getDouble(6)));
		}
		mapView.getOverlays().add(myPath);
		mapView.invalidate();
		setDialogBrief(false);

	}

	private void setDialogTotal(final boolean findSubway) {
		AlertDialog.Builder ab2 = new AlertDialog.Builder(MapActivity.this);
		ab2.setTitle(" All Path").setMessage("Detarture : " + departureButton.getText() + "\n"
				+ "Arrival : " + arrivalButton.getText() +  "\n\n" +
				shortest.time + "\n\n\n\n" + "Path\n" + shortest.totalPath);
		ab2.setPositiveButton("Brief Path",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setDialogBrief(findSubway);
						dialog.cancel();
					}
				});
		if(findSubway) {
			ab2.setNeutralButton("Find Bus Way",
					new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {
					findShortestBusPath(
							departureButton
									.getText()
									.toString(),
							arrivalButton.getText()
									.toString());
				}
			});
		}
		else {
			ab2.setNeutralButton("find Subway Way",
					new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {
					findShortestSubwayPath(
							departureButton
									.getText()
									.toString(),
							arrivalButton.getText()
									.toString());
				}
			});
		}		
		ab2.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		ab2.show();
	}

	private void setDialogBrief(final boolean findSubway) {
		AlertDialog.Builder ab = new AlertDialog.Builder(MapActivity.this);
		ab.setTitle("Brief Path").setMessage("Detarture : " + departureButton.getText() + "\n"
				+ "Arrival : " + arrivalButton.getText() +  "\n\n" +
				shortest.time + "\n\n\n\n" + "Path\n" + shortest.briefPath);
		ab.setPositiveButton("All Path", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setDialogTotal(findSubway);
				dialog.cancel();
			}
		});
		if(findSubway) {
			ab.setNeutralButton("Find Bus Way",
					new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {
					findShortestBusPath(
							departureButton
									.getText()
									.toString(),
							arrivalButton.getText()
									.toString());
				}
			});
		}
		else {
			ab.setNeutralButton("find Subway Way",
					new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {
					findShortestSubwayPath(
							departureButton
									.getText()
									.toString(),
							arrivalButton.getText()
									.toString());
				}
			});
		}
		ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		ab.show();
	}

	LocationListener mListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {

			Toast.makeText(MapActivity.this, "리스?��?���?", Toast.LENGTH_SHORT)
					.show();

			lastLocation = locationManager.getLastKnownLocation(mProvider);
			if (lastLocation == null) {
				Toast.makeText(MapActivity.this,
						"failed to find current location", Toast.LENGTH_SHORT)
						.show();
				mlatitude = 37.566352;
				mlongitude = 126.978103;
			} else {
				mlatitude = lastLocation.getLatitude();
				mlongitude = lastLocation.getLongitude();

				item = new OverlayItem("Current Location", " ", new GeoPoint(
						mlatitude, mlongitude));
				// currentLocationOverlay.clean();
				currentLocationOverlay.addItem(item);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}
	};

	public class MyOwnItemizedOverlay {
		protected ItemizedIconOverlay<OverlayItem> mOverlay;
		protected Context mContext;
		protected Drawable mMarker;

		public MyOwnItemizedOverlay(Drawable marker, Context context) {
			mContext = context;
			ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
			ResourceProxy resourceProxy = (ResourceProxy) new DefaultResourceProxyImpl(
					mContext);
			mMarker = marker;

			mOverlay = new ItemizedIconOverlay<OverlayItem>(
					items,
					mMarker,
					new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
						@Override
						public boolean onItemSingleTapUp(final int index,
								final OverlayItem item) {
							return onSingleTapUpHelper(index, item);
						}

						@Override
						public boolean onItemLongPress(final int index,
								final OverlayItem item) {
							return true;
						}
					}, resourceProxy);

		}

		public boolean onSingleTapUpHelper(int i, final OverlayItem item) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.setNegativeButton("close", null);
			dialog.setPositiveButton("departure",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (item.getTitle().equals("Departure Location")
									|| item.getTitle().equals(
											"Arrival Location"))
								return;
							pathOverlay.clean();
							departureButton.setText(item.getTitle());
							departureButton.setTextColor(Color
									.parseColor(strColor));
							departureButton.setTypeface(iFont);
							if (!departureButton.getText().equals("departure")
									&& !arrivalButton.getText().equals(
											"arrival")) {
								AlertDialog.Builder dialog2 = new AlertDialog.Builder(
										mContext);
								dialog2.setTitle("You");
								dialog2.setMessage("Want to find shortest path?");
								dialog2.setPositiveButton("Yes & Subway",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												findShortestSubwayPath(
														departureButton
																.getText()
																.toString(),
														arrivalButton.getText()
																.toString());
											}
										});
								dialog2.setNeutralButton("Yes & Bus",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												findShortestBusPath(
														departureButton
																.getText()
																.toString(),
														arrivalButton.getText()
																.toString());
											}
										});
								dialog2.setNegativeButton("close", null);
								dialog2.show();
							}

							if (!item.getTitle().equals("Current Location")) {
								db = SQLiteDatabase
										.openDatabase(
												geonameDatabaseFile,
												null,
												SQLiteDatabase.OPEN_READWRITE
														+ SQLiteDatabase.CREATE_IF_NECESSARY);
								String departureSQL = "select *"
										+ " from seoulgeoname"
										+ " where name = ?";
								String[] departureArgs = { "" };
								departureArgs[0] = item.getTitle();
								cursor = db.rawQuery(departureSQL,
										departureArgs);
								cursor.moveToNext();
								departureOverlay.clean();
								OverlayItem dItem = new OverlayItem(
										"Departure Location", " ",
										new GeoPoint(cursor.getDouble(2),
												cursor.getDouble(3)));
								departureOverlay.addItem(dItem);

							} else {
								departureOverlay.clean();
								OverlayItem dItem = new OverlayItem(
										"Departure Location", " ",
										new GeoPoint(currentlatitude,
												currentlongitude));
								departureOverlay.addItem(dItem);
							}
							mapView.invalidate();

						}
					});
			dialog.setNeutralButton("arrival",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (item.getTitle().equals("Departure Location")
									|| item.getTitle().equals(
											"Arrival Location"))
								return;
							pathOverlay.clean();
							arrivalButton.setText(item.getTitle());
							arrivalButton.setTextColor(Color
									.parseColor(strColor));
							arrivalButton.setTypeface(iFont);
							if (!departureButton.getText().equals("departure")
									& !arrivalButton.getText()
											.equals("arrival")) {
								AlertDialog.Builder dialog2 = new AlertDialog.Builder(
										mContext);
								dialog2.setTitle("You");
								dialog2.setMessage("Want to find shortest path?");
								dialog2.setPositiveButton("Yes & Subway",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												findShortestSubwayPath(
														departureButton
																.getText()
																.toString(),
														arrivalButton.getText()
																.toString());
											}
										});
								dialog2.setNeutralButton("Yes & Bus",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												findShortestBusPath(
														departureButton
																.getText()
																.toString(),
														arrivalButton.getText()
																.toString());
											}
										});
								dialog2.setNegativeButton("close", null);
								dialog2.show();
							}

							if (!item.getTitle().equals("Current Location")) {
								db = SQLiteDatabase
										.openDatabase(
												geonameDatabaseFile,
												null,
												SQLiteDatabase.OPEN_READWRITE
														+ SQLiteDatabase.CREATE_IF_NECESSARY);
								String arrivalSQL = "select *"
										+ " from seoulgeoname"
										+ " where name = ?";
								String[] arrivalArgs = { "" };
								arrivalArgs[0] = item.getTitle();
								cursor = db.rawQuery(arrivalSQL, arrivalArgs);
								cursor.moveToNext();
								arrivalOverlay.clean();
								OverlayItem aItem = new OverlayItem(
										"Arrival Location", " ", new GeoPoint(
												cursor.getDouble(2), cursor
														.getDouble(3)));
								arrivalOverlay.addItem(aItem);

							} else {
								arrivalOverlay.clean();
								OverlayItem aItem = new OverlayItem(
										"Arrival Location", " ", new GeoPoint(
												currentlatitude,
												currentlongitude));
								arrivalOverlay.addItem(aItem);
							}
							mapView.invalidate();
						}
					});
			dialog.show();

			return true;
		}

		public void addItem(OverlayItem item) {
			mOverlay.addItem(item);
		}

		public void clean() {
			mOverlay.removeAllItems();
		}

		public ItemizedIconOverlay<OverlayItem> getOverlay() {
			return mOverlay;
		}
	}

	public void boundCenterBottom(Drawable defaultMarker) {
	}

	public void boundCenter(Drawable mRed2) {
	}

	private void setDrawableResources() {
		Drawable d;
		Bitmap bitmap;
		d = getResources().getDrawable(R.drawable.restaurenticon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mRest = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(
				bitmap, 23, 32, true));

		d = getResources().getDrawable(R.drawable.touricon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mTour = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(
				bitmap, 20, 27, true));

		d = getResources().getDrawable(R.drawable.currentpositionicon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mNow = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(
				bitmap, 20, 27, true));

		d = getResources().getDrawable(R.drawable.locationicon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mLocation = new BitmapDrawable(getResources(),
				Bitmap.createScaledBitmap(bitmap, 20, 27, true));

		d = getResources().getDrawable(R.drawable.busicon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mBus = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(
				bitmap, 20, 27, true));

		d = getResources().getDrawable(R.drawable.subwayicon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mSubway = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(
				bitmap, 20, 27, true));

		d = getResources().getDrawable(R.drawable.arrivalicon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mArrival = new BitmapDrawable(getResources(),
				Bitmap.createScaledBitmap(bitmap, 38, 40, true));

		d = getResources().getDrawable(R.drawable.departureicon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mDeparture = new BitmapDrawable(getResources(),
				Bitmap.createScaledBitmap(bitmap, 53, 40, true));

		d = getResources().getDrawable(R.drawable.selectedsubwayicon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mSelectedSubway = new BitmapDrawable(getResources(),
				Bitmap.createScaledBitmap(bitmap, 20, 27, true));
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mProvider.equals(locationManager.GPS_PROVIDER)) {

			if (mProvider.equals(locationManager.GPS_PROVIDER)) {
				getLocation();
				Location lastLocation = locationManager
						.getLastKnownLocation(mProvider);
				if (startlatitude == (double) 0 && lastLocation != null) {
					mlatitude = lastLocation.getLatitude();
					mlongitude = lastLocation.getLongitude();
					item = new OverlayItem("Current Location", " ",
							new GeoPoint(mlatitude, mlongitude));
					// currentLocationOverlay.clean();
					currentLocationOverlay.addItem(item);
				}
			}
		}
	}

	private void getLocation() {
		if (mProvider.equals(locationManager.GPS_PROVIDER)) {
			// locationManager.requestLocationUpdates(mProvider, 3000, 5,
			// mListener);
			lastLocation = locationManager.getLastKnownLocation(mProvider);
			if (lastLocation == null) {
				Toast.makeText(MapActivity.this,
						"failed to find current location", Toast.LENGTH_SHORT)
						.show();
				mlatitude = 37.566352;
				mlongitude = 126.978103;
			} else {
				mlatitude = lastLocation.getLatitude();
				mlongitude = lastLocation.getLongitude();
			}
			currentlatitude = mlatitude;
			currentlongitude = mlongitude;

		}
	}

	@Override
	public void onPause() {

		super.onPause();
		if (mProvider == locationManager.GPS_PROVIDER)
			locationManager.removeUpdates(mListener);

	}

}
