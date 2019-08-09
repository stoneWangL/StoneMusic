package com.stone.stonemusic.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.stone.stonemusic.model.ArtistModel;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.presenter.impl.MusicObserverManager;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MusicApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MusicResources {
    public static final String TAG = "MusicResources";
    private List<Music> musicList = new ArrayList<>();
    public static HashMap<String, ArrayList<Music>> artistMap= new HashMap<>();
    public static List<ArtistModel> artistModelList = new ArrayList<>();
    private static ArtistModel artistModel;
    private ArrayList<Music> artistList;


    public MusicResources() {
    }

    /**
     * 获取音乐文件的各种路径
     * @param context 上下文
     * @return List<Music> Music的list
     */
    public List<Music> getMusic(Context context){
        Cursor cursor = null;
        try{
            cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if (cursor != null){
                while (cursor.moveToNext()){
                    Music m = new Music();
                    long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    int ismusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

                    if (ismusic != 0 && duration / (1000 * 60) >= 1) {
                        m.setId(id);
                        m.setTitle(title);
                        m.setArtist(artist);
                        m.setDuration(duration);
                        m.setSize(size);
                        m.setFileUrl(url);
                        m.setAlbum(album);
                        m.setAlbum_id(album_id);
                        musicList.add(m);

                        //加入artistHashMap 录入数据
                        if (artistMap.containsKey(artist)) {
                            Set<Map.Entry<String, ArrayList<Music>>> sAll = artistMap.entrySet();
                            for (Map.Entry<String, ArrayList<Music>> mE : sAll) {
                                if (mE.getKey().equals(artist))
                                    mE.getValue().add(m);
                            }
                        } else {
                            artistList = new ArrayList<>();
                            artistList.add(m);
                            artistMap.put(artist, artistList);
                        }
                    }

                }
            }
            //统计艺术家/歌手栏一级Mode 初始化
//            initArtistMode();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }

        return musicList;
    }

    /**
     * 统计艺术家/歌手栏一级Mode 初始化
     *
     * 在
     */
    public static void initArtistMode() {
        Set<Map.Entry<String, ArrayList<Music>>> sAll = artistMap.entrySet();
        for (Map.Entry<String, ArrayList<Music>> mE : sAll) {
            Log.e(TAG, "artist = " + mE.getKey() +
                "\n num = " + mE.getValue().size());
            artistModel = new ArtistModel();
            artistModel.setArtist(mE.getKey());
            artistModel.setNum(mE.getValue().size());
//            String path = SearchImageUtil.getImageUrl(mE.getKey());
//            if (null == path)
//                artistModel.setPath(null);
//            else
//                artistModel.setPath(path);
            String path = getAlbumArt(new Long(mE.getValue().get(0).getAlbum_id()).intValue());
            artistModel.setPath(path);

            artistModelList.add(artistModel);
        }
        //防止初始化耗时太久，完成后通知主界面更新数据
        MusicObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_INIT_FINISHED);
    }

    /**
     * 输入歌手，返回该歌手的MusicList
     * @param artist
     * @return
     */
    public static ArrayList<Music> getSameArtistMusicList(String artist) {
        Set<Map.Entry<String, ArrayList<Music>>> sAll = artistMap.entrySet();
        for (Map.Entry<String, ArrayList<Music>> mE : sAll) {
            Log.i(TAG, "artist = " + mE.getKey() +
                    "\n num = " + mE.getValue().size());
            if (artist.equals(mE.getKey()))
                return mE.getValue();
        }
        return null;
    }

    /**
     * 获取图片路径
     * @param album_id
     * @return
     */
    public static String getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = MusicApplication.getContext().getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (null != cur && cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
//        cur = null;
        Log.d(TAG,"album_art是="+album_art);
        return album_art;
    }




}
