package com.example.tripwithme;

import java.io.*;
import java.util.*;

import idv.hondadai.offlinemap.views.OfflineMapView;

import org.osmdroid.*;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.*;



//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapController;
//import com.google.android.maps.MapView;
//import com.google.android.maps.Overlay;
//import com.google.android.maps.Projection;

import android.*;
import android.app.Activity;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.location.*;
import android.os.*;
import android.view.*;
import android.widget.*;


public class map extends Activity {
	// layout
	private RelativeLayout mapLayout;

	// MapView
	private MapView mapView;
	Drawable mRest, mTour, mNow;
	private MapController mapController;
	private ResourceProxy proxy;
	LocationManager mLocMan;
	String mProvider;
	TextView mResult;
	double mlatitude;
	double mlongitude;
	ImageView dest;
	List<Overlay> overlayss; 

	Location lastlocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init Layout
		setContentView(R.layout.map);

		EditText dessearch = (EditText)findViewById(R.id.desSearch);
		dessearch.setHint("Search");


		this.mapLayout = (RelativeLayout)findViewById(R.id.mapLayout);

		// init Offline Map
		
		
		
		this.mapView = new OfflineMapView(this, "TripWithMe/Seoul.sqlitedb");
		this.mapController = mapView.getController();

		// set Zoom Countrol
		this.mapView.setBuiltInZoomControls(true);      // set Touch Control
		this.mapView.setMultiTouchControls(true);

		// zoom to 15
		this.mapController.setZoom(15);

		// add mapview
		this.mapLayout.addView(this.mapView, new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));

		// scroll to 24082456, 120558472
		GeoPoint geoPoint = new GeoPoint(37.550120, 126.924606);
		this.mapController.setCenter(geoPoint);



		// �쐞移� 愿�由ъ옄 援ы븿
		mLocMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria cri = new Criteria();
		cri.setAccuracy(Criteria.ACCURACY_FINE);
		cri.setPowerRequirement(Criteria.POWER_HIGH);
		mProvider = mLocMan.getBestProvider(cri, true);


		List<Overlay> overlays = mapView.getOverlays();

		mRest = getResources().getDrawable(R.drawable.bluemarker);
		mRest.setBounds(0, 0, mRest.getIntrinsicWidth(),mRest.getIntrinsicHeight());
		mTour = getResources().getDrawable(R.drawable.touricon);
		mTour.setBounds(0, 0, mTour.getIntrinsicWidth(), mTour.getIntrinsicHeight());
		mNow = getResources().getDrawable(R.drawable.currentpositionicon);
		mNow.setBounds(0, 0, mNow.getIntrinsicWidth(),mNow.getIntrinsicHeight());


		overlayss = mapView.getOverlays();

		Restaurant rest = new Restaurant(mRest,this);
		List<Overlay> overlays1 = mapView.getOverlays();
		overlays1.add(rest);

		Tour tour = new Tour(mTour,this);
		List<Overlay> overlays2 = mapView.getOverlays();
		overlays2.add(tour);
	}


	public void mOnClick(View v) {
		GeoPoint tgeopoint = new GeoPoint(mlatitude, mlongitude);
		mapView.getController().animateTo(tgeopoint);

	}

	public void onResume() {

		super.onResume();
		mLocMan.requestLocationUpdates(mProvider, 3000, 5, mListener);
		lastlocation = mLocMan.getLastKnownLocation(mProvider);
		mlatitude = lastlocation.getLatitude();
		mlongitude = lastlocation.getLongitude();

		Toast.makeText(map.this, "�뼚�씪�뼚�씪�뼚�씪", Toast.LENGTH_LONG).show();

		MyPosition myposition = new MyPosition(mNow, map.this);
		//List<Overlay> overlayss = mapView.getOverlays();
		//overlayss.add(myposition);

		//overlayss.clear();
		overlayss.add(myposition);
		///


	} 


	public void onPause() {      
		super.onPause();
		mLocMan.removeUpdates(mListener);
	}


	LocationListener mListener = new LocationListener() {
		public void onLocationChanged(Location location) {

			String sloc = "�쐞�룄: " + location.getLatitude() + "\n寃쎈룄: " + location.getLongitude();
			Toast.makeText(map.this, "由ъ뒪�꼫�샇異�", Toast.LENGTH_LONG).show();
			//mNow = getResources().getDrawable(R.drawable.currentpositionicon);
			//mNow.setBounds(mNow.getIntrinsicWidth(), mNow.getIntrinsicHeight(),0,0);
			mResult.setText(sloc);

			MyPosition myposition = new MyPosition(mNow, map.this);
			//List<Overlay> overlayss = mapView.getOverlays();
			//overlayss.add(myposition);

			overlayss.clear();
			overlayss.add(myposition);

		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub	
		}
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub	
		}
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub	
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
				item = new OverlayItem("諛붿��씫 �쟾臾�", 
						"�븷留� 移쇨뎅�닔", new GeoPoint(37.522815, 127.054492));
				break;
			case 1:
				item = new OverlayItem("臾쇱� ���봽", 
						"媛뺣궓 �뼞蹂띠씠", new GeoPoint(37.582500, 127.000577));
				break;
			case 2:
				item = new OverlayItem("源�移� 臾댁젣�븳", 
						"�꽌珥� �씪硫�", new GeoPoint(37.494534, 127.046081));
				break;
			case 3:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.562832, 126.992598));
				break;
			case 4:        
				item = new OverlayItem("移쒖젅 遊됱궗", 
						"誘몄쁺 遺꾩떇", new GeoPoint(37.506996, 127.036646));
				break;
			case 5:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.568381, 126.978270));
				break;
			case 6:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.558232, 126.934877));
				break;
			case 7:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.518842, 127.022655));
				break;
			case 8:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.564315, 126.986183));
				break;
			case 9:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.520769, 126.926904));
				break;
			case 10:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.567325, 126.981330));
				break;
			case 11:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.524367, 127.037646));
				break;
			case 12:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.485187, 127044877));
				break;
			case 13:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.563394, 126.982901));
				break;

			}
			return  item;
		}

		public boolean onTap(int index) {
			String msg;
			OverlayItem item = getItem(index);
			msg = "�긽�샇 = " + item.getTitle() + ",�꽕紐� = " + item.getSnippet();
			Toast.makeText(map.this, "tappppmsg", Toast.LENGTH_LONG).show();
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
				item = new OverlayItem("諛붿��씫 �쟾臾�", 
						"�븷留� 移쇨뎅�닔", new GeoPoint(37.579455, 126.977030));
				break;
			case 1:
				item = new OverlayItem("臾쇱� ���봽", 
						"媛뺣궓 �뼞蹂띠씠", new GeoPoint(37.579393, 126.991166));
				break;
			case 2:
				item = new OverlayItem("源�移� 臾댁젣�븳", 
						"�꽌珥� �씪硫�", new GeoPoint(37.536944, 126.977394));
				break;
			case 3:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.550871, 126.987669));
				break;
			case 4:
				item = new OverlayItem("移쒖젅 遊됱궗", 
						"誘몄쁺 遺꾩떇", new GeoPoint(37.515243, 127.057377));
				break;
			case 5:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.567053, 126.979288));
				break;
			case 6:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.553442, 126.921685));
				break;
			case 7:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.478588, 127.011285));
				break;
			case 8:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.533245, 126.997541));
				break;
			case 9:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.593085, 127.043650));
				break;
			case 10:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.521759, 127.116616));
				break;
			case 11:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.580758, 127.006397));
				break;
			case 12:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.572294, 126.985897));
				break;
			case 13:
				item = new OverlayItem("���졃�븳 媛�寃�", 
						"�븳鍮� 留뚮몢", new GeoPoint(37.571182, 126.968792));
				break;

			}
			return  item;
		}

		public boolean onTap(int index) {
			String msg;
			OverlayItem item = getItem(index);
			msg = "�긽�샇 = " + item.getTitle() + ",�꽕紐� = " + item.getSnippet();
			Toast.makeText(map.this, "tappppmsg", Toast.LENGTH_LONG).show();
			return true;
		}

		@Override
		public boolean onSnapToItem(int arg0, int arg1, Point arg2, MapView arg3) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	class MyPosition extends ItemizedOverlay<OverlayItem> {
		//LocationManager mLocMan;
		//String mProvider;

		public MyPosition(Drawable defaultMarker, Context pContext) {
			super(defaultMarker, new DefaultResourceProxyImpl(pContext) );
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
				Location lastlocation = mLocMan.getLastKnownLocation(mProvider);
				mlatitude = lastlocation.getLatitude();
				mlongitude = lastlocation.getLongitude();
				item = new OverlayItem(" ", 
						" ", new GeoPoint(mlatitude, mlongitude));
				item.setMarker(mNow);
				break;
			}
			return  item;
		}

		public boolean onTap(int index) {
			String msg;
			OverlayItem item = getItem(index);
			msg = "�긽�샇 = " + item.getTitle() + ",�꽕紐� = " + item.getSnippet();
			Toast.makeText(map.this, msg, Toast.LENGTH_LONG).show();
			return true;
		}

		@Override
		public boolean onSnapToItem(int arg0, int arg1, Point arg2, MapView arg3) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	public void boundCenterBottom(Drawable defaultMarker) {
		// TODO Auto-generated method stub

	}

	public void boundCenter(Drawable mRed2) {
		// TODO Auto-generated method stub

	}
}
