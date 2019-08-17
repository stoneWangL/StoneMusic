package com.stone.stonemusic.model.bean;

/**
 * ItemViewChoose的懒汉式单例
 */
public class ItemViewChoose {
    private static ItemViewChoose sItemViewChoose = null;
    private int itemChoosePosition = -1;

    private ItemViewChoose(){}

    //单例
    public static ItemViewChoose getInstance(){
        if (sItemViewChoose == null) {
            sItemViewChoose = new ItemViewChoose();
        }
        return sItemViewChoose;
    }

    public int getItemChoosePosition() {
        return itemChoosePosition;
    }

    public void setItemChoosePosition(int itemChoosePosition) {
        this.itemChoosePosition = itemChoosePosition;
    }
}
