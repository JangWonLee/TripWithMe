package com.example.tripwithme;


import android.app.*;
import android.content.*;
import android.graphics.*;
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
import android.widget.EditText;


public class WebViews extends Activity {
	
	WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://www.dropbox.com/s/0db6us9vxg7fdmi/Seoul.sqlitedb?dl=0 ");
        
        webview.setWebViewClient(new WebViewClientClass());
    
    }
    
    
    private class WebViewClientClass extends WebViewClient {
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		view.loadUrl(url);
    		return true;
    	}
    }
}

