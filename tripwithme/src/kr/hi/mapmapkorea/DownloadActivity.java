package kr.hi.mapmapkorea;

import com.example.tripwithme.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadActivity extends Activity {

	private Button downloadButton;
	private TextView regionText;
	private String msg;

	ProgressBar downloadProgress;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);

		downloadButton = (Button) findViewById(R.id.downloadbutton);
		downloadProgress = (ProgressBar) findViewById(R.id.downloadprogress);
		regionText = (TextView) findViewById(R.id.regiontext);
		msg = getIntent().getExtras().getString("msg");

		regionText.setText(msg);
		regionText.setVisibility(View.VISIBLE);

	}

	public void mOnClick(View v) {
		downloadProgress.setVisibility(View.VISIBLE);
		// setProgressBarIndeterminateVisibility(true);

		Intent intent = new Intent(this, WebViews.class);
		startActivity(intent);

	}

}
