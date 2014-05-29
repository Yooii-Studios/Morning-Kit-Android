package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 5. 29.
 */
public class MNPhotoAlbumTransitionTypeDialogFragment extends DialogFragment {
    private static final String KEY_TYPE = "type";


    public static MNPhotoAlbumTransitionTypeDialogFragment newInstance(
            MNPhotoAlbumTransitionType type) {
        MNPhotoAlbumTransitionTypeDialogFragment fragment =
                new MNPhotoAlbumTransitionTypeDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_TYPE, type);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        MNPhotoAlbumTransitionType type =
                (MNPhotoAlbumTransitionType)args.getSerializable(KEY_TYPE);

        int count = MNPhotoAlbumTransitionType.values().length;
        CharSequence[] src = new CharSequence[count];

        for (int i = 0; i < count; i++) {
            MNPhotoAlbumTransitionType tempType =
                    MNPhotoAlbumTransitionType.values()[i];
            src[i] = tempType.getName(getActivity());
        }

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(getActivity(),
                    AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        return builder.setSingleChoiceItems(src, type.ordinal(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Fragment parentFragment = getTargetFragment();
                        if (parentFragment != null &&
                                parentFragment
                                        instanceof OnSelectTypeListener) {
                            ((OnSelectTypeListener) parentFragment)
                                    .onSelectType(
                                            MNPhotoAlbumTransitionType
                                                    .values()[i]
                                    );
                        }
                        dismiss();
                    }
                }).create();
    }

    public interface OnSelectTypeListener {
        public void onSelectType(MNPhotoAlbumTransitionType type);
    }
}
