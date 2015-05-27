package com.yooiistudios.morningkit.panel.newsfeed.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrlType;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUtil;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 5.
 *
 * MNNewsFeedSelectDialogFragment
 *  커스텀 RSS 를 입력하는 다이얼로그
 */
public class MNNewsFeedSelectDialogFragment extends DialogFragment {
    private static final String KEY_URL = "url";

    private AutoCompleteTextView mFeedUrlEditText;
    private MNNewsFeedUrl mFeedUrl;

    public static MNNewsFeedSelectDialogFragment newInstance() {
        return new MNNewsFeedSelectDialogFragment();
    }

    public static MNNewsFeedSelectDialogFragment newInstance(
            MNNewsFeedUrl feedUrl) {
        MNNewsFeedSelectDialogFragment fragment = newInstance();

        Bundle args = new Bundle();
        args.putSerializable(KEY_URL, new MNNewsFeedUrl(feedUrl));

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().containsKey(KEY_URL)) {
            mFeedUrl = (MNNewsFeedUrl) getArguments().getSerializable(KEY_URL);
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View root = inflater.inflate(R.layout
                .dialog_fragment_news_feed_select, null, false);
        mFeedUrlEditText = (AutoCompleteTextView)root.findViewById(R.id.urlEditText);
        mFeedUrlEditText.setText(mFeedUrl != null ? mFeedUrl.url : "");

        // config adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                MNNewsFeedUtil.getUrlHistory(getActivity()));
        mFeedUrlEditText.setAdapter(adapter);

        //test code
//        root.findViewById(R.id.reset).setOnClickListener(new View
//                .OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mFeedUrl = MNNewsFeedUtil.getDefaultFeedUrl(getActivity());
//                mFeedUrlEditText.setText(mFeedUrl.getUrl());
//                mHasReset = true;
//            }
//        });
        View clearButton = root.findViewById(R.id.clear);
        clearButton.setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                mFeedUrlEditText.setText("");
            }
        });
        clearButton.bringToFront();

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        final AlertDialog dialog = builder
                .setTitle(R.string.news_feed_url_dialog_title)
                .setView(root)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Activity parentActivity = getActivity();
                                if (parentActivity != null &&
                                        parentActivity instanceof OnClickListener) {
                                    String url = mFeedUrlEditText.getText().toString();

                                    // add "http://" if it's not entered.
                                    if (!url.toLowerCase().matches("^\\w+://.*")) {
                                        MNNewsFeedUtil.addUrlToHistory(getActivity(), url);
                                        url = "http://" + url;
                                    }
                                    MNNewsFeedUtil.addUrlToHistory(getActivity(), url);

                                    ((OnClickListener) parentActivity).onConfirm(
                                            new MNNewsFeedUrl(url, MNNewsFeedUrlType.CUSTOM)
                                    );
                                }
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Activity parentActivity = getActivity();
                                if (parentActivity != null &&
                                        parentActivity instanceof OnClickListener) {
                                    ((OnClickListener) parentActivity).onCancel();
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
        void onConfirm(MNNewsFeedUrl feedUrl);
        void onCancel();
    }
}
