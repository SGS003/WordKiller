package com.example.wordkiller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.service.TaskListService;
import com.example.service.TestService1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListActivity extends Activity {
	Boolean mode = true;// 1 for task mode,and 0 for free mode
	final Intent intent = new Intent();// for lock screen service
	int currentDay = 0;
	String task = "";
	ListView listview = null;
	List mList;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * alarm manager to invoke notification from the service every 20
		 * seconds or 6 hours
		 */
		AlarmManager alarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		Intent alarmIntent = new Intent(TaskListActivity.this,
				TaskListService.class);
		PendingIntent piAlarm = PendingIntent.getService(TaskListActivity.this,
				0, alarmIntent, 0);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, 0, 40000, piAlarm);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		/* get the currentDay and task from database */
		try {
			getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listview = (ListView) findViewById(R.id.list);
		mList = new ArrayList();
		String[] id = task.split("/");
		for (int i = 0; i < id.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("list", "List " + id[i]);
			Log.v("task", id[i]);
			mList.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, mList,
				R.layout.task_list_view, new String[] { "list" },
				new int[] { R.id.taskList });
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TaskListActivity.this,
						TestActivity.class);
				intent.putExtra("index", position + 1);
				intent.putExtra("mode", mode);
				startActivity(intent);
			}
		});
		tv = (TextView) findViewById(R.id.text);
		if (currentDay == 13) {
			tv.setText("Final Day!");
		}
	}

	private void getData() throws IOException {
		// TODO Auto-generated method stub
		DBHelper dbhelper = new DBHelper(this);
		dbhelper.createDataBase();
		dbhelper.openDataBase();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);
		Cursor cursor0 = db.rawQuery("SELECT * FROM TaskList", null);

		while (cursor0.moveToNext()) {
			currentDay = cursor0.getInt(cursor0.getColumnIndex("currentDay"));
		}
		Log.v("currentTaskList Activty", String.valueOf(currentDay));
		SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);
		Cursor cursor = db.rawQuery("SELECT day" + currentDay
				+ " FROM TaskList", null);

		while (cursor.moveToNext()) {
			task = cursor.getString(cursor.getColumnIndex("day" + currentDay));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.back:
			Intent intent = new Intent(TaskListActivity.this,
					MainActivity.class);
			startActivity(intent);
			break;
		case R.id.Reset:
			try {
				reset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void reset() throws IOException {
		final AlertDialog.Builder normalDialog = new AlertDialog.Builder(
				TaskListActivity.this);
		normalDialog.setTitle("Confirmation");
		normalDialog.setMessage("Are you sure reset the memory?");
		normalDialog.setPositiveButton("yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						DBHelper dbhelper = new DBHelper(getBaseContext());
						try {
							dbhelper.createDataBase();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dbhelper.openDataBase();
						SQLiteDatabase db = SQLiteDatabase
								.openOrCreateDatabase(DBHelper.DB_PATH + "/"
										+ DBHelper.DB_NAME, null);
						db.execSQL("Update TaskList SET currentDay=1");
						Intent intent = new Intent(TaskListActivity.this,
								TaskListActivity.class);
						startActivity(intent);
						Toast.makeText(getBaseContext(), "Reset done",
								Toast.LENGTH_SHORT).show();
					}
				});
		normalDialog.setNegativeButton("no",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		normalDialog.show();

	}
}
