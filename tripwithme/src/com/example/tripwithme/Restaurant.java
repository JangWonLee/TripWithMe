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

public class Restaurant extends Activity {
   RestaurantDBHelper mHelper;
   Typeface mFont;
   
   @SuppressWarnings("deprecation")
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.mlist);
      
      mFont=Typeface.createFromAsset(getAssets(), "fonts/FinenessProBlack.otf");
      
      TextView listname = (TextView)findViewById(R.id.listTitle);
      listname.setText("Restaurent List");
      listname.setTypeface(mFont);
      
      mHelper = new RestaurantDBHelper(this);
      final Cursor cursor;
      SQLiteDatabase db = mHelper.getWritableDatabase();

      cursor = db.rawQuery("SELECT * FROM res2", null);
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
    		  Intent intent = new Intent(Restaurant.this, restDetail.class);
    		  intent.putExtra("name", cursor.getString(cursor.getColumnIndex("name")));
    		  intent.putExtra("intro", cursor.getString(cursor.getColumnIndex("intro")));
    		  intent.putExtra("tel", cursor.getString(cursor.getColumnIndex("tel")));
    		  intent.putExtra("menu", cursor.getString(cursor.getColumnIndex("menu")));
    		  intent.putExtra("time", cursor.getString(cursor.getColumnIndex("time")));
    		  startActivity(intent);
    	  }
	});
   }
}

class RestaurantDBHelper extends SQLiteOpenHelper {
   public RestaurantDBHelper(Context context) {
      super(context, "res2.db", null, 1);
   }

   public void onCreate(SQLiteDatabase db) {
      db.execSQL("CREATE TABLE res2 ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
         "name TEXT, address TEXT, intro TEXT, time TEXT, tel TEXT, menu TEXT);");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Chilryang', '731, Yeongdong-daero, Gangnam-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Deulpul', '24, Daemyeong 1-gil, Jongno-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Yongsusan(Samcheong)', '303, Eonju-ro, Gangnam-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Chungmuro Dwaejigalbi ', '39, Supyo-ro 6-gil, Jung-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Bamboo House', '33, Eonju-ro 107-gil, Gangnam-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'My ex-Wifes Secret Recipe', '136, Sejong-daero, Jung-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Nangpung', 'mapogu sukyodong 366-10', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Chez Mak', '22, Dosan-daero 11-gil, Gangnam-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Taj', '73, Myeongdong-gil, Jung-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Jinjujip', '33, Gukjegeumyung-ro 6-gil, Yeongdeungpo-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Cheolcheol Bokjip', '29, Eulji-ro 3-gil, Jung-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Mui Mui', '19, Dosan-daero 51-gil, Gangnam-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Star Chef', '38-6, Nonhyeon-ro 38-gil, Gangnam-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
      db.execSQL("INSERT INTO res2 VALUES (null, 'Crystal Jade', '14, Myeongdong-gil, Jung-gu', '(Family Restaurant)', 'Open 11:00 ~ Close 23:00', '02-123-4567', 'Steak & Pasta');");
   }

   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS product");
      onCreate(db);
   }
}