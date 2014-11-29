package com.its.taxi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

public class ClockManager {

    private static ClockManager mInstance;

    public static ClockManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ClockManager(context);
        }
        return mInstance;
    }

    private Context mContext;
    private ClockUpdateListener mClockUpdateListener;
    private BatteryUpdateListener mBatteryUpdateListener;
    private BroadcastReceiver mClockReceiver;
    private BroadcastReceiver mBatteryReceiver;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    private ClockManager(Context context) {
	    mContext = context;
    }

    private void registerAlarmBroadcast() {
        mClockReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                mClockUpdateListener.onClockUpdate();
            }
        };

        mBatteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                int percent = (level * 100) / scale;
                mBatteryUpdateListener.onBatteryUpdate(percent);
            }
        };

        mContext.registerReceiver(mClockReceiver, new IntentFilter("alarm"));
        mContext.registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        pendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("alarm"), 0);
        alarmManager = (AlarmManager) (mContext.getSystemService(Context.ALARM_SERVICE));
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000, pendingIntent);
    }
    
    public static void registerBroadcast() {
        Log.w("receiver registered", "mytag");
        if (mInstance != null) {
            mInstance.registerAlarmBroadcast();
        }
    }
    
    private void unregisterAlarmBroadcast() {
	    alarmManager.cancel(pendingIntent);
        mContext.unregisterReceiver(mClockReceiver);
        mContext.unregisterReceiver(mBatteryReceiver);
    }

    public static void unregisterBroadcast() {
        if (mInstance != null) {
            mInstance.unregisterAlarmBroadcast();
        }
    }
    
    public static ClockManager setClockUpdateListener(ClockUpdateListener listener) {
        if (mInstance != null) {
            mInstance.mClockUpdateListener = listener;
        }
        return mInstance;
    }

    public static ClockManager setBatteryUpdateListener(BatteryUpdateListener listener) {
        if (mInstance != null) {
            mInstance.mBatteryUpdateListener = listener;
        }
        return mInstance;
    }
}
