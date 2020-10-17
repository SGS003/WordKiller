package com.example.service;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.example.wordkiller.DBHelper;

public class TaskListService extends Service {

	private final String TAG = "TaskListService";
	private static Context mContext;

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		return null;
	}

	public void onCreate() {
		mContext = this;
		Log.i(TAG, "onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		DBHelper dbhelper = new DBHelper(this);
		try {
			dbhelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbhelper.openDataBase();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);
		Cursor cursor0 = db.rawQuery("SELECT currentDay FROM TaskList", null);
		int currentDay = 0;
		while (cursor0.moveToNext()) {
			currentDay = cursor0.getInt(cursor0.getColumnIndex("currentDay"));
		}
		currentDay = currentDay + 1;
		if (currentDay < 14) {
			db.execSQL("UPDATE TaskList SET currentDay=" + currentDay);
		}

		return super.onStartCommand(intent, flags, startId);
	}

}
