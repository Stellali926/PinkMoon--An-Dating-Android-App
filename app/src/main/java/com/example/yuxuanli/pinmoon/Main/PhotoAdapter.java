package com.example.yuxuanli.pinmoon.Main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuxuanli.pinmoon.R;

import java.util.List;

/**
 * Created by yuxuanli on 5/28/18.
 */

public class PhotoAdapter extends ArrayAdapter<Cards> {
    Context context;


    public PhotoAdapter(@NonNull Context context, int resource, @NonNull List<Cards> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(card_item.getName());
        image.setImageResource(R.mipmap.ic_launcher); // temp image

        return convertView;
    }
}
