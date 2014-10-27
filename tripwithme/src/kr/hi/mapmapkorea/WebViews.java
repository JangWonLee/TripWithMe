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
	
	private Typeface mFont;
	private Typeface jFont;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webviews);
		
        jFont=Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
        mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");
		
		mViewHelper = new ViewHelper(this);
		View webviewsLayout = findViewById(R.id.tab2);
		mViewHelper.setGlobalSize((ViewGroup) webviewsLayout);

		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("https://docs.google.com/file/d/0B0vdbaa0j01ySkl5RzVIV1dtRzA/edit?usp=docslist_api");

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