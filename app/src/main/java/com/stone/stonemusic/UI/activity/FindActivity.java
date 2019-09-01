package com.stone.stonemusic.UI.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.View.FindView;
import com.stone.stonemusic.adapter.FindAdapter;
import com.stone.stonemusic.base.BaseHaveBottomBarActivity;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.impl.FindPresenterImpl;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.playControl.PlayControl;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/28 17:28
 * @Description:
 */
public class FindActivity extends BaseHaveBottomBarActivity
        implements View.OnClickListener, FindView {
    private static String TAG = "FindActivity";
    private EditText editText;
    private TextView textViewResultShow;
    private ImageView ivFind, ivBack;
    FindPresenterImpl findPresenter;
    private FindAdapter findAdapter;
    private ListView listView;
    private List<Music> musicList = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    protected void initListenerOther() {
        editText = findViewById(R.id.editText_find);
        textViewResultShow = findViewById(R.id.textView_result_show);

        ivFind = findViewById(R.id.imageView_find_search);
        ivFind.setOnClickListener(this);
        ivBack = findViewById(R.id.imageView_find_back);
        ivBack.setOnClickListener(this);

        listView = findViewById(R.id.listView_find);


    }

    @Override
    protected void initDataOther() {
        findPresenter = new FindPresenterImpl(this);
        readMusic();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Music> lastMusicList = SongModel.getInstance().getChooseSongList();
                //在线find item被点击，说明当前需要播放的歌曲需要切换到在线
                SongModel.getInstance().setMusicType(PlayType.OnlineType); //将播放类型切换为OnlineType
                //设置当前播放的歌曲类型为搜索到的list
                SongModel.getInstance().setChooseSongList(SongModel.getInstance().getmFindSongList());

                Log.d(TAG, "位置：" + position + "; 歌名：" + musicList.get(position).getTitle());

                MediaUtils.currentSongPosition = position; //设置当前播放位置全局position
                int lastPosition = ItemViewChoose.getInstance().getItemChoosePosition(); //上一个播放的位置
                //点击的是正在播放的歌曲
                if (position == lastPosition
                        && lastMusicList.get(position).getMusicId().equals(
                        SongModel.getInstance().getChooseSongList().get(MediaUtils.currentSongPosition).getMusicId()))
                    jumpToOtherWhere.GoToPlayActivity(); //调用父类方法，跳转到播放Activity
                    //点击的不是当前播放的歌曲
                else {
                    PlayControl.controlBtnPlayDiffSong(); //播放音乐
                    //设置选中的item的位置,这里的position设置与ListView中当前播放位置的标识有关
                    ItemViewChoose.getInstance().setItemChoosePosition(position);
                    findAdapter.notifyDataSetChanged(); //更新adapter
                }
            }

        });
    }

    private void readMusic(){
        try{
            musicList = SongModel.getInstance().getmFindSongList();

            if (null != musicList && musicList.size() > 0){
                findAdapter = new FindAdapter(this, R.layout.item_music, musicList, this);
                listView.setAdapter(findAdapter);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void observerUpDataOtherPlayStartPositionChange() {
        ThisActivityHandler.sendEmptyMessage(2);
    }

    @Override
    protected void observerUpDataOtherContinueStopPause() {

    }

    @Override
    protected void onDestroyOther() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_find_search:
                clickFindBtn(); //搜索按钮被点击
                break;
            case R.id.imageView_find_back:
                finish();
                overridePendingTransition(R.anim.stop, R.anim.right_out);
//                overridePendingTransition(R.anim.right_in,R.anim.push_up_out);
                break;
            default:
                break;
        }
    }




    /**
     * 反馈，查询结果为空
     * @param result 0:表示查询结果为空， 1：表示查询失败， 2：查询成功
     */
    public void feedBackResult(final int result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (result) {
                    case 0:
                        textViewResultShow.setText("查询结果为空");
                        break;
                    case 1:
                        textViewResultShow.setText("查询失败，请检查网络");
                        break;
                    case 2:
                        textViewResultShow.setText("");
                        break;
                }
            }
        });
    }

    @Override
    public void notifyMusicList(final List<Music> list) {
        SongModel.getInstance().setmFindSongList(list);
        ThisActivityHandler.sendEmptyMessage(1);
    }

    @Override
    public void clickItemSet(int position) {
        //获取当前find列表点击位置的信息
        //弹出popUpWindow
        //直接下载
        final String QueryPath = musicList.get(position).getFileUrl();
        Log.i(TAG, "点击的歌曲的播放路径：" + QueryPath);
        //缓存功能先空置吧，以后再做
    }

    /**
     * 收到UI界面更新的通知后，在此刷新UI
     */
    private Handler ThisActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    readMusic();
                    break;
                case 2:
                    findAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void loadSuccess() {
        findPresenter.loadSuccess();
    }

    @Override
    public void loadFalse() {
        findPresenter.loadFalse();
    }

    /**
     * 搜索按钮被点击
     */
    @Override
    public void clickFindBtn() {
        //获取需要查询的字符串
        String findStr = editText.getText().toString();
        findPresenter.clickFindBtn(findStr);
    }
}
