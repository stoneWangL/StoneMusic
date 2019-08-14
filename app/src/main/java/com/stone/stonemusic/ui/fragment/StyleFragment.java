package com.stone.stonemusic.ui.fragment;


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

    //怀旧| 清新| 浪漫| 性感| 伤感| 治愈| 放松| 孤独| 感动| 兴奋| 快乐| 安静| 思念|
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
//        if (null != getContext() && null != getActivity()) {
            tabLayout = thisView.findViewById(R.id.tabLayout);
            viewPager = thisView.findViewById(R.id.viewPager);
            /*使用适配器将ViewPager与Fragment绑定在一起*/
            styleAdapter = new StyleAdapter(getActivity().getSupportFragmentManager());
            viewPager.setAdapter(styleAdapter);

            //将TabLayout与ViewPager绑定在一起
            tabLayout.setupWithViewPager(viewPager);

            //指定Tab的位置
            tabHuaiJiu = tabLayout.getTabAt(PAGE_HuaiJiu);
            tabQingXin = tabLayout.getTabAt(PAGE_QingXin);
            tabLangMan = tabLayout.getTabAt(PAGE_LangMan);
            tabXingGan = tabLayout.getTabAt(PAGE_XingGan);
            tabShangGan = tabLayout.getTabAt(PAGE_ShangGan);
            tabZhiYu = tabLayout.getTabAt(PAGE_ZhiYu);
//        }
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

//    private RecyclerView recyclerView;
//    private List<Beauty> data = new ArrayList<>();



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout., container, false);
//
//        recyclerView = (RecyclerView) view.findViewById(R.id.rv_online_music_list);
//        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
//        StaggeredGridLayoutManager recyclerViewLayoutManager =
//                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(recyclerViewLayoutManager);
//
//        BeautyAdapter adapter = new BeautyAdapter(data, getActivity());
//        //设置adapter
//        recyclerView.setAdapter(adapter);
//
//        return view;
//    }

    /**
     * 生成一些数据添加到集合中
     * 怀旧| 清新| 浪漫| 性感| 伤感| 治愈| 放松| 孤独| 感动| 兴奋| 快乐| 安静| 思念|
     */
//    Beauty beauty;
//    beauty = new Beauty("怀旧", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("清新", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("浪漫", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("性感", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("伤感", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("治愈", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("放松", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("孤独", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("感动", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("兴奋", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("快乐", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("安静", R.mipmap.ic_launcher);
//        data.add(beauty);
//    beauty = new Beauty("思念", R.mipmap.ic_launcher);
//        data.add(beauty);
}
