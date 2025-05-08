package com.example.android_game.game_states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.android_game.helpers.interfaces.GameState;
import com.example.android_game.main.Game;
import com.example.android_game.main.MainActivity;
import com.example.android_game.ui.ButtonImages;
import com.example.android_game.ui.CustomButton;
import com.example.android_game.ui.GameImages;

public class Menu extends BaseState implements GameState {

    private Paint paint;
    private final CustomButton btnStart;
    private final int menuX = MainActivity.GAME_WIDTH / 1000;
    private final int menuY = 1;
    private int btnStartX = menuX + GameImages.MAIN_MENU_BK.getImage().getWidth()/3 - ButtonImages.MENI_START.getWidth()/3 - 170;
    private int btnStartY = menuY + 400;

    public Menu(Game game) {
        super(game);

        paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);

        btnStart = new CustomButton(btnStartX, btnStartY, ButtonImages.MENI_START.getWidth(), ButtonImages.MENI_START.getHeight());
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {

        c.drawBitmap(
                GameImages.MAIN_MENU_BK.getImage(),
                menuX,
                menuY,
                null
        );

        c.drawBitmap(
                ButtonImages.MENI_START.getBtnImg(btnStart.isPushed()),
                btnStart.getHitbox().left,
                btnStart.getHitbox().top,
                null
        );
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isIn(event, btnStart))
                btnStart.setPushed(true);
        }else if(event.getAction() == MotionEvent.ACTION_UP) {
            if (isIn(event, btnStart))
                if(btnStart.isPushed())
                    game.setCurrentGameState(Game.GameState.PLAYING);
            btnStart.setPushed(false);
        }
    }

    private boolean isIn(MotionEvent e, CustomButton b) {
        return b.getHitbox().contains(e.getX(), e.getY());
    }
}
