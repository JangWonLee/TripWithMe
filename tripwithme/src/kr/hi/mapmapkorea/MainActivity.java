package kr.hi.mapmapkorea;

import kr.hi.mapmapkorea.util.ViewHelper;

import com.example.tripwithme.R;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ViewHelper mViewHelper;

	private Typeface mFont;
	private Typeface jFont;
	private Typeface kFont;

	private Button englishButton;
	private Button chinaButton;
	private Button japanButton;
	private TextView selectLanguageText;

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mViewHelper = new ViewHelper(this);
		View mainLayout = findViewById(R.id.main_layout);
		mViewHelper.setGlobalSize((ViewGroup) mainLayout);

		jFont = Typeface.createFromAsset(getAssets(), "fonts/chubgothic_1.ttf");
		mFont = Typeface.createFromAsset(getAssets(),
				"fonts/Anysome Italic.otf");
		kFont = Typeface.createFromAsset(getAssets(),
				"fonts/dearJoe 6 TRIAL.otf");

		englishButton = (Button) findViewById(R.id.englishbutton);
		chinaButton = (Button) findViewById(R.id.chinabutton);
		japanButton = (Button) findViewById(R.id.japanbutton);
		selectLanguageText = (TextView) findViewById(R.id.selectlanguagetext);

		englishButton.setTypeface(mFont);
		chinaButton.setTypeface(mFont);
		japanButton.setTypeface(mFont);
		selectLanguageText.setTypeface(kFont);
	}

	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.englishbutton:
			intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.chinabutton:
			Toast.makeText(MainActivity.this, "Comming soon..",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.japanbutton:
			Toast.makeText(MainActivity.this, "Comming soon..",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
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
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// /**
	// * A placeholder fragment containing a simple view.
	// */
	// public static class PlaceholderFragment extends Fragment {
	//
	// public PlaceholderFragment() {
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View rootView = inflater.inflate(R.layout.fragment_main, container,
	// false);
	// return rootView;
	// }
	// }

}
