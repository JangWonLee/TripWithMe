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

		for (int i = 0; i < 32146; i++) {
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
		
		for(int c=0; c<5; c++){

			Log.i("while", "while Start");
			int count = 0;
			for (int i = 0; i < 10536; i++) {
				int min = 9999;
				for (int j = 0; j < 655; j++) {
					if (myWay[i][j] < min) {
						min = myWay[i][j];
						if(i == 1698)
							Log.i("최소값1698"+j, myWay[i][j] + "");
					}
				}
				for (int j = 0; j < 655; j++) {
					if (bus[i][j] != 9999 && myWay[i][j] - min > 99) {
						if(i == 1698)
							Log.i("전1698-"+j, myWay[i][j] + "");
						myWay[i][j] = min + 10;   								// 환승 거리!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
						if(i == 1698)
							Log.i("후1698-"+j, myWay[i][j] + "");
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
		for(int i=178; i>0; i--) {
			if(busline[minLine][i] == endStopNum) {
				stopNumOfLine = i;
				break;
			}
		}
		shortest.time = minTime2 + "정거장";
		pathAry[0] = stopNum;
		shortest.pathCount = 1;
		shortest.totalPath = minLine + "-" + stopNum + "  ";
		stopNumOfLine--;
		for(int i = minTime2-1; i > -1; i--) {
			if(stopNumOfLine == -1)//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
				Log.i("minLine stop",""+ minLine+"  "+stopNumOfLine);
			else if(busline[minLine][stopNumOfLine] == -1)//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
				Log.i("stopNumOfLine",""+ stopNumOfLine);
			else if(myWay[busline[minLine][stopNumOfLine]][minLine] == i) {
				Log.i("minLine",""+ i);
				pathAry[shortest.pathCount] = busline[minLine][stopNumOfLine];
				shortest.pathCount++;
				shortest.totalPath += minLine + "-" + busline[minLine][stopNumOfLine] + "  ";
				stopNumOfLine--;
			}
			else {
				i -= 9;																	// 환승 거리!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
				Log.i("minLinekkkk",""+ i);
				for(int j=0; j<655; j++) {
					Log.i(j+"  "+i,""+ myWay[busline[minLine][stopNumOfLine+1]][j]);
					if(myWay[busline[minLine][stopNumOfLine+1]][j] == i) {
						for(int k=178; k>0; k--) {
							Log.i("minLinekkkk","" + busline[j][k] +"  "+ busline[minLine][stopNumOfLine+1]);
							if(busline[j][k] == busline[minLine][stopNumOfLine+1]) {
								stopNumOfLine = k;
								break;
							}
						}
						minLine = j;
						shortest.totalPath += "(환승)" + minLine + "-" + busline[minLine][stopNumOfLine] + "  ";
						stopNumOfLine--;			
						break;
					}
				}
			}
		}
		shortest.pathAry = pathAry;
		shortest.briefPath = shortest.totalPath;
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
					myWay[busline[line][i]][line] = bus[busline[line][i]][line] - bus[stop][line] + myWay[stop][line];
					if(busline[line][i]==1698)
						Log.i("1698!!!!!"+line,""+ (bus[busline[line][i]][line] - bus[stop][line] + myWay[stop][line]));
				}
			}
		}
			
	}
	
}