package com.example.tripwithme;

import android.app.Activity;
import android.app.Fragment;
import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.os.Build;

public class search extends Activity{
	
	Typeface mFont;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlackItalic.otf");  
        EditText searchedit = (EditText)findViewById(R.id.editsearch);
        searchedit.setTypeface(mFont);
    	searchedit.setText("Search FinenessProBlackItalic");
    }
	
	//public class MyView extends View{
		//public MyView(Context context){
			//super(context);
		//}
//		public void onDraw(Canvas canvas){
//			canvas.drawColor(Color.LTGRAY);
//			Paint Pnt=new Paint();
//			Pnt.setTypeface(mFont);
//			String str="ssssss";
//			canvas.drawText(str, 10, 40, Pnt);
//		}
//	}
	
}
	

