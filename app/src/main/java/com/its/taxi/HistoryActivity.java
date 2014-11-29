package com.its.taxi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class HistoryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_taxometer_statistics);

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);

        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("history");

        spec.setContent(R.id.tab_orders);
        spec.setIndicator(null, getResources().getDrawable(R.drawable.t_stat_list));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("statistics");
        spec.setContent(R.id.tab_summary);
        spec.setIndicator(null, getResources().getDrawable(R.drawable.t_stat_summary));
        tabs.addTab(spec);


        tabs.setCurrentTab(0);
    }
}
