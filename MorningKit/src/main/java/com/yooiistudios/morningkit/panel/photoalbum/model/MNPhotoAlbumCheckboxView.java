package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.yooiistudios.morningkit.R;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 5. 27.
 */
public class MNPhotoAlbumCheckboxView extends ImageButton {
    private OnCheckListener mOnCheckListener;
    private boolean mChecked;

    public MNPhotoAlbumCheckboxView(Context context) {
        super(context);

        init();
    }

    public MNPhotoAlbumCheckboxView(Context context, AttributeSet attr) {
        super(context, attr);

        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mChecked = !mChecked;

                setChecked(mChecked);
            }
        });
    }

    public void setOnCheckListener(OnCheckListener l) {
        mOnCheckListener = l;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;

        // update ui
        if (checked) {
            setImageResource(R.drawable.icon_panel_detail_checkbox_on);
        }
        else {
            setImageResource(R.drawable.icon_panel_detail_checkbox);
        }

        // notify
        if (mOnCheckListener != null) {
            mOnCheckListener.onCheck(this, checked);
        }
    }


    public interface OnCheckListener {
        public void onCheck(ImageButton btn, boolean checked);
    }
}
