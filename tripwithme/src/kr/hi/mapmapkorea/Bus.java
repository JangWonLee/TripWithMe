package kr.hi.mapmapkorea;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Bus {
	public int bus[][] = new int[10536][655]; // 지하철 노선도 인접 행렬
	private int myWay[][] = new int[10536][655];
	private int busline[][] = new int[655][179];

	private SQLiteDatabase db;
	private String geonameDatabaseFile = "/sdcard/Download/busstop2.sqlite";

	public Bus() // Bus ����(������� �ʱ�ȭ)
	{
		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
				SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.CREATE_IF_NECESSARY);
		Cursor cursor;
		cursor = db.rawQuery("select * FROM busstop2", null);
		cursor.moveToNext();
		for (int i = 0; i < 10536; i++)
			for (int j = 0; j < 655; j++)
				bus[i][j] = 9999;
		for (int i = 0; i < 655; i++)
			for (int j = 0; j < 179; j++)
				busline[i][j] = -1;

		for (int i = 0; i < 31631; i++) {
			bus[cursor.getInt(7)][cursor.getInt(1)] = cursor.getInt(3);
			busline[cursor.getInt(1)][cursor.getInt(3)] = cursor.getInt(7);
			cursor.moveToNext();
		}
	}

	public void search(int startStopNum, int endStopNum, Shortest shortest) {
		ArrayList<Integer> station = new ArrayList<Integer>();
		for (int i = 0; i < 10536; i++)
			for (int j = 0; j < 655; j++)
				myWay[i][j] = 9999;
		/**
		 * 첫 정류장 0 초기화
		 */
		for (int i = 0; i < 655; i++) { 
			if (bus[startStopNum][i] != 9999) {
				myWay[startStopNum][i] = 0;
				compare(startStopNum, i);
				station.add(i);
			}
		}

		int minTime1 = 9999, minTime2 = 9999;
		int minLine = 0;
		
		for(int c=0; c<10; c++){

			Log.i("while", "while Start");
			int count = 0;
			for (int i = 0; i < 10536; i++) {
				int min = 9999;
				for (int j = 0; j < 655; j++) {
					if (myWay[i][j] < min) {
						min = myWay[i][j];
					}
				}
				for (int j = 0; j < 655; j++) {
					if (bus[i][j] != 9999 && myWay[i][j] - min > 10) {

						if(i==24 && j==353)
							Log.i("353-24 바뀌기 전",""+ myWay[i][j]);
						myWay[i][j] = min + 10;   								// 환승 거리!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
						if(i==24 && j==353)
							Log.i("353-24 바뀐 후",""+ myWay[i][j]);
						compare(i,j);
					}
				}
				for (int j = 0; j < 655; j++) {
					if (myWay[endStopNum][j] != 9999) {
						minTime2 = myWay[endStopNum][j];
						minLine = j;
					}
				}

			}
			if (minTime2 == minTime1 && minTime2 != 9999)
				break;
			else
				minTime1 = minTime2;
			
		}
		int stopNum = endStopNum;
		int stopNumOfLine = 0;
		int pathAry[] = new int[500];
		int lineAry[] = new int[500];
		for(int i=178; i>0; i--) {
			if(busline[minLine][i] == endStopNum) {
				stopNumOfLine = i;
				break;
			}
		}
		pathAry[0] = stopNum;
		lineAry[0] = minLine;
		shortest.pathCount = 1;
		shortest.transferCount = 0;
		
		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
				SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.CREATE_IF_NECESSARY);
		
		Cursor cursor;
		String sql = "select line, name FROM busstop2 Where line_id = ? and busstop_id = ?";
		String[] args = { "" ,""};
		args[0] = minLine+"";
		args[1] = stopNum+"";
		cursor = db.rawQuery(sql, args);
		cursor.moveToNext();

		shortest.totalPath = "Line " + cursor.getString(0) +" : "+cursor.getString(1);
		shortest.briefPath = "Line " + cursor.getString(0) +" : "+cursor.getString(1);
		
		stopNumOfLine--;
		for(int i = minTime2-1; i > -1; i--) {
//			if(stopNumOfLine == -1)//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
//				Log.i("minLine stop",""+ minLine+"  "+stopNumOfLine);
//			else if(busline[minLine][stopNumOfLine] == -1)//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
//				Log.i("stopNumOfLine",""+ stopNumOfLine);
//			else 
			if(myWay[busline[minLine][stopNumOfLine]][minLine] == i) {
				Log.i("minLine",""+ i);
				pathAry[shortest.pathCount] = busline[minLine][stopNumOfLine];
				lineAry[shortest.pathCount] = minLine;
				shortest.pathCount++;
				
				sql = "select line, name FROM busstop2 Where line_id = ? and busstop_id = ?";
				args[0] = minLine+"";
				args[1] = busline[minLine][stopNumOfLine]+"";
				cursor = db.rawQuery(sql, args);
				cursor.moveToNext();
				shortest.totalPath ="Line " + cursor.getString(0) +" : "+cursor.getString(1) +"\n" + shortest.totalPath ;
				
				stopNumOfLine--;
			}
			else {
				i -= 9;																	// 환승 거리!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
				Log.i("minLinekkkk",""+ i);
				for(int j=0; j<655; j++) {
					String temp = "";
					Log.i(j+"  "+i,""+ myWay[busline[minLine][stopNumOfLine+1]][j]);
					if(myWay[busline[minLine][stopNumOfLine+1]][j] == i) {
						for(int k=178; k>0; k--) {
							Log.i("minLinekkkk","" + busline[j][k] +"  "+ busline[minLine][stopNumOfLine+1]);
							if(busline[j][k] == busline[minLine][stopNumOfLine+1]) {
								temp = " -> "+cursor.getString(0);
								stopNumOfLine = k;
								break;
							}
						}
						minLine = j;
						shortest.transferCount++;
						sql = "select line, name FROM busstop2 Where line_id = ? and busstop_id = ?";
						args[0] = minLine+"";
						args[1] = busline[minLine][stopNumOfLine]+"";
						cursor = db.rawQuery(sql, args);
						cursor.moveToNext();
						shortest.totalPath ="Line " + cursor.getString(0) +" : "+cursor.getString(1) +"\n" + shortest.totalPath;
						shortest.briefPath = cursor.getString(0) + temp +" : "+cursor.getString(1) +"\n\n" + shortest.briefPath;
						stopNumOfLine--;			
						break;
					}
				}
			}
		}
		shortest.briefPath ="Line " + cursor.getString(0) +" : "+cursor.getString(1) +"\n\n" + shortest.briefPath ;
		shortest.pathAry = pathAry;
		shortest.lineAry = lineAry;
		shortest.time = shortest.pathCount + "station, "+shortest.transferCount + "fransfer";
/*		
		db = SQLiteDatabase.openDatabase("/sdcard/Download/myway.sqlite", null,
				SQLiteDatabase.OPEN_READWRITE

				+ SQLiteDatabase.CREATE_IF_NECESSARY);
		
		for(int i=0; i<10536; i++) {
			Log.i("Count", i + "");
			String temp;
			temp = "insert into myway values ('" + myWay[i][0] + "','";
			for(int j=1; j<654; j++)
				temp +=  myWay[i][j]  + "','";
			temp +=  myWay[i][654]  + "');";
			db.execSQL(temp);
		}
*/		
	}

	public void compare(int stop, int line) {
/*
		for (int i = 0; i < 10536; i++) {
			if (bus[i][line] != 9999 )
				if (myWay[i][line] > myWay[stop][line])
					if(bus[i][line] > bus[stop][line])
						myWay[i][line] = bus[i][line] - bus[stop][line] + myWay[stop][line];
		}
*/
	
		for(int i = bus[stop][line]+1; i<179;i++) {
			if(busline[line][i] == -1)
				break;
			if(myWay[busline[line][i]][line] > myWay[stop][line]) {
//				myWay[busline[line][i]][line] = bus[busline[line][i]][line] - bus[stop][line] + myWay[stop][line];
				if(myWay[busline[line][i]][line] > bus[busline[line][i]][line] - bus[stop][line] + myWay[stop][line]) {
					if(busline[line][i]==24 && line==353)
						Log.i("!!!353-24 바뀌기 전",""+ myWay[busline[line][i]][line]);
					myWay[busline[line][i]][line] = bus[busline[line][i]][line] - bus[stop][line] + myWay[stop][line];
					if(busline[line][i]==24 && line==353)
						Log.i("!!!353-24 바뀐 후",""+ myWay[busline[line][i]][line]);
				}
			}
		}
			
	}
	
}
