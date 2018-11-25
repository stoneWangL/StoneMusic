package com.stone.stonemusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.stone.stonemusic.bean.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicUtil {
    public static final String TAG = "MusicUtil";
    private List<Music> musicList = new ArrayList<>();

    public MusicUtil() {
    }

    /**
     * 获取音乐文件的各种路径
     * @param context
     * @return
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
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return musicList;
    }






}
