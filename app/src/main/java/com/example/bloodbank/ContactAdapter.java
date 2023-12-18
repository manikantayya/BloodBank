package com.example.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bloodbank.Lvitems;
import com.example.bloodbank.R;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {
    private ArrayList<Lvitems> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public ContactAdapter(ArrayList<Lvitems> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.lvitems, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.txtname);
            viewHolder.phone = convertView.findViewById(R.id.txtPhone);
            viewHolder.blood = convertView.findViewById(R.id.txtblood);
            viewHolder.pa = convertView.findViewById(R.id.txtaddress);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Lvitems lvItem = arrayList.get(position);
        viewHolder.name.setText(lvItem.getName());
        viewHolder.phone.setText(lvItem.getPhone());
        viewHolder.pa.setText(lvItem.getPa());
        viewHolder.blood.setText(lvItem.getBlood());

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView phone;
        TextView blood;
        TextView pa;
    }
}
