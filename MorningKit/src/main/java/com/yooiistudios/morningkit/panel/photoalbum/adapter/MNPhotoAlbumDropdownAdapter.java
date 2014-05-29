package com.yooiistudios.morningkit.panel.photoalbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;

import java.util.ArrayList;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 5. 29.
 */
public class MNPhotoAlbumDropdownAdapter extends ArrayAdapter<String> {
    private static final int LAYOUT_ID = R.layout
            .panel_photo_album_transition_type_dropdown;

    public MNPhotoAlbumDropdownAdapter(Context context, ArrayList<String> src) {
        super(context, android.R.layout.simple_spinner_item, src);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT_ID, null, false);
        }

        TextView textView = (TextView)convertView.findViewById(
                android.R.id.text1);
        textView.setText(getItem(position));

        return convertView;
    }
}
