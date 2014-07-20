package com.example.tripwithme;

import android.support.v4.app.Fragment;
import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.os.Build;

public class Tour extends Activity {
   TourDBHelper mHelper;
   Typeface mFont;
   
   @SuppressWarnings("deprecation")
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.mlist);
      
      mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");
      
      TextView listname = (TextView)findViewById(R.id.listTitle);
      listname.setText("Tour List");
      listname.setTypeface(mFont);
      
      mHelper = new TourDBHelper(this);
      final Cursor cursor;
      SQLiteDatabase db = mHelper.getWritableDatabase();

      cursor = db.rawQuery("SELECT * FROM tou", null);
      startManagingCursor(cursor);

      SimpleCursorAdapter Adapter = null;
      Adapter = new SimpleCursorAdapter(this, 
            android.R.layout.simple_list_item_2,
            cursor, new String[] { "name", "address" }, 
            new int[] { android.R.id.text1, android.R.id.text2});
      ListView list = (ListView)findViewById(R.id.list);
      list.setAdapter(Adapter);
      
      list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    	  public void onItemClick(AdapterView<?> parent, View v, int position, long id){
    		  Intent intent = new Intent(Tour.this, tourDetail.class);
    		  intent.putExtra("name", cursor.getString(cursor.getColumnIndex("name")));
    		  intent.putExtra("intro", cursor.getString(cursor.getColumnIndex("intro")));
    		  intent.putExtra("tel", cursor.getString(cursor.getColumnIndex("tel")));
    		  intent.putExtra("menu", cursor.getString(cursor.getColumnIndex("menu")));
    		  startActivity(intent);
    	  }
	});
   }
}

class TourDBHelper extends SQLiteOpenHelper {
   public TourDBHelper(Context context) {
      super(context, "tou.db", null, 1);
   }

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE tou ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"name TEXT, address TEXT, intro TEXT, time TEXT, tel TEXT, menu TEXT);");
		db.execSQL("INSERT INTO tou VALUES (null, 'Gyeongbokgung', 'jongrogu sejongro 1-1', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Changdeokgung', 'jongrogu waryongdong 2-71', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'The War Memorial of Korea', 'yongsango yongsandong 1-8', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'N Seoul Tower', 'yongsango yongsandong san1-3', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Bongeunsa', 'gangnamgu samsungdong 73', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Seoul Kimchi Academy House', 'Jung-gu Myeongdong 21-7', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Trick Eye Museum', '20, Hongik-ro 3-gil, Mapo-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Hangaram Art Museum', '2406, Nambusunhwan-ro, Seocho-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Seoul Central Mosque', '39, Usadan-ro 10-gil, Yongsan-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Hongneung Forest', '57, Hoegi-ro, Dongdaemun-gu, Seoul ', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Olympic Park', '424, Olympic-ro, Songpa-gu, Seoul', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Naksan Public Art Project', '7-9, Naksan 3-gil, Jongno-gu, Seoul', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Beautiful Tea Museum', '19-11, Insadong-gil, Jongno-gu, Seoul', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
		db.execSQL("INSERT INTO tou VALUES (null, 'Gyeonghuigung Palace', '45, Saemunan-ro, Jongno-gu, Seoul', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
}

   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS product");
      onCreate(db);
   }
}