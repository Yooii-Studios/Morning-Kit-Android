package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yooiistudios.morningkit.R;

import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumDetailFragment.DEFAULT_INTERVAL_MIN;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumDetailFragment.DEFAULT_INTERVAL_SEC;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 5. 28.
 */
public class MNPhotoAlbumRefreshTimeDialogFragment extends DialogFragment {
    private static final String KEY_MINUTE = "min";
    private static final String KEY_SECOND = "sec";

    private EditText mMinuteEditText;
    private EditText mSecondEditText;

    public static MNPhotoAlbumRefreshTimeDialogFragment newInstance(
            int minute, int second) {
        MNPhotoAlbumRefreshTimeDialogFragment fragment =
                new MNPhotoAlbumRefreshTimeDialogFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_MINUTE, minute);
        args.putInt(KEY_SECOND, second);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
//        mMinute = args.getInt(KEY_MINUTE);
//        mSecond = args.getInt(KEY_SECOND);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View root = inflater.inflate(R.layout
                .dialog_fragment_photo_album_transition, null, false);
        mMinuteEditText = (EditText)root.findViewById(R.id.minuteEditText);
        mSecondEditText = (EditText)root.findViewById(R.id.secondEditText);

        mMinuteEditText.setText(String.valueOf(args.getInt(KEY_MINUTE)));
        mSecondEditText.setText(String.valueOf(args.getInt(KEY_SECOND)));


        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(getActivity(),
                    AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        AlertDialog dialog = builder
                .setTitle(R.string.photo_album_label_refresh_time)
                .setView(root)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Fragment parentFragment = getTargetFragment();
                                if (parentFragment != null &&
                                        parentFragment
                                                instanceof OnClickListener) {
                                    // minute
                                    String minuteStr = mMinuteEditText.getText()
                                            .toString();
                                    int minute;
                                    if (minuteStr.length() > 0) {
                                        minute = Integer.parseInt(minuteStr);
                                    } else {
                                        minute = DEFAULT_INTERVAL_MIN;
                                    }

                                    // second
                                    String secondStr = mSecondEditText.getText()
                                            .toString();
                                    int second;
                                    if (secondStr.length() > 0) {
                                        second = Integer.parseInt(secondStr);
                                    } else {
                                        second = DEFAULT_INTERVAL_SEC;
                                    }

                                    if (minute <= 0 && second <= 0) {
                                        ((OnClickListener)parentFragment)
                                                .onCancel();
                                    }
                                    else {
                                        ((OnClickListener) parentFragment)
                                                .onConfirm(minute, second);
                                    }
                                }
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Fragment parentFragment = getTargetFragment();
                                if (parentFragment != null &&
                                        parentFragment
                                                instanceof OnClickListener) {
                                    ((OnClickListener) parentFragment)
                                            .onCancel();
                                }
                            }
                        }
                )
                .setNeutralButton(R.string.setting_effect_sound_off,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Fragment parentFragment = getTargetFragment();
                                if (parentFragment != null &&
                                        parentFragment
                                                instanceof OnClickListener) {
                                    ((OnClickListener) parentFragment)
                                            .onTurnOff();
                                }
                            }
                        }
                ).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mSecondEditText.requestFocus();
                mSecondEditText.setSelection(mSecondEditText.length());
                InputMethodManager imm =
                        (InputMethodManager)getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mSecondEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        return dialog;
    }

    public interface OnClickListener {
        public void onConfirm(int minute, int second);
        public void onCancel();
        public void onTurnOff();
    }
}
