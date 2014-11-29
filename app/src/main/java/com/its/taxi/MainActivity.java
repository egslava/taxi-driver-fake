package com.its.taxi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class MainActivity extends Activity implements ClockUpdateListener, BatteryUpdateListener {

    private ImageView taxoCtrl2;
    private ImageView taxoCtrl1;
    private TextView taxoTotalSum;
    private TextView dateTime;
    private TextView batteryState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);

        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("yellow");

        spec.setContent(R.id.ll01);
        spec.setIndicator(null, getResources().getDrawable(R.drawable.m_menu_yellow));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("taxo");
        spec.setContent(R.id.main_panel_taxo);
        spec.setIndicator(null, getResources().getDrawable(R.drawable.m_menu_taxometer));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("messages");
        spec.setContent(R.id.main_panel_msg);
        spec.setIndicator(null, getResources().getDrawable(R.drawable.m_menu_msg));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        taxoCtrl1 = (ImageView) findViewById(R.id.img_taxo_ctrl_1);
        taxoCtrl2 = (ImageView) findViewById(R.id.img_taxo_ctrl_2);
        taxoTotalSum = (TextView) findViewById(R.id.text_taxo_total_sum);
        dateTime = ((TextView) findViewById(R.id.text_date_time));
        batteryState = (TextView) findViewById(R.id.text_battery_state);

        taxoCtrl1.setImageResource(R.drawable.t_menu_load);
        taxoCtrl2.setVisibility(View.INVISIBLE);
        taxoTotalSum.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Medium.ttf"));

        findViewById(R.id.panelTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder activationDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                activationDialogBuilder.setTitle(MainActivity.this.getString(R.string.ctx_menu_main_activation));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.text_row);
                adapter.add(getText(R.string.ctx_menu_main_activation).toString());
                activationDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, ActivationActivity.class));
                    }
                });
                activationDialogBuilder.create().show();

            }
        });

        ClockManager.getInstance(this).setClockUpdateListener(this).setBatteryUpdateListener(this);
        setNotification();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ClockManager.registerBroadcast();
    }

    @Override
    protected void onPause() {
        ClockManager.unregisterBroadcast();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        if (item.getItemId() == R.id.exit) {
            AlertDialog.Builder exitDialogBuilder = new AlertDialog.Builder(this);
            exitDialogBuilder.setMessage(getResources().getString(R.string.main_activity_is_exit));
            exitDialogBuilder.setPositiveButton(getText(R.string.main_activity_dialog_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((NotificationManager) getApplicationContext()
                            .getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
                    finish();
                }
            });
            exitDialogBuilder.setNegativeButton(getText(R.string.main_activity_dialog_no), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            exitDialogBuilder.create().show();
        }

        if (item.getItemId() == R.id.statistics) {
            startActivity(new Intent(this, HistoryActivity.class));
        }

        if (item.getItemId() == R.id.about) {
            AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(this);
            aboutDialogBuilder.setMessage("Версия 2.0");
            aboutDialogBuilder.setNeutralButton(getText(R.string.main_activity_dialog_close), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            aboutDialogBuilder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm:ss dd-MM-yyyy");

    @Override
    public void onClockUpdate() {
        dateTime.setText(sdf.format(System.currentTimeMillis()));
    }

    @Override
    public void onBatteryUpdate(int chargeLevel) {
        batteryState.setText("A" + chargeLevel + "%");
        batteryState.setTextColor(getResources().getColor(chargeLevel > 20 ? R.color.green_color : R.color.red_color));
    }

    private void setNotification() {

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.icon_notify);
        builder.setContentTitle(getApplicationContext().getText(R.string.app_name));
        builder.setContentText(getText(R.string.main_activity_notif));
        builder.setContentIntent(resultPendingIntent);
        builder.setOngoing(true);

        Notification notification;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.getNotification();
        } else {
            notification = builder.build();
        }

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
