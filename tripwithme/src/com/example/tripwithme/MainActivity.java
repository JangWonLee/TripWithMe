package com.example.tripwithme;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Typeface mFont;
	private Typeface jFont;
	
	private Button englishButton;
	private Button chinaButton;
	private Button japanButton;
	
	private Intent intent;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        
        jFont=Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
        mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");
        englishButton = (Button)findViewById(R.id.englishbutton);
        chinaButton = (Button)findViewById(R.id.chinabutton);
        japanButton = (Button)findViewById(R.id.japanbutton);
        englishButton.setTypeface(mFont);
        chinaButton.setTypeface(mFont);
        japanButton.setTypeface(mFont);
        
    }
    
    public void mOnClick(View v) {
    	intent = new Intent(this, SearchActivity.class);
    	startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
   

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
        		Bundle savedInstanceState) {
        	View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        	return rootView;
        }
    }

}
