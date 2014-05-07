package com.yooiistudios.morningkit.setting.info;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.memory.ViewUnbindHelper;
import com.yooiistudios.morningkit.common.review.MNReviewApp;
import com.yooiistudios.morningkit.setting.info.credit.MNCreditActivity;
import com.yooiistudios.morningkit.setting.info.moreinfo.MNMoreInfoActivity;
import com.yooiistudios.morningkit.setting.store.MNStoreActivity;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 6.
 *
 * MNInfoFragment
 *  세팅 - 인포 프래그먼트
 */
public class MNInfoFragment extends Fragment implements MNInfoItemClickListener {
    private static final String TAG = "MNInfoFragment";
    private static final String LINK_APP_PREFIX = "fb://profile/";
    private static final String FB_YOOII_ID = "652380814790935";

    @InjectView(R.id.setting_info_listview) ListView listView;

    // 이전에 생성된 프래그먼트를 유지
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_info_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);
            listView.setAdapter(new MNInfoListAdapter(getActivity(), this));
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
        listView.setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(getActivity())));
        getView().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(getActivity())));
    }

    @Override
    public void onItemClick(int position) {
        MNInfoItemType type = MNInfoItemType.valueOf(position);
        switch (type) {
            case STORE: {
                Intent intent = new Intent(getActivity(), MNStoreActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_modal_up, R.anim.activity_hold);
                break;
            }

            case MORNING_KIT_INFO: {
                Intent i = new Intent(getActivity(), MNMoreInfoActivity.class);
                startActivity(i);
                break;
            }

            case RATE_MORNING_KIT:
                MNReviewApp.showReviewActivity(getActivity());
                /*
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    getActivity().startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Couldn't launch the market", Toast.LENGTH_SHORT).show();
                }
                */
//                String storeUrl ="https://play.google.com/store/apps/details?id=facebook";
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl)));
                break;

            case LIKE_US_ON_FACEBOOK:
                try {
                    PackageManager packageManager = getActivity().getPackageManager();
                    if (packageManager != null) {
                        packageManager.getPackageInfo("com.facebook.katana", 0);
                    }
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINK_APP_PREFIX + FB_YOOII_ID)));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/YooiiMooii")));
                }
                break;

            case CREDITS: {
                Intent i = new Intent(getActivity(), MNCreditActivity.class);
                startActivity(i);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MNLog.i(TAG, "onDestroy");
        ViewUnbindHelper.unbindReferences(listView);
    }
}
