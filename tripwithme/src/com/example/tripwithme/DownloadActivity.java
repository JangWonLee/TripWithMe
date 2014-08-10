package com.example.tripwithme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownloadActivity extends Activity {
	
	private Button downloadButton;

	ProgressBar downloadProgress;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);

        
        downloadButton = (Button)findViewById(R.id.downloadbutton);
        
        downloadProgress = (ProgressBar)findViewById(R.id.downloadprogress);
                
	}
	
	public void mOnClick(View v) {
		downloadProgress.setVisibility(View.VISIBLE);
		setProgressBarIndeterminateVisibility(true);
	}

}