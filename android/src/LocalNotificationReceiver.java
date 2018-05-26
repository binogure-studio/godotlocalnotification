package org.godotengine.godot;

import android.util.Log;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import android.util.Log;
import android.net.Uri;


public class LocalNotificationReceiver extends BroadcastReceiver {
	private static final String TAG = "LocalNotificationReceiver";

	public static String NOTIFICATION_ID = "notification_id";
	public static String NOTIFICATION = "notification";

	@Override
	public void onReceive(final Context context, Intent intent) {

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = intent.getParcelableExtra(NOTIFICATION);

		// '0' is the default value
		int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);

		notificationManager.notify(notificationId, notification);
	}
}
