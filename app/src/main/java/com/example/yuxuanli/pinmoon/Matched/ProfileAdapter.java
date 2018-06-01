package com.example.yuxuanli.pinmoon.Matched;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuxuanli.pinmoon.R;
import com.example.yuxuanli.pinmoon.Utils.User;

import java.util.List;

/**
 * Created by yuxuanli on 5/20/18.
 */
public class ProfileAdapter extends ArrayAdapter<User>{
    private int resourceId;

    public ProfileAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);

        //improve the efficiency
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.personPic = (ImageView) view.findViewById(R.id.person_image);
            viewHolder.personName = (TextView) view.findViewById(R.id.person_name);
            viewHolder.imageButton = (ImageButton) view.findViewById(R.id.videoCalBtn);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        //viewHolder.personPic.setImageResource(user.getProfile_photo());
        viewHolder.personName.setText(user.getUsername());
        viewHolder.imageButton.setFocusable(false);

        return view;
    }


    class ViewHolder{
        ImageView personPic;
        TextView personName;
        ImageButton imageButton;
    }
}
