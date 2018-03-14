package com.example.wordkiller;

import java.io.IOException;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	final Intent intent = new Intent();// for lock screen service

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * alarm manager to invoke notification from the service every 20
		 * seconds or 6 hours
		 */
		AlarmManager alarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		Intent alarmIntent = new Intent(MainActivity.this, TestService1.class);
		PendingIntent piAlarm = PendingIntent.getService(MainActivity.this, 0,
				alarmIntent, 0);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, 0, 40000, piAlarm);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void free(View v) {
		Intent intent = new Intent(MainActivity.this, ListActivity.class);
		startActivity(intent);
	}

	public void task(View v) throws IOException {

		// create relative table
		DBHelper dbhelper = new DBHelper(this);
		dbhelper.createDataBase();
		dbhelper.openDataBase();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);
		db.execSQL("CREATE TABLE if not exists TaskList (id INTEGER PRIMARY KEY AUTOINCREMENT,currentDay Integer DEFAULT 0,day1 VARCHAR(50),day2 VARCHAR(50),day3 VARCHAR(50),day4 VARCHAR(50),day5 VARCHAR(50),day6 VARCHAR(60),day7 VARCHAR(50),day8 VARCHAR(50),day9 VARCHAR(50),day10 VARCHAR(50),day11 VARCHAR(50),day12 VARCHAR(50),day13 VARCHAR(50))");
		// initialize
		int currentDay = 0;
		Cursor cursor0 = db.rawQuery("SELECT * FROM TaskList", null);
		while (cursor0.moveToNext()) {
			currentDay = cursor0.getInt(cursor0.getColumnIndex("currentDay"));
		}
		if (currentDay == 0) {
			// if the task mode is just starting
			db.execSQL("INSERT INTO TaskList(currentDay,day1,day2,day3,day4,day5,day6,day7,day8,day9,day10,day11,day12,day13) VALUES (1,'1/2','1/2/3/4','3/4/5/6','1/2/5/6/7/8','3/4/7/8/9/10','5/6/9/10/11/12','1/2/7/8/11/12/13/14','3/4/13/14/9/10','5/6/11/12','7/8/13/14','9/10','11/12','13/14')");
			final AlertDialog.Builder normalDialog = new AlertDialog.Builder(
					MainActivity.this);
			normalDialog.setTitle("Confirmation");
			normalDialog.setMessage("Are you sure start task mode?");
			normalDialog.setPositiveButton("yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(MainActivity.this,
									TaskListActivity.class);
							startActivity(intent);
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
		}else{
			Intent intent = new Intent(MainActivity.this,
					TaskListActivity.class);
			startActivity(intent);
		}
		/*
		 * 
		 * day1 1/2 day2 1/2/3/4 day3 3/4/5/6 day4 1/2/5/6/7/8 day5 3/4/7/8/9/10
		 * day6 5/6/9/10/11/12 day7 1/2/7/8/11/12/13/14 day8 3/4/13/14/9/10 day9
		 * 5/6/11/12 day10 7/8/13/14 day11 9/10 day12 11/12 day13 13/14
		 */
		

	}
}
