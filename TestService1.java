package com.example.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.wordkiller.ListActivity;
import com.example.wordkiller.R;
import com.example.wordkiller.TestActivity;

public class TestService1 extends Service {
	private final String TAG = "TestService1";
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
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Time For Words!", System.currentTimeMillis());

		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				ListActivity.class), 0);
		notification.setLatestEventInfo(this, "Get started for words!",
				"Get started for words!", pi);
		int i = (int) (Math.random() * 10);
		nm.notify(i, notification);
		//nm.setAutoCancal();
		/*
		 * Notification notification = new Notification.Builder(mContext)
		 * .setAutoCancel(true) .setContentTitle("title")
		 * .setContentText("describe") .setContentIntent(pi)
		 * .setSmallIcon(R.drawable.ic_launcher)
		 * .setWhen(System.currentTimeMillis()) .build();
		 */
		return super.onStartCommand(intent, flags, startId);
	}

}