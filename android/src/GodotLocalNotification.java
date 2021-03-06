package org.godotengine.godot;

import android.app.Activity;
import android.content.Context;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.app.Notification;
import android.content.Intent;
import android.util.Log;

import com.godot.game.R;

import android.media.RingtoneManager;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationChannel;
import android.os.Build;
import android.graphics.BitmapFactory;
import android.os.SystemClock;

public class GodotLocalNotification extends Godot.SingletonBase {
	private static final String TAG = "GodotLocalNotification";
	private static Activity activity;
	private static Context context;
	private static int instance_id;
	private static String CHANNEL_ID = "LOCAL";
	private static String CHANNEL_NAME = "Channel local notification";

	static public Godot.SingletonBase initialize(Activity p_activity) { 
		return new GodotLocalNotification(p_activity); 
	} 

	public GodotLocalNotification(Activity p_activity) {
		registerClass("GodotLocalNotification", new String[] {
			"init",

			"schedule_local_notification", "cancel_local_notification"
		});

		activity = p_activity;
		context = activity.getApplicationContext();
	}

	public void init(final int p_instance_id) {
		this.instance_id = p_instance_id;

		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(activity.getPackageName() + CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = (NotificationManager)context.getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	public void schedule_local_notification(String title, String content, int delay, int notification_id) {
		// delay is after how much time(in millis) from current time you want to schedule the notification
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, activity.getPackageName() + CHANNEL_ID)
			.setContentTitle(title)
			.setContentText(content)
			.setAutoCancel(true)
			.setSmallIcon(R.drawable.icon)
			.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
			.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

		Intent intent = new Intent(context, Godot.class);
		PendingIntent intentActivity = PendingIntent.getActivity(context, notification_id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(intentActivity);

		Notification notification = builder.build();

		Intent notificationIntent = new Intent(context, LocalNotificationReceiver.class);
		notificationIntent.putExtra(LocalNotificationReceiver.NOTIFICATION_ID, notification_id);
		notificationIntent.putExtra(LocalNotificationReceiver.NOTIFICATION, notification);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification_id, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		long futureInMillis = SystemClock.elapsedRealtime() + delay * 1000;
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
	}

	public void cancel_local_notification(int notification_id) {
		try {
			Intent intent = new Intent(context, LocalNotificationReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

			alarmManager.cancel(pendingIntent);
		} catch (Exception ex) {
			Log.w(TAG, "Cannot show local notification: " + ex.getMessage());
		}
	}
}
