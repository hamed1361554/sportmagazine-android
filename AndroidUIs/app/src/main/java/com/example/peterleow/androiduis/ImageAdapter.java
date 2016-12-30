package com.example.peterleow.androiduis;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context context;

    public ImageAdapter(Context c) {
        context = c;
    }

    public int getCount() {
        return imageIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView per image item
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(10, 10, 10, 10);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imageIds[position]);
        return imageView;
    }

    private Integer[] imageIds = {
            R.drawable.photo_1, R.drawable.photo_2,
            R.drawable.photo_3, R.drawable.photo_4,
            R.drawable.photo_5, R.drawable.photo_6,
            R.drawable.photo_7, R.drawable.photo_8,
            R.drawable.photo_1, R.drawable.photo_2,
            R.drawable.photo_3, R.drawable.photo_4,
            R.drawable.photo_5, R.drawable.photo_6,
            R.drawable.photo_7, R.drawable.photo_8,
            R.drawable.photo_1, R.drawable.photo_2,
            R.drawable.photo_3, R.drawable.photo_4,
            R.drawable.photo_5, R.drawable.photo_6,
            R.drawable.photo_7, R.drawable.photo_8,
            R.drawable.photo_1, R.drawable.photo_2,
            R.drawable.photo_3, R.drawable.photo_4,
            R.drawable.photo_5, R.drawable.photo_6,
            R.drawable.photo_7
    };
}