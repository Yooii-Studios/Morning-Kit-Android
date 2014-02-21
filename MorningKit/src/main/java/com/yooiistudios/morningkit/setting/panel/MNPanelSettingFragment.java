package com.yooiistudios.morningkit.setting.panel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.panel.MNPanel;
import com.yooiistudios.morningkit.panel.MNPanelType;
import com.yooiistudios.morningkit.panel.selectpager.MNPanelSelectPagerAdapter;
import com.yooiistudios.morningkit.panel.selectpager.MNPanelSelectPagerInterface;
import com.yooiistudios.morningkit.panel.selectpager.MNPanelSelectPagerLayout;
import com.yooiistudios.morningkit.panel.selectpager.fragment.MNPanelSelectPagerFirstFragment;
import com.yooiistudios.morningkit.panel.selectpager.fragment.MNPanelSelectPagerSecondFragment;
import com.yooiistudios.morningkit.setting.panel.animate.MNTwinkleAnimator;
import com.yooiistudios.morningkit.setting.panel.matrixitem.MNSettingPanelMatrixItem;
import com.yooiistudios.morningkit.setting.panel.matrixitem.MNSettingPanelMatrixItemBuilder;
import com.yooiistudios.morningkit.setting.panel.matrixitem.MNSettingPanelMatrixItemClickListener;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrix;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrixType;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 6.
 *
 * MNPanelSettingFragment
 *  세팅 - 위젯 프래그먼트
 */
public class MNPanelSettingFragment extends Fragment implements MNSettingPanelMatrixItemClickListener, MNPanelSelectPagerInterface {

    private static final String TAG = "MNPanelSettingFragment";

    // @InjectView(R.id.setting_panel_matrix_layout_line_1) LinearLayout panelMatrixItemLayoutLine1;
    @InjectView(R.id.setting_panel_matrix_layout_line_2) LinearLayout panelMatrixItemLayoutLine2;

    ArrayList<MNSettingPanelMatrixItem> panelMatrixItems;
    @InjectView(R.id.setting_panel_matrix_item_1) MNSettingPanelMatrixItem panelMatrixItem1;
    @InjectView(R.id.setting_panel_matrix_item_2) MNSettingPanelMatrixItem panelMatrixItem2;
    @InjectView(R.id.setting_panel_matrix_item_3) MNSettingPanelMatrixItem panelMatrixItem3;
    @InjectView(R.id.setting_panel_matrix_item_4) MNSettingPanelMatrixItem panelMatrixItem4;

    @InjectView(R.id.setting_panel_select_pager_layout) MNPanelSelectPagerLayout panelSelectPagerLayout;

    // for blur/clear animation
    boolean isPanelMatrixItemPressed = false;
    int pressedPanelMatrixItemIndex = -1;
    boolean isPanelSelectPagerItemPressed = false;
    int pressedSelectPagerItemIndex = -1;

    // for guide animation
    boolean isGuideAnimationOn = false;
    int viewIndexToBeAnimatied = -1; // 애니메이션을 할 변수
    int animatingViewIndex = -1; // 애니메이션 중에만 쓸 변수
    int animationRemainingCount = -1;

    // 이전에 생성된 프래그먼트를 유지
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_panel_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // add to list
            panelMatrixItems = new ArrayList<MNSettingPanelMatrixItem>();
            panelMatrixItems.add(panelMatrixItem1);
            panelMatrixItems.add(panelMatrixItem2);
            panelMatrixItems.add(panelMatrixItem3);
            panelMatrixItems.add(panelMatrixItem4);

            // pager
            panelSelectPagerLayout.loadPanelSelectPager(getChildFragmentManager(), this);
        }
        return rootView;
    }

    // 현재의 패널 세팅을 가져 와서 지역화된 패널 이름과 테마가 적용된 패널 이미지를 적용
    private void initPanelMatrixItems() {
        List<Integer> panelList = MNPanel.getPanelUniqueIdList(getActivity());

        MNSettingPanelMatrixItemBuilder.buildItem(panelMatrixItem1, MNPanelType.valueOfUniqueId(panelList.get(0)), getActivity(), 0, this);
        MNSettingPanelMatrixItemBuilder.buildItem(panelMatrixItem2, MNPanelType.valueOfUniqueId(panelList.get(1)), getActivity(), 1, this);
        MNSettingPanelMatrixItemBuilder.buildItem(panelMatrixItem3, MNPanelType.valueOfUniqueId(panelList.get(2)), getActivity(), 2, this);
        MNSettingPanelMatrixItemBuilder.buildItem(panelMatrixItem4, MNPanelType.valueOfUniqueId(panelList.get(3)), getActivity(), 3, this);
    }

    // 위젯 2*1 / 2*2 체크
    private void checkPanelMatrix() {
        if (MNPanelMatrix.getCurrentPanelMatrixType(getActivity()) == MNPanelMatrixType.PANEL_MATRIX_2X2) {
            panelMatrixItemLayoutLine2.setVisibility(View.VISIBLE);
        } else {
            panelMatrixItemLayoutLine2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(getActivity())));
        checkPanelMatrix();
        initPanelMatrixItems();

        // page select layout - 리팩토링
        panelSelectPagerLayout.applyTheme();
    }

    @Override
    public void onPause() {
        super.onPause();

        // 모든 애니메이션 취소
        if (isPanelMatrixItemPressed) {
            clearPanelMatrixItemsAnimation(pressedPanelMatrixItemIndex);
        }
        if (isPanelSelectPagerItemPressed) {
            clearPanelSelectPagerAnimation(pressedSelectPagerItemIndex);
        }
        cancelGuideAnimation();
    }

    @Override
    public void onPanelItemClick(int position) {
        // 셀렉트 페이저가 눌리지 않은 상태에서는 blur/clear animation 적용
        if (!isPanelSelectPagerItemPressed) {
            // 이미 panelMatrixItem이 눌려 있고 같은 index를 눌러 취소할 경우
            if (isPanelMatrixItemPressed && pressedPanelMatrixItemIndex == position) {
                // 1. pager twinkle 애니메이션이 동작 중이라면 전체 취소 - 미구현
                cancelGuideAnimation();

                // 애니메이션 취소
                clearPanelMatrixItemsAnimation(position);
            } else {
                // panelMatrixItem이 눌리지 않았거나 눌렸으나 다른 index를 누를 경우
                // pager twinkle 애니메이션 시작되지 않았다면 시작
                if (!isGuideAnimationOn) {
                    isGuideAnimationOn = true;
                    startPanelSelectPagerGuideAnimation();
                }

                // blur, clear animation
                animatePanelMatrixItems(position);
            }
        } else {
            // 셀렉트 페이저가 눌린 상태에서는
            // 1. pager twinkle 애니메이션이 동작 중이라면 전체 취소 - 미구현
            cancelGuideAnimation();

            // 2. 선택된 페이저의 패널 타입으로 panelMatrixItem의 패널 타입을 변경
            changePanel(position, pressedSelectPagerItemIndex);

            // 3. 페이저 애니메이션 취소
            clearPanelSelectPagerAnimation(pressedSelectPagerItemIndex);
        }
    }

    private void startPanelMatrixGuideAnimation() {
        viewIndexToBeAnimatied = 0;
        animationRemainingCount = 1;
        startRecursivePanelMatrixGuideAnimation();
    }

    private void startRecursivePanelMatrixGuideAnimation() {
        // 트리거 체크 - 음수로 플래그 설정하면 애니메이션 취소
        if (viewIndexToBeAnimatied >= 0) {
            animatingViewIndex = viewIndexToBeAnimatied;
            ValueAnimator twinkleAnimation = MNTwinkleAnimator.makeTwinkleAnimation(getActivity());
            twinkleAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    panelMatrixItems.get(animatingViewIndex).getShadowLayout().setSolidAreaColor((Integer) animator.getAnimatedValue());
                }
            });
            twinkleAnimation.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    animatingViewIndex = -1;
                    viewIndexToBeAnimatied++;

                    if (viewIndexToBeAnimatied < panelMatrixItems.size()) {
                        startRecursivePanelMatrixGuideAnimation();
                    } else {
                        if (animationRemainingCount > 0) {
                            viewIndexToBeAnimatied = 0;
                            animationRemainingCount--;
                            startRecursivePanelMatrixGuideAnimation();
                        } else {
                            // 제대로 모든 애니메이션이 종료
                            isGuideAnimationOn = false;
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            twinkleAnimation.start();
        } else {
            animatingViewIndex = 0;
            isGuideAnimationOn = false;
        }
    }

    private void startPanelSelectPagerGuideAnimation() {
        ViewPager panelSelectPager = panelSelectPagerLayout.getPanelSelectPager();
        MNPanelSelectPagerAdapter panelSelectPagerAdapter
                = (MNPanelSelectPagerAdapter) panelSelectPager.getAdapter();

        MNPanelSelectPagerFirstFragment firstFragment
                = (MNPanelSelectPagerFirstFragment) panelSelectPagerAdapter.getActiveFragment(panelSelectPager, 0);

        viewIndexToBeAnimatied = 0;
        animationRemainingCount = 1;
        startRecursivePanelSelectFirstPagerGuideAnimation(firstFragment);
    }

    private void startRecursivePanelSelectFirstPagerGuideAnimation(final MNPanelSelectPagerFirstFragment firstFragment) {
        if (viewIndexToBeAnimatied >= 0) {
            animatingViewIndex = viewIndexToBeAnimatied;
            ValueAnimator twinkleAnimation = MNTwinkleAnimator.makeTwinkleAnimation(getActivity());
            twinkleAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    RoundShadowRelativeLayout roundShadowRelativeLayout
                            = firstFragment.getRoundShadowRelativeLayouts().get(animatingViewIndex);
                    roundShadowRelativeLayout.setSolidAreaColor((Integer) animator.getAnimatedValue());
                }
            });
            twinkleAnimation.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    viewIndexToBeAnimatied++;

                    if (viewIndexToBeAnimatied < firstFragment.getRoundShadowRelativeLayouts().size()) {
                        startRecursivePanelSelectFirstPagerGuideAnimation(firstFragment);
                    } else {
                        if (animationRemainingCount > 0) {
                            viewIndexToBeAnimatied = 0;
                            animationRemainingCount--;
                            startRecursivePanelSelectFirstPagerGuideAnimation(firstFragment);
                        } else {
                            viewIndexToBeAnimatied = 0;
                            animationRemainingCount = 1;
                            ViewPager panelSelectPager = panelSelectPagerLayout.getPanelSelectPager();
                            MNPanelSelectPagerAdapter panelSelectPagerAdapter
                                    = (MNPanelSelectPagerAdapter) panelSelectPager.getAdapter();
                            MNPanelSelectPagerSecondFragment secondFragment
                                    = (MNPanelSelectPagerSecondFragment) panelSelectPagerAdapter.getActiveFragment(panelSelectPager, 1);

                            panelSelectPager.setCurrentItem(1, true);
                            startRecursivePanelSelectSecondPagerGuideAnimation(secondFragment);
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            twinkleAnimation.start();
        } else {
            animatingViewIndex = 0;
            isGuideAnimationOn = false;
        }
    }

    private void startRecursivePanelSelectSecondPagerGuideAnimation(final MNPanelSelectPagerSecondFragment secondFragment) {
        if (viewIndexToBeAnimatied >= 0) {
            animatingViewIndex = viewIndexToBeAnimatied;
            final RoundShadowRelativeLayout roundShadowRelativeLayout
                    = secondFragment.getRoundShadowRelativeLayouts().get(animatingViewIndex);

            // 언락 체크해 색을 따로 적용함
            ValueAnimator twinkleAnimation;
            if ((Integer) roundShadowRelativeLayout.getTag() == MNPanelType.MEMO.getIndex() ||
                    (Integer) roundShadowRelativeLayout.getTag() == MNPanelType.DATE_COUNTDOWN.getIndex()) {

                List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(getActivity());
                if ((ownedSkus.indexOf(SKIabProducts.SKU_MEMO) == -1 &&
                        (Integer) roundShadowRelativeLayout.getTag() == MNPanelType.MEMO.getIndex()) ||
                        (ownedSkus.indexOf(SKIabProducts.SKU_DATE_COUNTDOWN) == -1 &&
                                (Integer) roundShadowRelativeLayout.getTag() == MNPanelType.DATE_COUNTDOWN.getIndex())) {
                    twinkleAnimation = MNTwinkleAnimator.makeLockPanelTwinkleAnimation(getActivity());
                } else {
                    twinkleAnimation = MNTwinkleAnimator.makeTwinkleAnimation(getActivity());
                }
            } else {
                twinkleAnimation = MNTwinkleAnimator.makeTwinkleAnimation(getActivity());
            }

            // 색 변환 적용
            twinkleAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    roundShadowRelativeLayout.setSolidAreaColor((Integer) animator.getAnimatedValue());
                }
            });
            twinkleAnimation.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    animatingViewIndex = -1;
                    viewIndexToBeAnimatied++;

                    // 상점, 빈 두칸은 애니메이션을 넣지 않음
                    if (viewIndexToBeAnimatied < secondFragment.getRoundShadowRelativeLayouts().size() - 3) {
                        startRecursivePanelSelectSecondPagerGuideAnimation(secondFragment);
                    } else {
                        if (animationRemainingCount > 0) {
                            viewIndexToBeAnimatied = 0;
                            animationRemainingCount--;
                            startRecursivePanelSelectSecondPagerGuideAnimation(secondFragment);
                        } else {
                            // 제대로 애니메이션이 종료
                            isGuideAnimationOn = false;
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            twinkleAnimation.start();
        } else {
            animatingViewIndex = 0;
            isGuideAnimationOn = false;
        }
    }

    private void cancelGuideAnimation() {
        viewIndexToBeAnimatied = -100;
    }

    private void animatePanelMatrixItems(int position) {
        // 이미 blur가 들어간 Item은 다시 blur를 주지 않고, 이전에 선택되었던 Item만 blur 처리를 해줌
        for (MNSettingPanelMatrixItem panelMatrixItem : panelMatrixItems) {
            if ((Integer)panelMatrixItem.getTag() != position) {
                if (!isPanelMatrixItemPressed || (Integer)panelMatrixItem.getTag() == pressedPanelMatrixItemIndex) {
                    Animation blurAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.panel_blur);
                    if (blurAnimation != null) {
                        panelMatrixItem.startAnimation(blurAnimation);
                    }
                }
            } else {
                if (isPanelMatrixItemPressed) {
                    Animation clearAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.panel_clear);
                    if (clearAnimation != null) {
                        panelMatrixItem.startAnimation(clearAnimation);
                    }
                }
            }
        }
        // toggle
        isPanelMatrixItemPressed = true;
        pressedPanelMatrixItemIndex = position;
    }

    private void animatePanelSelectPager(int position) {
        ViewPager panelSelectPager = panelSelectPagerLayout.getPanelSelectPager();
        MNPanelSelectPagerAdapter panelSelectPagerAdapter
                = (MNPanelSelectPagerAdapter) panelSelectPager.getAdapter();

        MNPanelSelectPagerFirstFragment firstFragment
                = (MNPanelSelectPagerFirstFragment) panelSelectPagerAdapter.getActiveFragment(panelSelectPager, 0);
        MNPanelSelectPagerSecondFragment secondFragment
                = (MNPanelSelectPagerSecondFragment) panelSelectPagerAdapter.getActiveFragment(panelSelectPager, 1);

        // 방금 선택한 인덱스를 clear처리, 이전 인덱스는 blur 처리(처음이라면 -1이므로 상관없음)
        for (RoundShadowRelativeLayout roundShadowRelativeLayout : firstFragment.getRoundShadowRelativeLayouts()) {
            if ((Integer) roundShadowRelativeLayout.getTag() != position) {
                if (!isPanelSelectPagerItemPressed || (Integer) roundShadowRelativeLayout.getTag() == pressedSelectPagerItemIndex) {
                    Animation blurAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.panel_blur);
                    if (blurAnimation != null) {
                        roundShadowRelativeLayout.startAnimation(blurAnimation);
                    }
                }
            } else {
                if (isPanelSelectPagerItemPressed) {
                    Animation clearAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.panel_clear);
                    if (clearAnimation != null) {
                        roundShadowRelativeLayout.startAnimation(clearAnimation);
                    }
                }
            }
        }
        for (RoundShadowRelativeLayout roundShadowRelativeLayout : secondFragment.getRoundShadowRelativeLayouts()) {
            if ((Integer) roundShadowRelativeLayout.getTag() != position) {
                if (!isPanelSelectPagerItemPressed || (Integer) roundShadowRelativeLayout.getTag() == pressedSelectPagerItemIndex) {
                    Animation blurAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.panel_blur);
                    if (blurAnimation != null) {
                        roundShadowRelativeLayout.startAnimation(blurAnimation);
                    }
                }
            } else {
                if (isPanelSelectPagerItemPressed) {
                    Animation clearAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.panel_clear);
                    if (clearAnimation != null) {
                        roundShadowRelativeLayout.startAnimation(clearAnimation);
                    }
                }
            }
        }
        // toggle - 여전히 선택중
        isPanelSelectPagerItemPressed = true;
        pressedSelectPagerItemIndex = position;
    }

    private void clearPanelMatrixItemsAnimation(int position) {
        for (MNSettingPanelMatrixItem panelMatrixItem : panelMatrixItems) {
            if ((Integer)panelMatrixItem.getTag() != position) {
                Animation clearAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.panel_clear);
                if (clearAnimation != null) {
                    panelMatrixItem.startAnimation(clearAnimation);
                }
            }
        }
        isPanelMatrixItemPressed = false;
        pressedPanelMatrixItemIndex = -1;
    }

    private void clearPanelSelectPagerAnimation(int position) {
        ViewPager panelSelectPager = panelSelectPagerLayout.getPanelSelectPager();
        MNPanelSelectPagerAdapter panelSelectPagerAdapter
                = (MNPanelSelectPagerAdapter) panelSelectPagerLayout.getPanelSelectPager().getAdapter();

        MNPanelSelectPagerFirstFragment firstFragment
                = (MNPanelSelectPagerFirstFragment) panelSelectPagerAdapter.getActiveFragment(panelSelectPager, 0);
        MNPanelSelectPagerSecondFragment secondFragment
                = (MNPanelSelectPagerSecondFragment) panelSelectPagerAdapter.getActiveFragment(panelSelectPager, 1);

        for (RoundShadowRelativeLayout roundShadowRelativeLayout : firstFragment.getRoundShadowRelativeLayouts()) {
            if ((Integer) roundShadowRelativeLayout.getTag() != position) {
                Animation clearAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.panel_clear);
                if (clearAnimation != null) {
                    roundShadowRelativeLayout.startAnimation(clearAnimation);
                }
            }
        }
        for (RoundShadowRelativeLayout roundShadowRelativeLayout : secondFragment.getRoundShadowRelativeLayouts()) {
            if ((Integer) roundShadowRelativeLayout.getTag() != position) {
                Animation clearAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.panel_clear);
                if (clearAnimation != null) {
                    roundShadowRelativeLayout.startAnimation(clearAnimation);
                }
            }
        }

        // toggle
        isPanelSelectPagerItemPressed = false;
        pressedSelectPagerItemIndex = -1;
    }

    private void changePanel(int panelMatrixItemIndex, int panelSelectPagerIndex) {
        MNPanelType panelTypeToBeChanged = MNPanelType.valueOf(panelSelectPagerIndex);

        // UI 변경
        for (MNSettingPanelMatrixItem settingPanelMatrixItem : panelMatrixItems) {
            if ((Integer) settingPanelMatrixItem.getTag() == panelMatrixItemIndex) {
                // 새 패널로 변경
                MNSettingPanelMatrixItemBuilder.buildItem(settingPanelMatrixItem,
                        panelTypeToBeChanged,
                        getActivity(),
                        panelMatrixItemIndex,
                        this);

                // 강조 애니메이션
                Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.panel_matrix_item_scale_up_and_down);
                if (scaleAnimation != null) {
                    settingPanelMatrixItem.startAnimation(scaleAnimation);
                }
            }
        }

        // 변경된 위젯 타입 아카이빙
        MNPanel.changePanel(getActivity(), panelTypeToBeChanged.getUniqueId(), panelMatrixItemIndex);
    }

    @Override
    public void onPanelSelectPagerItemClick(int position) {

        if (!isPanelMatrixItemPressed) {
            // pager가 선택된 상황에서 같은 인덱스를 누르면 애니메이션 클리어 및 선택 취소
            if (isPanelSelectPagerItemPressed && pressedSelectPagerItemIndex == position) {
                // twinkle 애니메이션 전체 취소 - 미구현
                cancelGuideAnimation();

                // panelSelectPager 애니메이션 취소
                clearPanelSelectPagerAnimation(position);
            } else {
                // panelMatrixItem twinkle 애니메이션이 작동하고 있지 않다면 작동
                if (!isGuideAnimationOn) {
                    isGuideAnimationOn = true;
                    startPanelMatrixGuideAnimation();
                }

                // 그 외에는 전부 선택 애니메이션 작동
                animatePanelSelectPager(position);
            }
        } else {
            // 셀렉트 페이저가 눌린 상태에서는
            // 1. twinkle 애니메이션 전체 취소 - 미구현
            cancelGuideAnimation();

            // 2. 선택되었던 panelMatrixItem 위치에 방금 선택한 페이저아이템에 해당하는 패널 타입을 대입
            changePanel(pressedPanelMatrixItemIndex, position);

            // 3. panelMatrixItem 애니메이션 취소
            clearPanelMatrixItemsAnimation(pressedPanelMatrixItemIndex);
        }
    }

    @Override
    public void onPanelSelectPagerUnlockItemClick(int position) {
        // 모든 애니메이션은 취소
        if (isPanelMatrixItemPressed) {
            clearPanelMatrixItemsAnimation(pressedPanelMatrixItemIndex);
        }
        if (isPanelSelectPagerItemPressed) {
            clearPanelSelectPagerAnimation(pressedSelectPagerItemIndex);
        }

        // 언락 액티비티 오픈(자동)
    }

    @Override
    public void onPanelSelectPagerStoreItemClick(int position) {
        // 모든 애니메이션은 취소
        if (isPanelMatrixItemPressed) {
            clearPanelMatrixItemsAnimation(pressedPanelMatrixItemIndex);
        }
        if (isPanelSelectPagerItemPressed) {
            clearPanelSelectPagerAnimation(pressedSelectPagerItemIndex);
        }

        // 상점 액티비티 오픈(자동)
    }
}
