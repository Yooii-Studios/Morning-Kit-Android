package com.yooiistudios.morningkit.setting.info;

import android.app.PendingIntent;
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

import com.flurry.android.FlurryAgent;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.review.MNReviewApp;
import com.yooiistudios.morningkit.setting.info.credit.MNCreditActivity;
import com.yooiistudios.morningkit.setting.info.moreinfo.MNMoreInfoActivity;
import com.yooiistudios.morningkit.setting.store.MNStoreActivity;
import com.yooiistudios.morningkit.setting.store.MNStoreFragment;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;

import java.util.HashMap;
import java.util.Map;

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

                // 플러리
                Map<String, String> params = new HashMap<String, String>();
                params.put(MNFlurry.CALLED_FROM, "Setting - Info - Store");
                FlurryAgent.logEvent(MNFlurry.STORE, params);
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

            case RECOMMEND:
                String appName = getString(R.string.recommend_app_full_name);
                String title = getString(R.string.recommend_title) + " [" + appName + "]";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, title);

                String link;
                if (MNStoreFragment.IS_STORE_FOR_NAVER) {
                    // 1500436# 은 여행의신(productNo)
                    // 출시전이면 originalProductId, 후면 productNo
                    // 모닝은 37676
                    link = "http://nstore.naver.com/appstore/web/detail.nhn?originalProductId=37676";
                } else {
                    link = "http://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                }
                String message = title + "\n" + getString(R.string.recommend_description) + "\n" + link;
                intent.putExtra(Intent.EXTRA_TEXT, message);

                // createChooser Intent
                Intent createChooser = Intent.createChooser(intent, title);

                // PendingIntent 가 완벽한 해법
                // (가로 모드에서 설정으로 와서 친구 추천하기를 누를 때 계속 반복 호출되는 상황을 막기 위함)
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(getActivity(), 0, createChooser, 0);

                try {
                    pendingIntent.send();
                } catch(PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
