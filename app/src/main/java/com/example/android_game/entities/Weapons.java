package com.example.android_game.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.android_game.R;
import com.example.android_game.helpers.interfaces.BitmapMethods;
import com.example.android_game.main.MainActivity;

public enum Weapons implements BitmapMethods {
    BIG_SWORD(R.drawable.big_sword_hand);

    final Bitmap weaponImg;
    Weapons(int resId) {
        options.inScaled = false;
        weaponImg = getScaleBitmap(BitmapFactory.decodeResource(MainActivity.getGamecontext().getResources(), resId, options));
    }

    public Bitmap getWeaponImg() {
        return weaponImg;
    }

    public int getWidth() {
        return weaponImg.getWidth();
    }
    public int getHeight() {
        return weaponImg.getHeight();

    }
}
