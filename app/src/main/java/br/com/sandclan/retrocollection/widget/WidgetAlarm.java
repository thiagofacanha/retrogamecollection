package br.com.sandclan.retrocollection.widget;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class WidgetAlarm extends BroadcastReceiver {

    public static final String ANDROID_APPWIDGET_ACTION_APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        updateWidgets(context);
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, WidgetAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), AlarmManager.INTERVAL_HOUR, pi);
    }

    private static void updateWidgets(Context context) {
        Intent dataUpdatedIntent = new Intent(ANDROID_APPWIDGET_ACTION_APPWIDGET_UPDATE)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }
}