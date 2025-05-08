package com.example.android_game.game_states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.example.android_game.entities.Character;
import com.example.android_game.entities.Player;
import com.example.android_game.entities.Weapons;
import com.example.android_game.entities.enemies.Skeleton;
import com.example.android_game.environments.MapManager;
import com.example.android_game.helpers.GameConstants;
import com.example.android_game.helpers.interfaces.GameState;
import com.example.android_game.main.Game;
import com.example.android_game.ui.ButtonImages;
import com.example.android_game.ui.CustomButton;
import com.example.android_game.ui.PlayingUi;

import java.util.ArrayList;

public class Playing extends BaseState implements GameState {

    private float cameraX, cameraY;
    private boolean movePlayer;
    private PointF lastTouchDiff;
    private MapManager mapManager;
    private Player player;
    private final ArrayList<Skeleton> skeletons;

    private final PlayingUi playingUi;
    private final Paint redPaint;
    private RectF attackBox = null;
    private boolean attacking, attackChecked;

    public Playing(Game game) {
        super(game);

        mapManager = new MapManager();
        player = new Player();
        skeletons = new ArrayList<>();

        playingUi = new PlayingUi(this);

        redPaint = new Paint();
        redPaint.setStrokeWidth(1);
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setColor(Color.RED);

        for(int i = 0; i < 5; i++)
            spawnSkeleton();
        updateWeaponHitbox();
    }

    public void spawnSkeleton() {
        skeletons.add(new Skeleton(new PointF(player.getHitbox().left - cameraX, player.getHitbox().top - cameraY)));
    }

    @Override
    public void update(double delta) {
        updatePlayerMove(delta);
        player.update(delta, movePlayer);
        updateWeaponHitbox();

        if(attacking) if(!attackChecked)
            checkAttack();

        for (Skeleton skeleton : skeletons)
            if(skeleton.isActive())
                skeleton.update(delta);

        mapManager.setCameraValues(cameraX, cameraY);
    }

    private void checkAttack() {
        RectF attackBoxWithoutCamera = new RectF(attackBox);
        attackBoxWithoutCamera.left -= cameraX;
        attackBoxWithoutCamera.top -= cameraY;
        attackBoxWithoutCamera.right -= cameraX;
        attackBoxWithoutCamera.bottom -= cameraY;

        for(Skeleton s : skeletons)
            if(attackBoxWithoutCamera.intersects(s.getHitbox().left, s.getHitbox().top, s.getHitbox().right, s.getHitbox().bottom))
                s.setActive(false);
        attackChecked = true;
    }

    private void updateWeaponHitbox() {
        PointF pos = getWeaponPos();
        float h = getWeaponHeight();
        float w = getWeaponWidth();

        attackBox = new RectF(pos.x, pos.y, pos.x + w, pos.y + h);
    }

    private float getWeaponWidth() {
        float weaponW;
        switch (player.getFaceDir()) {
            case GameConstants.Face_Dir.LEFT:
            case GameConstants.Face_Dir.RIGHT:
                weaponW = Weapons.BIG_SWORD.getHeight();
                break;

            case GameConstants.Face_Dir.UP:
            case GameConstants.Face_Dir.DOWN:
                weaponW = Weapons.BIG_SWORD.getWidth();
                break;
            default:
                weaponW = 0;
                break;
        }
        return weaponW;
    }

    private float getWeaponHeight() {
        float weaponH;
        switch (player.getFaceDir()) {
            case GameConstants.Face_Dir.UP:
            case GameConstants.Face_Dir.DOWN:
                weaponH = Weapons.BIG_SWORD.getHeight();
                break;

            case GameConstants.Face_Dir.LEFT:
            case GameConstants.Face_Dir.RIGHT:
                weaponH = Weapons.BIG_SWORD.getWidth();
                break;
            default:
                weaponH = 0;
                break;
        }
        return weaponH;
    }

    private PointF getWeaponPos() {
        PointF weaponPos;
        switch (player.getFaceDir()) {
            case GameConstants.Face_Dir.UP:
                weaponPos = new PointF(
                        player.getHitbox().left + 1.75f * GameConstants.Sprites.SCALE_MULTIPLIER,
                        player.getHitbox().top - Weapons.BIG_SWORD.getHeight()
                );
                break;
            case GameConstants.Face_Dir.DOWN:
                weaponPos = new PointF(
                        player.getHitbox().left + 2.5f * GameConstants.Sprites.SCALE_MULTIPLIER,
                        player.getHitbox().bottom
                );
                break;
            case GameConstants.Face_Dir.LEFT:
                weaponPos = new PointF(
                        player.getHitbox().left - Weapons.BIG_SWORD.getHeight(),
                        player.getHitbox().bottom - Weapons.BIG_SWORD.getWidth() - 0.75f * GameConstants.Sprites.SCALE_MULTIPLIER
                );
                break;
            case GameConstants.Face_Dir.RIGHT:
                weaponPos = new PointF(
                        player.getHitbox().right,
                        player.getHitbox().bottom - Weapons.BIG_SWORD.getWidth() - 0.75f * GameConstants.Sprites.SCALE_MULTIPLIER
                );
                break;
            default:
                weaponPos = new PointF(0, 0);
                break;
        }
        return weaponPos;
    }

    @Override
    public void render(Canvas c) {
        mapManager.draw(c);

        drawPlayer(c);

        for (Skeleton skeleton : skeletons)
            if(skeleton.isActive())
                drawCharacter(c, skeleton);
        playingUi.drawUI(c);
    }


    private void drawPlayer(Canvas c) {
        c.drawBitmap(
                player.getGameCharType().getSprite(getAniIndex(), player.getFaceDir()),
                player.getHitbox().left,
                player.getHitbox().top,
                null);
        c.drawRect(player.getHitbox(), redPaint);
        if (attacking)
            drawWeapon(c);
    }

    private int getAniIndex() {
        if(attacking) return 4;
        return player.getAniIndex();
    }
    private void drawWeapon(Canvas c) {
        c.rotate(getWeaponRotation(), attackBox.left, attackBox.top);
        c.drawBitmap(
                Weapons.BIG_SWORD.getWeaponImg(),
                attackBox.left + weaponRotAdjustLeft(),
                attackBox.top + weaponRotAdjustTop(),
                null
        );
        c.rotate(getWeaponRotation() * -1, attackBox.left, attackBox.top);
        c.drawRect(attackBox, redPaint);
    }

    private float weaponRotAdjustTop() {
        float weaponRot;
        switch (player.getFaceDir()) {
            case GameConstants.Face_Dir.UP:
            case GameConstants.Face_Dir.LEFT:
                weaponRot = - Weapons.BIG_SWORD.getHeight();
                break;
            default:
                weaponRot = 0;
                break;
        }
        return weaponRot;
    }

    private float weaponRotAdjustLeft() {
        float weaponRot;
        switch (player.getFaceDir()) {
            case GameConstants.Face_Dir.UP:
            case GameConstants.Face_Dir.RIGHT:
                weaponRot = - Weapons.BIG_SWORD.getWidth();
                break;
            default:
                weaponRot = 0;
                break;
        }
        return weaponRot;
    }

    private float getWeaponRotation() {
        float weaponRot;
        switch (player.getFaceDir()) {
            case GameConstants.Face_Dir.UP:
                weaponRot = 180;
                break;
            case GameConstants.Face_Dir.LEFT:
                weaponRot = 90;
                break;
            case GameConstants.Face_Dir.RIGHT:
                weaponRot = 270;
                break;
            default:
                weaponRot = 0;
                break;
        }
        return weaponRot;
    }

    public void drawCharacter(Canvas canvas, Character c) {
        canvas.drawBitmap(
                c.getGameCharType().getSprite(c.getAniIndex(), c.getFaceDir()),
                c.getHitbox().left + cameraX,
                c.getHitbox().top + cameraY,
                null);

        canvas.drawRect(
                c.getHitbox().left + cameraX,
                c.getHitbox().top + cameraY,
                c.getHitbox().right + cameraX,
                c.getHitbox().bottom + cameraY,
                redPaint);
    }

    private void updatePlayerMove(double delta) {
        if(!movePlayer)
            return;
        float baseSpeed = (float) delta * 300;
        float ratio = Math.abs(lastTouchDiff.y)/ Math.abs(lastTouchDiff.x);
        double angle = Math.atan(ratio);

        float xSpeed = (float) Math.cos(angle);
        float ySpeed = (float) Math.sin(angle);

        if(xSpeed > ySpeed) {
            if(lastTouchDiff.x > 0) player.setFaceDir(GameConstants.Face_Dir.RIGHT);
            else player.setFaceDir(GameConstants.Face_Dir.LEFT);
        } else {
            if(lastTouchDiff.y > 0) player.setFaceDir(GameConstants.Face_Dir.DOWN);
            else player.setFaceDir(GameConstants.Face_Dir.UP);
        }

        if (lastTouchDiff.x < 0) {xSpeed *= -1;}
        if (lastTouchDiff.y < 0) {ySpeed *= -1;}

        int pWidth = GameConstants.Sprites.SIZE;
        int pHeight = GameConstants.Sprites.SIZE;

        if(xSpeed <= 0) {pWidth = 0;}
        if(ySpeed <= 0) {pHeight = 0;}

        float deltaX =  xSpeed * baseSpeed * -1;
        float deltaY =  ySpeed * baseSpeed * -1;

        if (mapManager.canMoveHere(player.getHitbox().left + cameraX * -1 + deltaX * -1 + pWidth, player.getHitbox().top + cameraY * -1 + deltaY * -1 + pHeight))  {
            cameraX += deltaX;
            cameraY += deltaY;
        }
    }

    public void setGameStateToMenu() {
        game.setCurrentGameState(Game.GameState.MENU);
    }

    public void setPlayerMoveTrue(PointF lastTouchDiff) {
        movePlayer = true;
        this.lastTouchDiff = lastTouchDiff;
    }

    public void setPlayerMoveFalse() {
        movePlayer = false;
        player.resetAnimation();
    }

    @Override
    public void touchEvents(MotionEvent event) {
        playingUi.touchEvents(event);
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
        if(!attacking)
            attackChecked = false;
    }
}
