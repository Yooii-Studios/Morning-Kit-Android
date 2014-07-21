package com.yooiistudios.morningkit.panel.newsfeed.ui;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUtil;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 5.
 */
public class MNNewsFeedSelectDialogFragment extends DialogFragment {
    private static final String KEY_URL = "url";

    private AutoCompleteTextView mFeedUrlEditText;

    public static MNNewsFeedSelectDialogFragment newInstance(
            String feedUrl) {
        MNNewsFeedSelectDialogFragment fragment =
                new MNNewsFeedSelectDialogFragment();

        Bundle args = new Bundle();
        args.putString(KEY_URL, feedUrl);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View root = inflater.inflate(R.layout
                .dialog_fragment_news_feed_select, null, false);
        mFeedUrlEditText = (AutoCompleteTextView)root.findViewById(R.id.urlEditText);
        mFeedUrlEditText.setText(args.getString(KEY_URL));

        // config adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                MNNewsFeedUtil.getUrlHistory(getActivity()));
        mFeedUrlEditText.setAdapter(adapter);

        //test code
        root.findViewById(R.id.reset).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeedUrlEditText.setText(MNNewsFeedUtil.getDefaultFeedUrl(getActivity()));
            }
        });
        root.findViewById(R.id.clear).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                mFeedUrlEditText.setText("");
            }
        });

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(getActivity(),
                    AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        AlertDialog dialog = builder
                .setTitle(R.string.news_feed_url_dialog_title)
                .setView(root)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Fragment parentFragment = getTargetFragment();
                                if (parentFragment != null &&
                                        parentFragment
                                                instanceof OnClickListener) {
                                    String url = mFeedUrlEditText.getText()
                                            .toString();

                                    // add "http://" if it's not entered.
                                    if (!url.toLowerCase().matches("^\\w+://.*")) {
                                        url = "http://" + url;
                                    }

                                    MNNewsFeedUtil.addUrlToHistory(
                                            getActivity(), url);

                                    ((OnClickListener) parentFragment)
                                            .onConfirm(url);
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
                ).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mFeedUrlEditText.requestFocus();
                mFeedUrlEditText.setSelection(mFeedUrlEditText.length());
                InputMethodManager imm =
                        (InputMethodManager)getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mFeedUrlEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        return dialog;
    }

    public interface OnClickListener {
        public void onConfirm(String url);
        public void onCancel();
    }
}
