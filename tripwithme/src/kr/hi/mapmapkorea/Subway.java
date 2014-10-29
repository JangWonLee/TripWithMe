package kr.hi.mapmapkorea;

import java.util.Stack;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.widget.TextView;

class Shortest {
	public String time;
	public String briefPath;
	public String totalPath;
}

class Subway {
	private int length[][] = new int[303][303]; // 지하철 노선도 인접 행렬
	private int dist[] = new int[303]; // 출발점에서 각 점까지의 비용을 저장하는 배열
	private boolean s[] = new boolean[303]; // SShortPath함수에서 사용하는 bool형 배열
	private boolean store[][] = new boolean[303][303]; // 출발점에서 시작하는 신장트리를 나타내는
	// 배열
	private SQLiteDatabase db;
	private String geonameDatabaseFile = "/sdcard/Download/mapmapkorea.sqlite";

	public Subway() // Subway ����(������� �ʱ�ȭ)
	{
		for (int i = 0; i < 303; i++)
			// 인접행렬 9999로 초기화
			for (int j = 0; j < 303; j++)
				length[i][j] = 9999;

		for (int i = 0; i < 32; i++)
			// 1 절반 초기화
			length[i][i + 1] = 2;

		for (int i = 33; i < 47; i++)
			// 2 절반 초기화

			length[i][i + 1] = 2;
		length[43][48] = 2;
		for (int i = 48; i < 74; i++)
			// 2 절반 초기화

			length[i][i + 1] = 2;
		length[70][75] = 2;
		for (int i = 75; i < 83; i++)
			length[i][i + 1] = 2;
		length[33][83] = 2;

		for (int i = 84; i < 115; i++)
			// 3 절반 초기화

			length[i][i + 1] = 2;

		for (int i = 116; i < 142; i++)
			// 4 절반 초기화
			length[i][i + 1] = 2;

		for (int i = 143; i < 185; i++)
			// 5 절반 초기화
			length[i][i + 1] = 2;
		length[180][186] = 2;
		for (int i = 186; i < 192; i++)
			length[i][i + 1] = 2;

		for (int i = 193; i < 197; i++)
			// 6 절반 초기화

			length[i][i + 1] = 2;
		length[193][198] = 2;
		length[193][199] = 2;
		for (int i = 199; i < 230; i++)
			length[i][i + 1] = 2;

		for (int i = 231; i < 267; i++)
			// 7 절반 초기화
			length[i][i + 1] = 2;

		for (int i = 268; i < 278; i++)
			// 8 절반 초기화
			length[i][i + 1] = 2;

		for (int i = 279; i < 302; i++)
			// 9 절반 초기화
			length[i][i + 1] = 2;
		length[0][231] = 5;
		length[3][119] = 5;
		length[7][227] = 5;
		length[13][47] = 5;
		length[14][219] = 5;
		length[15][128] = 5;
		length[17][93] = 5;
		length[17][166] = 5;
		length[19][33] = 5;
		length[20][133] = 5;
		length[23][294] = 5;
		length[25][157] = 5;
		length[27][70] = 5;
		length[35][94] = 5;
		length[36][167] = 5;
		length[37][129] = 5;
		length[37][168] = 5;
		length[38][218] = 5;
		length[48][248] = 5;
		length[52][272] = 5;
		length[59][103] = 5;
		length[62][140] = 5;
		length[69][265] = 5;
		length[74][150] = 5;
		length[76][155] = 5;
		length[77][290] = 5;
		length[78][205] = 5;
		length[83][163] = 5;
		length[85][197] = 5;
		length[86][195] = 5;
		length[93][166] = 5;
		length[95][130] = 5;
		length[97][216] = 5;
		length[102][255] = 5;
		length[102][300] = 5;
		length[113][275] = 5;
		length[115][189] = 5;
		length[118][234] = 5;
		length[129][168] = 5;
		length[135][211] = 5;
		length[138][297] = 5;
		length[139][257] = 5;
		length[145][279] = 5;
		length[158][292] = 5;
		length[161][209] = 5;
		length[169][217] = 5;
		length[176][246] = 5;
		length[179][269] = 5;
		length[228][238] = 5;

		/*
		 * 
		 * 
		 * /* length[1][2] = 1.5; length[2][19] = 1;
		 * 
		 * for(int i=20; i<29; i++) // b�뼱 ���� �ʱ�ȭ length[i][i+1] = 1;
		 * length[20][21] = 1.5; length[23][24] = 1.5;
		 * 
		 * for(int i=30; i<42; i++) // c�뼱 ���� �ʱ�ȭ length[i][i+1] = 1;
		 * 
		 * length[0][30] = 0.5; //ȯ�¿� ���� �ʱ�ȭ length[27][36] = 0.5;
		 * length[18][24] = 0.5; length[16][37] = 0.5; length[4][21] = 0.5;
		 * length[9][40] = 0.5;
		 */
		for (int i = 0; i < 303; i++)
			// 행렬의 나머지 절반 초기화
			for (int j = 0; j <= i; j++) {
				if (i == j)
					length[i][i] = 0;
				length[i][j] = length[j][i];
			}
	}

	private int Choose() // ShortPath 헬퍼함수
	{
		double min = 9999;
		int minpos = -1;

		for (int i = 0; i < 303; ++i) // s[i]가 true이고 dist[i]가 최소가되는 i를 찾는다
		{
			if (s[i] != false)
				continue;
			if (dist[i] >= min)
				continue;

			min = dist[i];
			minpos = i;
		}
		return minpos;
	}

	private void ShortestPath(int v) // 최단노선 찾는 함수
	{
		for (int i = 0; i < 303; i++)
			// store배열 false로 초기화
			for (int j = 0; j < 303; j++)
				store[j][i] = false;

		for (int i = 0; i < 303; i++) // dist배열과 s배열 초기화
		{
			dist[i] = length[v][i];
			if (length[v][i] < 9999 && length[v][i] > 0)
				store[v][i] = true;
			s[i] = false;
		}
		dist[v] = 0; // 자기자신으로의 거리는 0
		s[v] = true; //

		for (int i = 0; i < 301; i++) // 출발점에서부터 각 정거장까지의 최단시간을 결정
		{
			int u = Choose();
			s[u] = true;
			for (int w = 0; w < 303; w++)
				if (!s[w] && dist[u] + length[u][w] < dist[w]) {
					dist[w] = dist[u] + length[u][w];
					store[u][w] = true;
				}
		}
	}

	public void Path(int x, int y, Shortest shortest) // x에서 y로 가는 최단노선을 출력해주는
	// 함수
	{
		ShortestPath(x); // 출발를 x로 놓고 ShortestPath 실행

		int w = y;
		int u, v;

		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
				SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.CREATE_IF_NECESSARY);
		Cursor cursor;
		Cursor cursor2;

		Stack<Integer> s = new Stack<Integer>();
		s.push(y);

		for (int j = 0; j < 303; j++) // x에서 y로 가는 경로를 stack에 저장
		{
			for (int i = 0; i < 303; i++) {

				if (store[i][w] == true) {
					s.push(i);
					w = i;
				}
			}

			if (w == x)
				break;
		}

		shortest.briefPath = "";
		shortest.totalPath = "";

		int count = 0;
		int pathAry[] = new int[100];
		while (!s.empty()) {
			pathAry[count] = s.pop();
			count++;
		}

		for (int i = 0; i < count; i++) {
			if (i == count - 1) // 마지막역이고 환승역이 아닌 경우
			{
				cursor = db.rawQuery("select * FROM seoulStation Where num = "
						+ pathAry[i], null);
				cursor.moveToNext();
				shortest.briefPath += "Line " + cursor.getInt(3) + " : "
						+ cursor.getString(2);
				shortest.totalPath += "Line " + cursor.getInt(3) + " : "
						+ cursor.getString(2);
				;

				// shortest.path += " " + cursor.getCount() ;
				// shortest.path += " " + (u+1) ;
				break;
			}

			if (length[pathAry[i]][pathAry[i + 1]] == 5) // 환승역인경우
			{
				cursor = db.rawQuery("select * FROM seoulStation Where num = "
						+ pathAry[i], null);
				cursor.moveToNext();
				cursor2 = db.rawQuery("select * FROM seoulStation Where num = "
						+ pathAry[i + 1], null);
				cursor2.moveToNext();

				shortest.briefPath += cursor.getInt(3) + " -> "
						+ cursor2.getInt(3) + " : " + cursor.getString(2)
						+ "\n";
				shortest.totalPath += "Line " + cursor.getInt(3) + " : "
						+ cursor.getString(2) + "\n";

				// shortest.path += " " + (u+1) ;

				// shortest.path += " " + (v+1);

				if (i == 0)
					break; // 마지막역이고 환승역인 경우
				// shortest.briefPath += " ->";
			} else // 환승역이 아닌 경우
			{

				if (i == 0) {
					cursor = db.rawQuery(
							"select * FROM seoulStation Where num = "
									+ pathAry[i], null);
					cursor.moveToNext();
					shortest.briefPath += "Line " + cursor.getInt(3) + " : "
							+ cursor.getString(2)
							+ "\n            [ Direction : ";
					cursor = db.rawQuery(
							"select * FROM seoulStation Where num = "
									+ pathAry[i + 1], null);
					cursor.moveToNext();

					shortest.briefPath += cursor.getString(2) + " ]" + "\n";
					// shortest.path += " " + (u+1) + " -> ";
				}
				cursor = db.rawQuery("select * FROM seoulStation Where num = "
						+ pathAry[i], null);
				cursor.moveToNext();
				shortest.totalPath += "Line " + cursor.getInt(3) + " : "
						+ cursor.getString(2) + "\n";
			}
		}

		// String S = new String();
		// distText.setText(dist[y]+" ");
		// distText.setText("??????????");
		shortest.time = ""+dist[y];
	}

	private TextView findViewById(int top) {
		// TODO Auto-generated method stub
		return null;
	}
}