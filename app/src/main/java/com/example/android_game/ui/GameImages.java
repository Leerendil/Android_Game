package com.example.android_game.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.android_game.R;
import com.example.android_game.helpers.interfaces.BitmapMethods;
import com.example.android_game.main.MainActivity;

public enum GameImages implements BitmapMethods {
    MAIN_MENU_BK(R.drawable.bkg1);
    private final Bitmap image;
    GameImages(int resID) {
        options.inScaled = false;
        image = BitmapFactory.decodeResource(MainActivity.getGamecontext().getResources(), resID, options);
    }

    public Bitmap getImage() {
        return  image;
    }

}
