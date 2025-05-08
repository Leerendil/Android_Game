package com.example.android_game.environments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.android_game.main.MainActivity;
import com.example.android_game.R;
import com.example.android_game.helpers.GameConstants;
import com.example.android_game.helpers.interfaces.BitmapMethods;

public enum Floor implements BitmapMethods {

    OUTSIDE(R.drawable.tileset_floor, 22, 26);

    private Bitmap[] sprites;
    Floor(int resID, int tilesInWidth, int tilesInHeight) {
        options.inScaled = false;
        sprites = new Bitmap[tilesInHeight * tilesInWidth];
        Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGamecontext().getResources(), resID, options);
        for (int j = 0; j < tilesInHeight; j++) {
            for (int i = 0; i < tilesInWidth; i++) {
                int index = j * tilesInWidth + i;
                sprites[index] = getScaleBitmap(Bitmap.createBitmap(spriteSheet, GameConstants.Sprites.DEFAULT_SIZE * i, GameConstants.Sprites.DEFAULT_SIZE * j, GameConstants.Sprites.DEFAULT_SIZE, GameConstants.Sprites.DEFAULT_SIZE));
            }
        }

    }

    public Bitmap getSprite(int id) {
        return sprites[id];
    }

}
