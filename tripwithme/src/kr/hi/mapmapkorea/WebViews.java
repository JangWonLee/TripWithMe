package kr.hi.mapmapkorea;

import kr.hi.mapmapkorea.util.ViewHelper;

import com.example.tripwithme.R;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.DownloadListener;
import android.widget.EditText;
import android.net.Uri;
import android.content.*;

public class WebViews extends Activity {
	private ViewHelper mViewHelper;

	private WebView webview;
	
	private String urlAddress;

	private Typeface mFont;
	private Typeface jFont;
	private Typeface kFont;
	private boolean isMap;
	
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webviews);

		jFont = Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
		mFont = Typeface.createFromAsset(getAssets(),
				"fonts/FinenessProBlack.otf");
		kFont = Typeface.createFromAsset(getAssets(), "fonts/YoureInvited.otf");

		mViewHelper = new ViewHelper(this);
		View webviewsLayout = findViewById(R.id.tab2);
		mViewHelper.setGlobalSize((ViewGroup) webviewsLayout);

		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		
		int cityNumber = getIntent().getIntExtra("CityToWebview", 9);
		isMap = getIntent().getExtras().getBoolean("isMap");
		
		intent = new Intent(WebViews.this, SearchActivity.class);
		
		switch (cityNumber) {
		case 0:
			if(isMap)
				urlAddress = "https://docs.google.com/file/d/0B0vdbaa0j01ySkl5RzVIV1dtRzA/edit?usp=docslist_api";
			else
				urlAddress = "https://docs.google.com/file/d/0B0vdbaa0j01yVEh3MG9PckJRTEk/edit?usp=docslist_api";
			intent.putExtra("Downloading", 0);
			break;
		case 1:
			if(isMap)
				urlAddress = "https://docs.google.com/file/d/0B0vdbaa0j01yOTRadXVBWWxHeGs/edit?usp=docslist_api";
			else
				urlAddress = "https://docs.google.com/file/d/0B0vdbaa0j01ydldXZklzLWs5SEE/edit?usp=docslist_api";
				;
			intent.putExtra("Downloading", 1);
			break;
		case 2:
			if(isMap)
				urlAddress = "";
			else
				;
			intent.putExtra("Downloading", 2);
			break;
		default:
			break;
		}

//		webview.loadUrl("https://docs.google.com/file/d/0B0vdbaa0j01ySkl5RzVIV1dtRzA/edit?usp=docslist_api");
		webview.loadUrl(urlAddress);
		
		webview.setWebViewClient(new WebViewClientClass());

		webview.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				// viewIntent.setDataAndType(Uri.parse(url), mimetype);
				startActivity(intent);
			}

		});

	}

	private class WebViewClientClass extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
