package com.example.tripwithme;

import idv.hondadai.offlinemap.views.OfflineMapView;

import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapController;
//import com.google.android.maps.MapView;
//import com.google.android.maps.Overlay;
//import com.google.android.maps.Projection;


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
	private Button mbtn;
	
	private SQLiteDatabase db;
	
	private List<Overlay> mOverlay; 

	private Location lastLocation;
	private String geonameDatabaseFile = "TripWithMe/DATA.sqlite";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init Layout
		setContentView(R.layout.map);

		mbtn = (Button)findViewById(R.id.m_btn);
		mtext = (EditText)findViewById(R.id.desSearch);
		
		EditText dessearch = (EditText)findViewById(R.id.desSearch);
		dessearch.setHint("Search");

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

		// 초기 위치 지정
		getLocation();
		GeoPoint geoPoint = new GeoPoint(currentLatitude, currentLongitude);
		this.mapController.setCenter(geoPoint);
		
		mRest = getResources().getDrawable(R.drawable.bluemarker);
		mRest.setBounds(0, 0, mRest.getIntrinsicWidth(),mRest.getIntrinsicHeight());
		mTour = getResources().getDrawable(R.drawable.touricon);
		mTour.setBounds(0, 0, mTour.getIntrinsicWidth(), mTour.getIntrinsicHeight());
		mNow = getResources().getDrawable(R.drawable.currentpositionicon);
		mNow.setBounds(0, 0, mNow.getIntrinsicWidth(),mNow.getIntrinsicHeight());


		mOverlay = mapView.getOverlays();

		Restaurant rest = new Restaurant(mRest,this);
		List<Overlay> overlays1 = mapView.getOverlays();
		overlays1.add(rest);

		Tour tour = new Tour(mTour,this);
		List<Overlay> overlays2 = mapView.getOverlays();
		overlays2.add(tour);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		getLocation();

		MyPosition myPosition = new MyPosition(mNow, Map.this);
		//mOverlay.clear();
		mOverlay.add(myPosition);
	}

	private void getLocation() {
		locationManager.requestLocationUpdates(mProvider, 3000, 5, mListener);
		lastLocation = locationManager.getLastKnownLocation(mProvider);
		mlatitude = lastLocation.getLatitude();
		mlongitude = lastLocation.getLongitude();
		
		currentLatitude = mlatitude;
		currentLongitude = mlongitude;
	} 

	@Override
	public void onPause() {      
		super.onPause();
		locationManager.removeUpdates(mListener);
	}

	public void mOnClick (View v) {
		switch (v.getId()) {
		case R.id.gpsicon:
			GeoPoint tgeopoint = new GeoPoint(mlatitude, mlongitude);
			mapView.getController().animateTo(tgeopoint);
			break;
			
		case R.id.m_btn:
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


	class Restaurant extends ItemizedOverlay<OverlayItem> {

		public Restaurant(Drawable defaultMarker, Context pContext) {
			super(defaultMarker, new DefaultResourceProxyImpl(pContext) );
			boundCenterBottom(defaultMarker);
			boundCenter(mRest);
			populate();
		}
		public int size() {
			return 14;
		}
		protected OverlayItem createItem(int i) {
			OverlayItem item = null;

			switch (i) {
			case 0:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.522815, 127.054492));
				break;
			case 1:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.582500, 127.000577));
				break;
			case 2:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.494534, 127.046081));
				break;
			case 3:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.562832, 126.992598));
				break;
			case 4:        
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.506996, 127.036646));
				break;
			case 5:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.568381, 126.978270));
				break;
			case 6:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.558232, 126.934877));
				break;
			case 7:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.518842, 127.022655));
				break;
			case 8:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.564315, 126.986183));
				break;
			case 9:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.520769, 126.926904));
				break;
			case 10:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.567325, 126.981330));
				break;
			case 11:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.524367, 127.037646));
				break;
			case 12:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.485187, 127044877));
				break;
			case 13:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.563394, 126.982901));
				break;

			}
			return  item;
		}

		public boolean onTap(int index) {
			String msg;
			OverlayItem item = getItem(index);
			msg = "�긽�샇 = " + item.getTitle() + ",�꽕紐� = " + item.getSnippet();
			Toast.makeText(Map.this, msg,Toast.LENGTH_LONG).show();
			return true;
		}

		@Override
		public boolean onSnapToItem(int arg0, int arg1, Point arg2, MapView arg3) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	

	class Tour extends ItemizedOverlay<OverlayItem> {

		public Tour(Drawable defaultMarker, Context pContext) {
			super(defaultMarker, new DefaultResourceProxyImpl(pContext) );
			boundCenterBottom(defaultMarker);
			boundCenter(mTour);
			populate();        	
		}
		public int size() {
			return 14;
		}
		protected OverlayItem createItem(int i) {
			OverlayItem item = null;

			switch (i) {
			case 0:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.579455, 126.977030));
				break;
			case 1:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.579393, 126.991166));
				break;
			case 2:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.536944, 126.977394));
				break;
			case 3:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.550871, 126.987669));
				break;
			case 4:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.515243, 127.057377));
				break;
			case 5:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.567053, 126.979288));
				break;
			case 6:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.553442, 126.921685));
				break;
			case 7:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.478588, 127.011285));
				break;
			case 8:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.533245, 126.997541));
				break;
			case 9:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.593085, 127.043650));
				break;
			case 10:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.521759, 127.116616));
				break;
			case 11:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.580758, 127.006397));
				break;
			case 12:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.572294, 126.985897));
				break;
			case 13:
				item = new OverlayItem("상호", 
						"설명", new GeoPoint(37.571182, 126.968792));
				break;

			}
			return  item;
		}

		public boolean onTap(int index) {
			String msg;
			OverlayItem item = getItem(index);
			msg = "�긽�샇 = " + item.getTitle() + ",�꽕紐� = " + item.getSnippet();
			Toast.makeText(Map.this, "tappppmsg", Toast.LENGTH_LONG).show();
			return true;
		}

		@Override
		public boolean onSnapToItem(int arg0, int arg1, Point arg2, MapView arg3) {
			// TODO Auto-generated method stub
			return false;
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
			msg = "�긽�샇 = " + item.getTitle() + ",�꽕紐� = " + item.getSnippet();
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
