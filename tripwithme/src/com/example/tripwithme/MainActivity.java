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

	Typeface mFont;
	Typeface jFont;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        
        jFont=Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
        mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");
        TextView jname = (TextView)findViewById(R.id.name);
        Button engbtn = (Button)findViewById(R.id.eng);
        Button chibtn = (Button)findViewById(R.id.chi);
        Button japbtn = (Button)findViewById(R.id.jap);
        jname.setTypeface(jFont);
        engbtn.setTypeface(mFont);
        chibtn.setTypeface(mFont);
        japbtn.setTypeface(mFont);
        
     //   mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProRegularItalic.ttf");
    }
    
    public void mOnClick(View v) {
    	Intent intent = new Intent(this, search.class);
    	startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
