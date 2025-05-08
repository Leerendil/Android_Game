package com.example.android_game.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.android_game.game_states.Playing;

public class PlayingUi {

    //For UI
    private final PointF joystickCenterPos = new PointF(300, 750);
    private final PointF attackBtnCenterPos = new PointF(1800, 750);
    private final float radius = 150;
    private final Paint circlePaint;

    //FOR MULTITOUCH
    private int joystickPointerId = -1;
    private int attackButtonPointerId = -1;
    private boolean touchDown;
    private CustomButton btnMenu;
    private final Playing playing;
    public PlayingUi(Playing playing) {
        this.playing = playing;

        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStrokeWidth(5);
        circlePaint.setStyle(Paint.Style.STROKE);

        btnMenu = new CustomButton(5, 5, ButtonImages.PLAYING_MENU.getWidth(), ButtonImages.PLAYING_MENU.getHeight());
    }

    public void draw(Canvas c) {
        drawUI(c);
    }

    public void drawUI(Canvas c) {
        c.drawCircle(joystickCenterPos.x, joystickCenterPos.y, radius, circlePaint);
        c.drawCircle(attackBtnCenterPos.x, attackBtnCenterPos.y, radius, circlePaint);

        c.drawBitmap(
                ButtonImages.PLAYING_MENU.getBtnImg(btnMenu.isPushed(btnMenu.getPointerId())),
                btnMenu.getHitbox().left,
                btnMenu.getHitbox().top,
                null

        );
    }

    private boolean isInsideRadius(PointF eventPos, PointF circle) {
        float a = Math.abs(eventPos.x - circle.x);
        float b = Math.abs(eventPos.y - circle.y);
        float c = (float) Math.hypot(a, b);

        return c <= radius;
    }

    private boolean checkInsideAttackBtn(PointF eventPos) {
        return isInsideRadius(eventPos, attackBtnCenterPos);
    }
    private boolean checkInsideJoyStick(PointF eventPos, int pointerId) {

        if(isInsideRadius(eventPos, joystickCenterPos)) {
            touchDown = true;
            joystickPointerId = pointerId;
            return true;
        }

        return false;
    }

    private void spawnSkeleton() {
        playing.spawnSkeleton();
    }
    public void touchEvents(MotionEvent event) {
        final int action = event.getActionMasked();
        final int actionIndex = event.getActionIndex();
        final int pointerId = event.getPointerId(actionIndex);

        final PointF eventPos = new PointF(event.getX(actionIndex), event.getY(actionIndex));

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:

                if (checkInsideJoyStick(eventPos, pointerId)) {
                    touchDown = true;
                } else if(checkInsideAttackBtn(eventPos)) {
                    if (attackButtonPointerId < 0) {
                        playing.setAttacking(true);
                        attackButtonPointerId = pointerId;
                    }
                }
                else {
                    if (isIn(eventPos, btnMenu))
                        btnMenu.setPushed(true, pointerId);
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if(touchDown)
                    for(int i = 0; i < event.getPointerCount(); i++) {
                        if(event.getPointerId(i) == joystickPointerId) {
                            float xDiff = event.getX(i) - joystickCenterPos.x;
                            float yDiff = event.getY(i) - joystickCenterPos.y;
                            playing.setPlayerMoveTrue(new PointF(xDiff, yDiff));
                        }
                    }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                if(pointerId == joystickPointerId) {
                   resetJoystickButton();
                } else {
                    if (isIn(eventPos, btnMenu))
                        if (btnMenu.isPushed(pointerId)) {
                            resetJoystickButton();
                            playing.setGameStateToMenu();
                        }
                    btnMenu.unPush(pointerId);
                    if(pointerId == attackButtonPointerId) {
                        playing.setAttacking(false);
                        attackButtonPointerId = -1;
                    }
                }
                break;
        }
    }

    private void resetJoystickButton() {
        touchDown = false;
        playing.setPlayerMoveFalse();
        joystickPointerId = -1;
    }
    private boolean isIn(PointF eventPos, CustomButton b) {
        return b.getHitbox().contains(eventPos.x, eventPos.y);
    }
}
