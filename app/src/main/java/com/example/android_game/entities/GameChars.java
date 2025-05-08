package com.example.android_game.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.android_game.main.MainActivity;
import com.example.android_game.R;
import com.example.android_game.helpers.GameConstants;
import com.example.android_game.helpers.interfaces.BitmapMethods;

public enum GameChars implements BitmapMethods {
    PLAYER(R.drawable.knight_spritesheet),
    SKELETON(R.drawable.skeleton_spritesheet);
    private final Bitmap spriteSheet;
    private final Bitmap[][] sprites = new Bitmap[7][4];
    GameChars(int resID) {
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGamecontext().getResources(), resID, options);
        for (int j = 0; j < sprites.length; j++) {
            for (int i = 0; i < sprites[j].length; i++)
                sprites[j][i] = getScaleBitmap(Bitmap.createBitmap(spriteSheet, GameConstants.Sprites.DEFAULT_SIZE * i, GameConstants.Sprites.DEFAULT_SIZE * j, GameConstants.Sprites.DEFAULT_SIZE, GameConstants.Sprites.DEFAULT_SIZE));
        }
    }

    public Bitmap getSpriteSheet() {
        return spriteSheet;
    }

    public Bitmap getSprite(int yPos, int xPos) {
        return sprites[yPos][xPos];
    }

}
