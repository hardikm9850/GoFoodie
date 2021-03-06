package com.app.gofoodie.adapter.listviewadapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.app.gofoodie.model.cuisine.Cuisine;

import java.util.ArrayList;

public class CheckedListViewAdapter extends ArrayAdapter<Cuisine> implements View.OnClickListener {

    private Activity mActivity = null;
    private ArrayList<Cuisine> mList = null;
    private CheckedTextView mCheckTextView = null;

    public CheckedListViewAdapter(@NonNull Activity activity, ArrayList<Cuisine> list) {
        super(activity, android.R.layout.simple_list_item_checked, list);
        this.mActivity = activity;
        this.mList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            this.mCheckTextView = (CheckedTextView) mActivity.getLayoutInflater().inflate(android.R.layout.simple_list_item_checked, null);
            this.mCheckTextView.setOnClickListener(this);
        } else {
            this.mCheckTextView = (CheckedTextView) convertView;
        }

        this.mCheckTextView.setTag(position);
        this.mCheckTextView.setText(mList.get(position).getCuisineName());
        this.mCheckTextView.setChecked(mList.get(position).isChecked);

        return this.mCheckTextView;
    }

    @Override
    public void onClick(View view) {

        CheckedTextView ctv = (CheckedTextView) view;
        ctv.setChecked(!ctv.isChecked());
        int position = (int) ctv.getTag();
        mList.get(position).isChecked = ctv.isChecked();
    }

}
