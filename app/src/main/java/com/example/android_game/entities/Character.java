package com.example.android_game.entities;

import static com.example.android_game.helpers.GameConstants.Sprites.SIZE;

import android.graphics.PointF;

import com.example.android_game.helpers.GameConstants;

public abstract class Character extends Entity{

    protected  int aniTick, aniIndex;
    protected int faceDir = GameConstants.Face_Dir.DOWN;
    protected final GameChars gameCharType;
    public Character(PointF pos, GameChars gameCharType) {
        super(pos, SIZE, SIZE);
        this.gameCharType = gameCharType;
    }

    protected void updateAnimation() {
        aniTick++;
        if (aniTick >= GameConstants.Animation.SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GameConstants.Animation.AMOUNT) {aniIndex = 0;}}
    }

    public void resetAnimation() {
        aniTick = 0;
        aniIndex = 0;
    }

    public GameChars getGameCharType() {
        return gameCharType;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getFaceDir() {
        return faceDir;
    }

    public void setFaceDir(int faceDir){
        this.faceDir = faceDir;
    }
}
