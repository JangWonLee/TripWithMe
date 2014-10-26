package kr.hi.mapmapkorea;

import com.example.tripwithme.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SubwayActivity extends Activity {

	String start;
	String end;
	TextView shortestTimeText;
	TextView shortestPathText;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway);
        shortestTimeText = (TextView)findViewById(R.id.shortesttime);
        shortestPathText = (TextView)findViewById(R.id.shortestpath);
        start = getIntent().getExtras().getString("shortestTime");
		end = getIntent().getExtras().getString("shortestPath");
		shortestTimeText.setText(start);
    	shortestPathText.setText(end);
    }
    
}
