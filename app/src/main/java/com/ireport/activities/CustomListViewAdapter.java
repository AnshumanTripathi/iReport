package com.ireport.activities;

import com.ireport.R;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Somya on 12/6/2016.
 */
public class CustomListViewAdapter extends ArrayAdapter<ListActivityRowClass> {

    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<ListActivityRowClass> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
        TextView status;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ListActivityRowClass rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.streetAddress);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.description);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getStreetAddress());
        holder.txtTitle.setText(rowItem.getDescription());
        holder.imageView.setImageBitmap(rowItem.getImageId());
        holder.status.setText(rowItem.getStatus());

        return convertView;
    }
}
