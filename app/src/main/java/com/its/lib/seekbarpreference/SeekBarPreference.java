// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.its.lib.seekbarpreference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.its.taxi.R;

public final class SeekBarPreference extends DialogPreference
    implements android.widget.SeekBar.OnSeekBarChangeListener
{

    private final int a;
    private final int b;
    private final int c;
    private int d;
    private SeekBar e;
    private TextView f;

    public SeekBarPreference(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        c = attributeset.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "minValue", 5);
        b = attributeset.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "maxValue", 100);
        a = attributeset.getAttributeIntValue("http://schemas.android.com/apk/res/android", "defaultValue", 50);
    }

    public final CharSequence getSummary()
    {
        String s = super.getSummary().toString();
        int i = getPersistedInt(a);
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(i);
        return String.format(s, aobj);
    }

    protected final View onCreateDialogView()
    {
        d = getPersistedInt(a);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_slider, null);
        ((TextView)view.findViewById(R.id.min_value)).setText(Integer.toString(c));
        ((TextView)view.findViewById(R.id.max_value)).setText(Integer.toString(b));
        e = (SeekBar)view.findViewById(R.id.seek_bar);
        e.setMax(b - c);
        e.setProgress(d - c);
        e.setOnSeekBarChangeListener(this);
        f = (TextView)view.findViewById(R.id.current_value);
        f.setText(Integer.toString(d));
        return view;
    }

    protected final void onDialogClosed(boolean flag)
    {
        super.onDialogClosed(flag);
        if (!flag)
        {
            return;
        }
        if (shouldPersist())
        {
            persistInt(d);
        }
        notifyChanged();
    }

    public final void onProgressChanged(SeekBar seekbar, int i, boolean flag)
    {
        d = i + c;
        f.setText(Integer.toString(d));
    }

    public final void onStartTrackingTouch(SeekBar seekbar)
    {
    }

    public final void onStopTrackingTouch(SeekBar seekbar)
    {
    }
}
