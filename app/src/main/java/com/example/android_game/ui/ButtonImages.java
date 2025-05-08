package com.example.android_game.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.android_game.R;
import com.example.android_game.helpers.interfaces.BitmapMethods;
import com.example.android_game.main.MainActivity;

public enum ButtonImages implements BitmapMethods {
    MENI_START(R.drawable.mainmenu_start_buttons, 610, 210),
    PLAYING_MENU(R.drawable.home_buttons, 230, 220);

    private int width, height;
    private Bitmap normal, pushed;
    ButtonImages(int resID, int width, int height) {
        options.inScaled = false;
        this.width = width;
        this.height = height;

        Bitmap buttonAtlas = BitmapFactory.decodeResource(MainActivity.getGamecontext().getResources(), resID, options);
        normal = Bitmap.createBitmap(buttonAtlas, 0, 0, width, height);
        pushed = Bitmap.createBitmap(buttonAtlas, width, 0, width, height);
    }

    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public Bitmap getBtnImg(boolean isBtnPushed) {

//        if(isBtnPushed)
//            return pushed;
//        return normal;

        return isBtnPushed ? pushed : normal;
    }
}
