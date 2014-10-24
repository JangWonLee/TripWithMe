package com.example.tripwithme;

import idv.hondadai.offlinemap.views.OfflineMapView;

import java.util.ArrayList;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Map extends Activity {
	// layout
	private RelativeLayout mapLayout;

	// MapView
	private MapView mapView;
	private Drawable mRest, mTour, mNow;
	
	private MapController mapController;
	private ResourceProxy proxy;
	
	private LocationManager locationManager;
	private String mProvider;
	
	private double mlatitude;
	private double mlongitude;
	private double currentlatitude;
	private double currentlongitude;
	private double startlatitude;
	private double startlongitude;

	
	private ImageView dest;
	private AutoCompleteTextView desSearchEdit;
	private Button mbtn;
	private TextView departuretext;
	private TextView arrivaltext;
	
	private ArrayList<String> geoList;
	private ArrayAdapter<String> adapter;
	
	private SQLiteDatabase db;
	private Cursor cursor;

	
	private MyOwnItemizedOverlay itemizedoverlay3;
	private MyOwnItemizedOverlay geoSearchLocation;

	private Location lastLocation;
	private String geonameDatabaseFile = "/sdcard/Download/mapmapkorea.sqlite";
	private OverlayItem item = null;
	
	private int GPSstate; // 0=Not GPS	1=GPS
	
	private Subway subway;
	private Shortest shortest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init Layout
		setContentView(R.layout.map);

		subway = new Subway();
		shortest = new Shortest();
		desSearchEdit = (AutoCompleteTextView)findViewById(R.id.dessearchedit);
		desSearchEdit.setHint("Search");
		departuretext = (TextView)findViewById(R.id.departuretext);
		arrivaltext = (TextView)findViewById(R.id.arrivaltext);
		
		geoList = new ArrayList<String>();
		mbtn = (Button)findViewById(R.id.dessearchbutton);
		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null, SQLiteDatabase.OPEN_READWRITE+SQLiteDatabase.CREATE_IF_NECESSARY);
		
		cursor = db.rawQuery("SELECT * FROM seoulgeoname ", null);
		
		int recordCount = cursor.getCount();
		Toast.makeText(this,"cursor count : " + recordCount, Toast.LENGTH_SHORT).show();
		
		for(int i=0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			geoList.add(cursor.getString(1));
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, geoList);
        
		desSearchEdit.setAdapter(adapter);

		this.mapLayout = (RelativeLayout)findViewById(R.id.mapLayout);

		// init Offline Map
		this.mapView = new OfflineMapView(this, "TripWithMe/Seoul.sqlitedb");
		this.mapController = mapView.getController();

		// set Zoom Countrol
		this.mapView.setBuiltInZoomControls(false);      // 돋보기 모양 false
		this.mapView.setMultiTouchControls(true);

		// zoom to 15
		this.mapController.setZoom(15);

		// add mapview
		this.mapLayout.addView(this.mapView, new RelativeLayout.LayoutParams (android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));




		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		mProvider = locationManager.getBestProvider(criteria, true);
		
		if(!mProvider.equals(locationManager.GPS_PROVIDER)) {
			GPSstate = 0;
			Toast.makeText(this,"Please Check Your GPS !!", Toast.LENGTH_LONG).show();
			startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
		}


		// 초기 위치 지정
		
		startlatitude = getIntent().getExtras().getDouble("latitude");
		startlongitude = getIntent().getExtras().getDouble("longitude");
		
		if(startlatitude == (double)0){
			if(mProvider.equals(locationManager.GPS_PROVIDER)) {
				getLocation();
				GeoPoint geoPoint = new GeoPoint(currentlatitude, currentlongitude);
				this.mapController.setCenter(geoPoint);
			} else {
				this.mapController.setCenter(new GeoPoint(37.566352, 126.978103));
			}
		} else {
			GeoPoint geoPoint = new GeoPoint(startlatitude, startlongitude);
			this.mapController.setCenter(geoPoint);
		}
	
		Drawable d;
		Bitmap bitmap;
		d = getResources().getDrawable(R.drawable.restaurenticon);
		d.setBounds(0, 0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mRest = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,23,32,true));
		
		d = getResources().getDrawable(R.drawable.touricon);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mTour = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,20,27,true));
		
		d = getResources().getDrawable(R.drawable.currentpositionicon);
		d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
		bitmap = ((BitmapDrawable) d).getBitmap();
		mNow = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,20, 27,true));
		
		MyOwnItemizedOverlay itemizedoverlay1 = new MyOwnItemizedOverlay(mRest, this);

		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null, SQLiteDatabase.OPEN_READWRITE+SQLiteDatabase.CREATE_IF_NECESSARY);
		
		cursor = db.rawQuery("SELECT * FROM restaurant", null);

		for(int i=0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			item = new OverlayItem(cursor.getString(1), cursor.getString(2),
			new GeoPoint(cursor.getDouble(11), cursor.getDouble(12)));
			Log.i("aa", cursor.getString(1));
			if(startlatitude == (double)0)
				itemizedoverlay1.addItem(item);
			else
				if(startlatitude == cursor.getDouble(11))
					itemizedoverlay1.addItem(item);
		}
		mapView.getOverlays().add(itemizedoverlay1.getOverlay());
		

		MyOwnItemizedOverlay itemizedoverlay2 = new MyOwnItemizedOverlay(mTour, this);

		
		cursor = db.rawQuery("SELECT * FROM tourlist", null);
		for(int i=0; i < cursor.getCount(); i++)
		{
			cursor.moveToNext();
			item = new OverlayItem(cursor.getString(1), cursor.getString(2),
			new GeoPoint(cursor.getDouble(6), cursor.getDouble(7)));
			if(startlatitude == (double)0)
				itemizedoverlay2.addItem(item);
			else
				if(startlatitude == cursor.getDouble(6))
					itemizedoverlay2.addItem(item);
		}
		
		mapView.getOverlays().add(itemizedoverlay2.getOverlay());
		db.close();
		
		itemizedoverlay3 = new MyOwnItemizedOverlay(mNow, this);
		if(startlatitude == (double)0 && currentlatitude != 37.566352) {
			item = new OverlayItem("Current Location"," ",
					new GeoPoint(currentlatitude, currentlongitude));
			itemizedoverlay3.addItem(item);
		}
		mapView.getOverlays().add(itemizedoverlay3.getOverlay());
		
		geoSearchLocation = new MyOwnItemizedOverlay(mNow, this);
		mapView.getOverlays().add(geoSearchLocation.getOverlay());
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mProvider.equals(locationManager.GPS_PROVIDER)) {
			
			if(mProvider.equals(locationManager.GPS_PROVIDER)) {
				getLocation();
				Location lastLocation = locationManager.getLastKnownLocation(mProvider);
				if(startlatitude == (double)0 && lastLocation != null) {
					mlatitude = lastLocation.getLatitude();
					mlongitude = lastLocation.getLongitude();
					item = new OverlayItem("Current Location"," ",
							new GeoPoint(mlatitude, mlongitude));
	//				itemizedoverlay3.clean();
					itemizedoverlay3.addItem(item);
				}
			}
		}
	}

	private void getLocation() {
		if(mProvider.equals(locationManager.GPS_PROVIDER)) {
//			locationManager.requestLocationUpdates(mProvider, 3000, 5, mListener);
			lastLocation = locationManager.getLastKnownLocation(mProvider);
			if(lastLocation == null) {
				Toast.makeText(Map.this,"failed to find current location", Toast.LENGTH_SHORT).show();
				mlatitude = 37.566352;
				mlongitude = 126.978103;
			}
			else {
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
		if(mProvider == locationManager.GPS_PROVIDER)
			locationManager.removeUpdates(mListener);

	}

	public void mOnClick (View v) {
		switch (v.getId()) {
		case R.id.gpsicon:
			if(GPSstate == 0)
			{
				locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
				criteria.setPowerRequirement(Criteria.POWER_HIGH);
				mProvider = locationManager.getBestProvider(criteria, true);
				if(mProvider.equals(locationManager.GPS_PROVIDER)) {
					GPSstate = 1;
					getLocation();
				}
			}
			
			if(mProvider.equals(locationManager.GPS_PROVIDER)) {
				if(mlatitude == 37.566352) {
					Toast.makeText(Map.this,mProvider + "  "+"failed to find current location", Toast.LENGTH_SHORT).show();
				}
				else {	
					GeoPoint tgeopoint = new GeoPoint(mlatitude, mlongitude);
					mapView.getController().animateTo(tgeopoint);
				}
			}
			else {
				Toast.makeText(this,"Please Check Your GPS !!", Toast.LENGTH_SHORT).show();
				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);

			}
			break;
			
		case R.id.dessearchbutton:
			db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null, SQLiteDatabase.OPEN_READWRITE+SQLiteDatabase.CREATE_IF_NECESSARY);
			
			String aSQL = "select *"
					+ " from seoulgeoname"
					+ " where NAME like ?";

			String[] args = {""};
			args[0] = desSearchEdit.getText() + "%";

			cursor = db.rawQuery(aSQL, args);
			
			int recordCount = cursor.getCount();
			Toast.makeText(this,"cursor count : " + recordCount, Toast.LENGTH_SHORT).show();
			
			
			startManagingCursor(cursor);
			ListView list = (ListView) findViewById(R.id.list);
			list.setBackgroundColor(Color.rgb(0, 0, 0));
			
			String[] columns = new String[] {"name"};
			int[] to = new int[] { R.id.name_entry };
			
			SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(Map.this, R.layout.items, cursor, columns, to);
			
	        list.setAdapter(mAdapter);
	        list.setOnItemClickListener(mItemClickListener);
	        list.setVisibility(View.VISIBLE);
	        
	        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(desSearchEdit.getWindowToken(),0);
	   			
			//outCursor.close();
			//db.close();
			break;

		default:
			break;
		}
	}
	
	AdapterView.OnItemClickListener mItemClickListener = 
			new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, 
				int position, long id) {
			geoSearchLocation.clean();
			GeoPoint geoPoint = new GeoPoint(cursor.getDouble(2), cursor.getDouble(3));
			mapController.setCenter(geoPoint);
			item = new OverlayItem(cursor.getString(1)," ",geoPoint);
			geoSearchLocation.addItem(item);
			String mes;
			mes = "Select Item = " + cursor.getDouble(2) + "   " + cursor.getDouble(3);
			Toast.makeText(Map.this,mes,Toast.LENGTH_SHORT).show();
			parent.setVisibility(View.INVISIBLE);
		}
	};
	
	public void findShortestSubwayPath(String start, String end)
	{
		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null, SQLiteDatabase.OPEN_READWRITE+SQLiteDatabase.CREATE_IF_NECESSARY);
		

		String aSQL = "select *"
				+ " from seoulgeoname"
				+ " where name = ?";  // 여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.여기 고쳐야함.
		
		String[] args = {""};
		
		double startLa;
		double startLo;
		
		if(start.equals("Current Location"))
		{
			startLa = currentlatitude;
			startLo = currentlongitude;
		}
		else
		{
			args[0] = start;
			
			cursor = db.rawQuery(aSQL, args);
		
			cursor.moveToNext();
				
			startLa = cursor.getDouble(2);
			startLo = cursor.getDouble(3);
		}
		
		double endLa;
		double endLo;
		
		if(end.equals("Current Location"))
		{
			endLa = currentlatitude;
			endLo = currentlongitude;
		}
		else
		{
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

		for(int i=0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			
			la = cursor.getDouble(6);
			lo = cursor.getDouble(7);
			
			if(((startLa-la)*(startLa-la)+(startLo-lo)*(startLo-lo)) < startDistance)
			{
				startDistance = ((startLa-la)*(startLa-la)+(startLo-lo)*(startLo-lo));
				startSubway = cursor.getInt(1);
			}
			if(((endLa-la)*(endLa-la)+(endLo-lo)*(endLo-lo)) < endDistance)
			{
				endDistance = ((endLa-la)*(endLa-la)+(endLo-lo)*(endLo-lo));
				endSubway = cursor.getInt(1);
			}
		}
		Log.i("start", " " + startSubway);
		Log.i("end", " " + endSubway);
		
		subway.Path(startSubway, endSubway, shortest);
		Intent intent = new Intent(this, SubwayActivity.class);
		intent.putExtra("shortestTime", shortest.time);
		intent.putExtra("shortestPath", shortest.path);
		startActivity(intent);
		
			
	}
	
	LocationListener mListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {

			Toast.makeText(Map.this, "리스너호출", Toast.LENGTH_SHORT).show();

			lastLocation = locationManager.getLastKnownLocation(mProvider);
			if(lastLocation == null) {
				Toast.makeText(Map.this,"failed to find current location", Toast.LENGTH_SHORT).show();
				mlatitude = 37.566352;
				mlongitude = 126.978103;
			}
			else {
				mlatitude = lastLocation.getLatitude();
				mlongitude = lastLocation.getLongitude();
				
				item = new OverlayItem("Current Location"," ",
						new GeoPoint(mlatitude, mlongitude));
		//		itemizedoverlay3.clean();
				itemizedoverlay3.addItem(item);
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
			ResourceProxy resourceProxy = (ResourceProxy) new DefaultResourceProxyImpl(mContext);
			mMarker = marker;

			mOverlay = new ItemizedIconOverlay<OverlayItem>(
					items, mMarker, 
					new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
				@Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
					return onSingleTapUpHelper(index, item);
				}
		
				@Override public boolean onItemLongPress(final int index, final OverlayItem item) {
					return true;
				}
			}, resourceProxy);

		}
		
		public boolean onSingleTapUpHelper(int i, final OverlayItem item) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.setNegativeButton("close", null);
			dialog.setPositiveButton("departure", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					departuretext.setText(item.getTitle());
					if(!departuretext.getText().equals("departure") && !arrivaltext.getText().equals("arrival"))
					{
						AlertDialog.Builder dialog2 = new AlertDialog.Builder(mContext);
						dialog2.setTitle("You");
						dialog2.setMessage("Want to find shortest path?");
						dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								findShortestSubwayPath(departuretext.getText().toString(),arrivaltext.getText().toString());
							}
						});
						
						dialog2.setNegativeButton("close", null);
						dialog2.show();
					}
				}
			});
			dialog.setNeutralButton("arrival", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					arrivaltext.setText(item.getTitle());
					if(!departuretext.getText().equals("departure") & !arrivaltext.getText().equals("arrival"))
					{
						AlertDialog.Builder dialog2 = new AlertDialog.Builder(mContext);
						dialog2.setTitle("You");
						dialog2.setMessage("Want to find shortest path?");
						dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								findShortestSubwayPath(departuretext.getText().toString(),arrivaltext.getText().toString());
							}
						});
						dialog2.setNegativeButton("close", null);
						dialog2.show();
					}
				}
			});
			dialog.show();
			
			return true;
		}
		
		public void addItem(OverlayItem item){
			mOverlay.addItem(item);
		}
		
		public void clean(){
			mOverlay.removeAllItems();
		}
		
		public ItemizedIconOverlay<OverlayItem> getOverlay(){
			return mOverlay;
		}
	}

	public void boundCenterBottom(Drawable defaultMarker) {
	}

	public void boundCenter(Drawable mRed2) {
	}
}
