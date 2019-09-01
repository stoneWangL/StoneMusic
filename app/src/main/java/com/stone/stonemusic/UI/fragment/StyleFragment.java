package com.stone.stonemusic.UI.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.StyleAdapter;
import com.stone.stonemusic.base.BaseFragment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StyleFragment extends BaseFragment {
    private View thisView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private StyleAdapter styleAdapter;

    /**
     * 怀旧| 清新| 浪漫|
     * 性感| 伤感| 治愈|
     * 放松| 孤独| 感动|
     * 兴奋| 快乐| 安静|
     * 思念|
     */
    private TabLayout.Tab
            tabHuaiJiu, tabQingXin, tabLangMan,
            tabXingGan, tabShangGan, tabZhiYu,
            tabFangSong, tabGuDu, tabGanDong,
            tabXingFen, tabKuaiLe, tabAnJing,
            tabSiNian;
    //几个代表Fragment页面的常量
    public static final int
            PAGE_HuaiJiu = 0, PAGE_QingXin = 1, PAGE_LangMan = 2,
            PAGE_XingGan = 3, PAGE_ShangGan = 4, PAGE_ZhiYu = 5,
            PAGE_FangSong = 6, PAGE_GuDu = 7, PAGE_GanDong = 8,
            PAGE_XingFen = 9, PAGE_KuaiLe = 10, PAGE_AnJing = 11,
            PAGE_SiNian = 12
            ;
    @Nullable
    @Override
    public View initView() {
        thisView = View.inflate(getContext(), R.layout.fragment_style, null);
        return thisView;
    }

    @Override
    protected void initListener() {
        if (null != getActivity()) {
            tabLayout = thisView.findViewById(R.id.tabLayout);
            viewPager = thisView.findViewById(R.id.viewPager);
            /*使用适配器将ViewPager与Fragment绑定在一起*/
            styleAdapter = new StyleAdapter(getActivity().getSupportFragmentManager());
            viewPager.setAdapter(styleAdapter);

            //将TabLayout与ViewPager绑定在一起
            tabLayout.setupWithViewPager(viewPager);

            //指定Tab的位置
            tabHuaiJiu = tabLayout.getTabAt(PAGE_HuaiJiu); //怀旧
            tabQingXin = tabLayout.getTabAt(PAGE_QingXin); //清新
            tabLangMan = tabLayout.getTabAt(PAGE_LangMan); //浪漫

            tabXingGan = tabLayout.getTabAt(PAGE_XingGan); //性感
            tabShangGan = tabLayout.getTabAt(PAGE_ShangGan); //伤感
            tabZhiYu = tabLayout.getTabAt(PAGE_ZhiYu); //治愈

            tabFangSong = tabLayout.getTabAt(PAGE_FangSong); //放松
            tabGuDu = tabLayout.getTabAt(PAGE_GuDu); //孤独
            tabGanDong = tabLayout.getTabAt(PAGE_GanDong); //感动
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }
}
