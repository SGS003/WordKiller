package com.example.wordkiller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.service.TestService1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity {
	Boolean mode = false; // 0 represent for free mode and 1 for task mode
	private DBHelper dbHelper = null;

	ListView listview = null;
	List<Map<String, Object>> mList;
	List<WordList> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		try {
			data = getData();// return a list of word lists
		} catch (IOException e) {
			e.printStackTrace();
		}
		listview = (ListView) findViewById(R.id.list);
		mList = new ArrayList<Map<String, Object>>();
		Iterator<WordList> i = data.iterator();
		// add data
		while (i.hasNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			WordList c = i.next();
			map.put("id", "List "+c.id);
			map.put("mastered ", c.mastered);
			mList.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, mList,
				R.layout.listview, new String[] { "id", "mastered" },
				new int[] { R.id.textView1, R.id.textView2 });
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ListActivity.this,
						TestActivity.class);
				intent.putExtra("index", position+1);
				intent.putExtra("mode", mode);
				startActivity(intent);
			}
		});

		

	}

	private List<WordList> getData() throws IOException {
		
		// TODO Auto-generated method stub
		DBHelper dbhelper = new DBHelper(this);
		dbhelper.createDataBase();
		dbhelper.openDataBase();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);
		Cursor cursor = db.rawQuery("SELECT * FROM list", null);
		WordList c;
		// return data if exists
		if (cursor.getCount() == 0) {
			return null;
		} else {
			List<WordList> WordList = new ArrayList<WordList>(cursor.getCount());
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				int mastered = cursor.getInt(cursor.getColumnIndex("mastered"));
				c = new WordList(id, mastered);
				WordList.add(c);
			}
			cursor.close();
			return WordList;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;

	}

	// portrait & landscape
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.back:
			Intent intent = new Intent(ListActivity.this, MainActivity.class);
			startActivity(intent);			
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
