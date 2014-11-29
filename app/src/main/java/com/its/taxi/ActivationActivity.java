package com.its.taxi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivationActivity extends Activity {

    private String[] items = new String[]{"Уфа", "Самара", "Казань", "Пермь", "Челябинск", "Нижний Новгород", "Стерлитамак"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activation_main);
        findViewById(R.id.btn_activation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder cityListDialogBuilder = new AlertDialog.Builder(ActivationActivity.this);
                cityListDialogBuilder.setTitle(getText(R.string.screen_activation_progress_dialog_city_title));
                cityListDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView)findViewById(R.id.textCity)).setText(items[which]);
                    }
                });
                cityListDialogBuilder.create().show();
            }
        });
    }
}
