package com.example.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.wordkiller.R;

public class WidgetProvider extends AppWidgetProvider {
	private static final String TAG = "WidgetProvider";
	private PendingIntent pendingIntent = null;
	private static final int UPDATE_DURATION = 10 * 1000; // update interval

	/* when a widget is deleted */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.i(TAG, "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	/* when all widgets are deleted */
	@Override
	public void onDisabled(Context context) {
		Log.i(TAG, "onDisabled");
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pendingIntent);

		super.onDisabled(context);
	}

	// when a widget loaded to the screen first time
	@Override
	public void onEnabled(Context context) {
		Log.i(TAG, "onEnabled");
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "onReceive");
		// get remote views of widget
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.appwidget);
		AppWidgetManager mWidgetManager = AppWidgetManager.getInstance(context);
		// get all widget id
		int[] appWidgetIds = mWidgetManager.getAppWidgetIds(new ComponentName(
				context, WidgetProvider.class));
		mWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

		super.onReceive(context, intent);
	}

	/*
	 * widget updates every time new widget is added or reaching the time
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		AlarmManager alarm = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(context, WidgetService.class);
		PendingIntent piAlarm = PendingIntent.getService(context, 0,
				alarmIntent, 0);
		alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime(), UPDATE_DURATION, piAlarm);

		Log.v("onUpdate", "alarm");

	}

}
