package com.example.android_game.helpers.interfaces;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface GameState {

 void update(double delta);
 void render(Canvas c);
 void touchEvents(MotionEvent event);

}
