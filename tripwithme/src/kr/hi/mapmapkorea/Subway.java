package kr.hi.mapmapkorea;

import java.util.Stack;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

class Shortest {
	public String time;
	public String path;
}

class Subway {
	private int length[][] = new int[303][303]; // ����ö �뼱�� ���� ���
	private int dist[] = new int[303]; // ��������� �� �������� ����� �����ϴ� �迭
	private boolean s[] = new boolean[303]; // ShortPath�Լ����� ����ϴ� bool�� �迭
	private boolean store[][] = new boolean[303][303]; // ��������� �����ϴ�
														// ����Ʈ���� ��Ÿ���� �迭
	private SQLiteDatabase db;
	private String geonameDatabaseFile = "/sdcard/Download/mapmapkorea.sqlite";

	public Subway() // Subway ����(������� �ʱ�ȭ)
	{
		for (int i = 0; i < 303; i++)
			// ������� 9999�� �ʱ�ȭ
			for (int j = 0; j < 303; j++)
				length[i][j] = 9999;

		for (int i = 0; i < 32; i++)
			// 1 ���� �ʱ�ȭ
			length[i][i + 1] = 2;

		for (int i = 33; i < 47; i++)
			// 2 ���� �ʱ�ȭ
			length[i][i + 1] = 2;
		length[43][48] = 2;
		for (int i = 48; i < 74; i++)
			// 2 ���� �ʱ�ȭ
			length[i][i + 1] = 2;
		length[70][75] = 2;
		for (int i = 75; i < 83; i++)
			length[i][i + 1] = 2;
		length[33][83] = 2;

		for (int i = 84; i < 115; i++)
			// 3 ���� �ʱ�ȭ
			length[i][i + 1] = 2;

		for (int i = 116; i < 142; i++)
			// 4 ���� �ʱ�ȭ
			length[i][i + 1] = 2;

		for (int i = 143; i < 185; i++)
			// 5 ���� �ʱ�ȭ
			length[i][i + 1] = 2;
		length[180][186] = 2;
		for (int i = 186; i < 192; i++)
			length[i][i + 1] = 2;

		for (int i = 193; i < 197; i++)
			// 6 ���� �ʱ�ȭ
			length[i][i + 1] = 2;
		length[193][198] = 2;
		length[193][199] = 2;
		for (int i = 199; i < 230; i++)
			length[i][i + 1] = 2;

		for (int i = 231; i < 267; i++)
			// 7 ���� �ʱ�ȭ
			length[i][i + 1] = 2;

		for (int i = 268; i < 278; i++)
			// 8 ���� �ʱ�ȭ
			length[i][i + 1] = 2;

		for (int i = 279; i < 302; i++)
			// 9 ���� �ʱ�ȭ
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
			// ����� ������ ���� �ʱ�ȭ
			for (int j = 0; j <= i; j++) {
				if (i == j)
					length[i][i] = 0;
				length[i][j] = length[j][i];
			}
	}

	private int Choose() // ShortPath �����Լ�
	{
		double min = 9999;
		int minpos = -1;

		for (int i = 0; i < 303; ++i) // s[i]�� true�̰� dist[i]�� �ּҰ��Ǵ� i��
										// ã�´�
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

	private void ShortestPath(int v) // �ִܳ뼱 ã�� �Լ�
	{
		for (int i = 0; i < 303; i++)
			// store�迭 false�� �ʱ�ȭ
			for (int j = 0; j < 303; j++)
				store[j][i] = false;

		for (int i = 0; i < 303; i++) // dist�迭�� s�迭 �ʱ�ȭ
		{
			dist[i] = length[v][i];
			if (length[v][i] < 9999 && length[v][i] > 0)
				store[v][i] = true;
			s[i] = false;
		}
		dist[v] = 0; // �ڱ��ڽ������� �Ÿ��� 0
		s[v] = true; //

		for (int i = 0; i < 301; i++) // ������������� �� ����������� �ִܽð���
										// ����
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

	public void Path(int x, int y, Shortest shortest) // x���� y�� ���� �ִܳ뼱��
														// ������ִ� �Լ�
	{
		ShortestPath(x); // ��߸� x�� ���� ShortestPath ����

		int w = y;
		int u, v;

		db = SQLiteDatabase.openDatabase(geonameDatabaseFile, null,
				SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.CREATE_IF_NECESSARY);
		Cursor cursor;

		Stack<Integer> s = new Stack<Integer>();
		s.push(y);

		for (int j = 0; j < 303; j++) // x���� y�� ���� ��θ� stack�� ����
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

		shortest.path = "�ִ� ��� : ";

		int count = 0;
		while (!s.empty()) {
			u = s.pop();
			if (s.empty()) // ���������̰� ȯ�¿��� �ƴ� ���
			{
				cursor = db.rawQuery("select * FROM seoulStation Where num = "
						+ u, null);
				cursor.moveToNext();
				shortest.path += " " + cursor.getString(2);
				// shortest.path += " " + cursor.getCount() ;
				// shortest.path += " " + (u+1) ;
				break;
			}
			v = s.pop();

			if (length[u][v] == 5) // ȯ�¿��ΰ��
			{
				cursor = db.rawQuery("select * FROM seoulStation Where num = "
						+ u, null);
				cursor.moveToNext();
				shortest.path += " " + cursor.getString(2) + "[["
						+ cursor.getInt(3) + "]]";
				// shortest.path += " " + (u+1) ;

				shortest.path += " (ȯ��)";
				cursor = db.rawQuery("select * FROM seoulStation Where num = "
						+ v, null);
				cursor.moveToNext();
				shortest.path += " " + cursor.getString(2) + "[["
						+ cursor.getInt(3) + "]]";
				// shortest.path += " " + (v+1);

				if (s.empty())
					break; // ���������̰� ȯ�¿��� ���
				shortest.path += " ->";
			} else // ȯ�¿��� �ƴ� ���
			{

				s.push(v);
				if (count == 1) {
					cursor = db
							.rawQuery("select * FROM seoulStation Where num = "
									+ u, null);
					cursor.moveToNext();
					shortest.path += " " + cursor.getString(2) + "[["
							+ cursor.getInt(3) + "]]" + " -> ";
					// shortest.path += " " + (u+1) + " -> ";
				}
				count++;

			}
		}

		// String S = new String();
		// distText.setText(dist[y]+" ");
		// distText.setText("??????????");
		shortest.time = "�ִܽð� = " + dist[y] + "��";
	}

	private TextView findViewById(int top) {
		// TODO Auto-generated method stub
		return null;
	}
}
