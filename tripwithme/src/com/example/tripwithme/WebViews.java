package com.example.tripwithme;


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
	
	WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviews);

        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://docs.google.com/file/d/0B0vdbaa0j01ySkl5RzVIV1dtRzA/edit?usp=docslist_api");
        
        webview.setWebViewClient(new WebViewClientClass());
        
        webview.setDownloadListener(new DownloadListener() {
        	public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength){
        		Uri uri = Uri.parse(url);
        		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        		viewIntent.setDataAndType(Uri.parse(url), mimetype);
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

