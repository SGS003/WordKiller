package com.example.service;

import java.io.IOException;
import java.util.Random;

import com.example.wordkiller.DBHelper;
import com.example.wordkiller.ListActivity;
import com.example.wordkiller.R;
import com.example.wordkiller.Word;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetService extends Service {
	private final String TAG = "WidgetService";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		RemoteViews view = new RemoteViews(getPackageName(), R.layout.appwidget);
		Word w = null;
		try {
			w = getWord(getBaseContext());
			view.setTextViewText(R.id.word, w.getWord());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("widgetService","din");
		}		
		RemoteViews remoteViews = new RemoteViews(getApplicationContext()
				.getPackageName(), R.layout.appwidget);
		remoteViews.setTextViewText(R.id.word, w.getWord());
		remoteViews.setTextViewText(R.id.chinese, w.getChinese());
		remoteViews.setTextViewText(R.id.english, w.getEnglish());
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(getBaseContext());
		//update
		appWidgetManager.updateAppWidget(new ComponentName(getBaseContext(),
				WidgetProvider.class), remoteViews);
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	private Word getWord(Context context) throws IOException {
		// TODO Auto-generated method stub
		DBHelper dbhelper = new DBHelper(context);
		dbhelper.createDataBase();
		dbhelper.openDataBase();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);
		int index = new Random().nextInt(14)+1;
		int wordIndex = new Random().nextInt(100)+1;
		Cursor cursor = db.rawQuery("SELECT * FROM List"+index
				+ " WHERE id="+wordIndex, null);

		Word w = null;
		while (cursor.moveToNext()) {
			String word = cursor.getString(cursor.getColumnIndex("word"));
			String chinese = cursor.getString(cursor
					.getColumnIndex("chinese"));
			String english = cursor.getString(cursor
					.getColumnIndex("english"));
			w = new Word(word, chinese, english);
		}
		Log.e("widgetServiceGetWord", wordIndex+w.getWord() + w.getChinese() + w.getEnglish());
		cursor.close();
		return w;
	}
}
