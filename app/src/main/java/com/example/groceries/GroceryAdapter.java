package com.example.groceries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

public class GroceryAdapter extends BaseAdapter {
    Context context;
    ArrayList<GroceryItem> list;
    LayoutInflater inflater;

    public GroceryAdapter(Context context, ArrayList<GroceryItem> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void updateList(ArrayList<GroceryItem> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grocery, parent, false);
        }

        GroceryItem item = list.get(position);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView textView = convertView.findViewById(R.id.textView);

        // Remove old listener to avoid loops
        checkBox.setOnCheckedChangeListener(null);

        checkBox.setChecked(item.isChecked);
        textView.setText(item.name);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.isChecked = isChecked;
            if (context instanceof MainActivity) {
                ((MainActivity) context).sortAndRefresh();
            }
        });

        return convertView;
    }
}