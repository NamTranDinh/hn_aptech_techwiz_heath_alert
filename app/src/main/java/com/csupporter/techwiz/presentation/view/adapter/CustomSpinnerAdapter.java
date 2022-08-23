package com.csupporter.techwiz.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csupporter.techwiz.R;


public class CustomSpinnerAdapter extends BaseAdapter {
    private final Context context;
    private final int[] flags;
    private final String[] filterNames;

    public CustomSpinnerAdapter(Context applicationContext, int[] flags, String[] filterNames) {
        this.context = applicationContext;
        this.flags = flags;
        this.filterNames = filterNames;
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_spinner_items, null);
        ImageView icon = view.findViewById(R.id.imageView);
        TextView names = view.findViewById(R.id.textView);
        icon.setImageResource(flags[i]);
        names.setText(filterNames[i]);
        return view;
    }
}
