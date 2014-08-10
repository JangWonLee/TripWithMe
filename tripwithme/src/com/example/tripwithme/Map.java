package com.example.tripwithme;

import idv.hondadai.offlinemap.views.OfflineMapView;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
	private double currentLatitude;
	private double currentLongitude;
	
	private ImageView dest;
	private EditText mtext;
	private EditText desSearchEdit;
	private Button mbtn;
	
	private SQLiteDatabase db;
	
	private List<Overlay> mOverlay; 

	private Location lastLocation;
	private String geonameDatabaseFile = "/sdcard/TripWithMe/DATA.sqlite";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init Layout
		setContentView(R.layout.map);

		mbtn = (Button)findViewById(R.id.dessearchbutton);
		mtext = (EditText)findViewById(R.id.dessearchedit);
		
		desSearchEdit = (EditText)findViewById(R.id.dessearchedit);
		desSearchEdit.setHint("Search");

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



/*
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		mProvider = locationManager.getBestProvider(criteria, true);
*/
		// 초기 위치 지정
//		getLocation();
//		GeoPoint geoPoint = new GeoPoint(currentLatitude, currentLongitude);
//		this.mapController.setCenter(geoPoint);
		
		mRest = getResources().getDrawable(R.drawable.bluemarker);
		mRest.setBounds(0, 0, mRest.getIntrinsicWidth(),mRest.getIntrinsicHeight());
		mTour = getResources().getDrawable(R.drawable.touricon);
		mTour.setBounds(0, 0, mTour.getIntrinsicWidth(), mTour.getIntrinsicHeight());
		mNow = getResources().getDrawable(R.drawable.currentpositionicon);
		mNow.setBounds(0, 0, mNow.getIntrinsicWidth(),mNow.getIntrinsicHeight());


//		mOverlay = mapView.getOverlays();
		
		
		
		

		MyOwnItemizedOverlay itemizedoverlay1 = new MyOwnItemizedOverlay(mRest, this);

		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null, SQLiteDatabase.OPEN_READWRITE+SQLiteDatabase.CREATE_IF_NECESSARY);
		
		Cursor cursor = db.rawQuery("SELECT * FROM restaurant", null);
		OverlayItem item = null;
		for(int i=0; i < cursor.getCount(); i++)
		{
			cursor.moveToNext();
			item = new OverlayItem(cursor.getString(1), cursor.getString(2),
			new GeoPoint(cursor.getDouble(11), cursor.getDouble(12)));
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
			itemizedoverlay2.addItem(item);
		}
		
		

		
		mapView.getOverlays().add(itemizedoverlay2.getOverlay());
		
		db.close();
		
		

	}

	@Override
	public void onResume() {
		super.onResume();
/*		getLocation();

		MyPosition myPosition = new MyPosition(mNow, Map.this);
		//mOverlay.clear();
		mOverlay.add(myPosition);*/
	}

	private void getLocation() {
//		if(mProvider != null) {
/*			locationManager.requestLocationUpdates(mProvider, 3000, 5, mListener);
			lastLocation = locationManager.getLastKnownLocation(mProvider);
			mlatitude = lastLocation.getLatitude();
			mlongitude = lastLocation.getLongitude();
		
			currentLatitude = mlatitude;
			currentLongitude = mlongitude;*/
//		}
	} 

	@Override
	public void onPause() {     

		super.onPause();
//		locationManager.removeUpdates(mListener);

	}

	public void mOnClick (View v) {
		switch (v.getId()) {
		case R.id.gpsicon:
			GeoPoint tgeopoint = new GeoPoint(mlatitude, mlongitude);
			mapView.getController().animateTo(tgeopoint);
			break;
			
		case R.id.dessearchbutton:
			String geonameDatabaseFile = "/sdcard/TripWithMe/DATA.sqlite";
			db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null, SQLiteDatabase.OPEN_READWRITE+SQLiteDatabase.CREATE_IF_NECESSARY);
			
			String aSQL = "select ID, NAME, LATITUDE, LONGITUDE "
					+ " from DATA"
					+ " where NAME like ?";

			String[] args = {""};
			args[0] = mtext.getText() + "%";

			Cursor outCursor = db.rawQuery(aSQL, args);
			
			int recordCount = outCursor.getCount();
			Toast.makeText(this,"cursor count : " + recordCount, Toast.LENGTH_SHORT).show();
			
			
			startManagingCursor(outCursor);
			ListView list = new ListView(Map.this);
			
			String[] columns = new String[] {"name", "latitude", "longitude"};
			int[] to = new int[] { R.id.name_entry, R.id.latitude_entry, R.id.longitud_entry };
			
//			SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.items, outCursor, columns, to);
			
		
	   //     list.setAdapter(mAdapter);
	    
	    //    mapView.addView(list);
			

			outCursor.close();
			db.close();
			break;

		default:
			break;
		}
	}
	
	LocationListener mListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {

			Toast.makeText(Map.this, "리스너호출", Toast.LENGTH_LONG).show();
			//mNow = getResources().getDrawable(R.drawable.currentpositionicon);
			//mNow.setBounds(mNow.getIntrinsicWidth(), mNow.getIntrinsicHeight(),0,0);

			MyPosition myposition = new MyPosition(mNow, Map.this);
			//List<Overlay> mOverlay = mapView.getOverlays();
			//mOverlay.add(myposition);

			mOverlay.clear();
			mOverlay.add(myposition);
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
		
		public boolean onSingleTapUpHelper(int i, OverlayItem item) {
			Toast.makeText(mContext, "Item " + i + " has been tapped!", Toast.LENGTH_SHORT).show();
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
			return true;
		}
		
		public void addItem(OverlayItem item){
			mOverlay.addItem(item);
		}
		
		public ItemizedIconOverlay<OverlayItem> getOverlay(){
			return mOverlay;
		}
	}


	class MyPosition extends ItemizedOverlay<OverlayItem> {
		//LocationManager locationManager;
		//String mProvider;

		public MyPosition(Drawable defaultMarker, Context context) {
			super(defaultMarker, new DefaultResourceProxyImpl(context) );
			boundCenterBottom(defaultMarker);
			boundCenter(mNow);
			populate();
		}
		public int size() {
			return 1;
		}
		protected OverlayItem createItem(int i) {
			OverlayItem item = null;

			switch (i) {

			case 0:
				Location lastLocation = locationManager.getLastKnownLocation(mProvider);
				mlatitude = lastLocation.getLatitude();
				mlongitude = lastLocation.getLongitude();
				item = new OverlayItem(" ", " ", new GeoPoint(mlatitude, mlongitude));
				item.setMarker(mNow);
				break;
			}
			return  item;
		}

		public boolean onTap(int index) {
			String msg;
			OverlayItem item = getItem(index);
			msg = "?긽?샇 = " + item.getTitle() + ",?꽕紐? = " + item.getSnippet();
			Toast.makeText(Map.this, msg, Toast.LENGTH_LONG).show();
			return true;
		}

		@Override
		public boolean onSnapToItem(int arg0, int arg1, Point arg2, MapView arg3) {
			return false;
		}
	}

	public void boundCenterBottom(Drawable defaultMarker) {
	}

	public void boundCenter(Drawable mRed2) {
	}
}
