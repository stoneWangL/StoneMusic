package com.stone.stonemusic.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.BeautyAdapter;
import com.stone.stonemusic.model.Beauty;

import java.util.ArrayList;
import java.util.List;

public class MVFragment3 extends Fragment {

    private RecyclerView recyclerView;
    private List<Beauty> data = new ArrayList<>();

    public MVFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_online_music_list);
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        initData();
        BeautyAdapter adapter = new BeautyAdapter(data, getActivity());
        //设置adapter
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * 生成一些数据添加到集合中
     * 怀旧| 清新| 浪漫| 性感| 伤感| 治愈| 放松| 孤独| 感动| 兴奋| 快乐| 安静| 思念|
     */
    private void initData() {
        Beauty beauty;
        beauty = new Beauty("怀旧", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("清新", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("浪漫", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("性感", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("伤感", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("治愈", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("放松", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("孤独", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("感动", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("兴奋", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("快乐", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("安静", R.mipmap.ic_launcher);
        data.add(beauty);
        beauty = new Beauty("思念", R.mipmap.ic_launcher);
        data.add(beauty);

    }

}
