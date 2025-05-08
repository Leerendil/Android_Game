package com.example.android_game.helpers.interfaces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.android_game.helpers.GameConstants;

public interface BitmapMethods {

    BitmapFactory.Options options = new BitmapFactory.Options();

    default Bitmap getScaleBitmap(Bitmap bitmap) { return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * GameConstants.Sprites.SCALE_MULTIPLIER, bitmap.getHeight()* GameConstants.Sprites.SCALE_MULTIPLIER  , false);}
}
