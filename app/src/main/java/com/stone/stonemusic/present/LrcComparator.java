package com.stone.stonemusic.present;

import com.stone.stonemusic.model.LrcContent;

import java.util.Comparator;

public class LrcComparator implements Comparator<LrcContent>{

    @Override
    public int compare(LrcContent o1, LrcContent o2) {

        return o1.getLrcTime()-o2.getLrcTime();
    }

}
