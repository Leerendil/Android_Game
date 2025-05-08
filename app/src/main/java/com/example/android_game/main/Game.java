package com.example.android_game.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.android_game.game_states.Menu;
import com.example.android_game.game_states.Playing;

public class Game {

    private SurfaceHolder holder;
    private Menu menu;
    private Playing playing;
    private GameState currentGameState = GameState.MENU;
    private final LoopThread loopThread;
    public Game(SurfaceHolder holder) {
        this.holder = holder;
        this.loopThread = new LoopThread(this);
        initGameStates();
    }

    public void update(double delta) {
        switch (currentGameState) {
            case MENU:
                menu.update(delta);
                break;
            case PLAYING:
                playing.update(delta);
                break;
        }
    }

    public void render() {
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);

        //Draw the game
        switch (currentGameState) {
            case MENU:
                menu.render(c);
                break;
            case PLAYING:
                playing.render(c);
                break;
        }

        holder.unlockCanvasAndPost(c);
    }
    private void initGameStates() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    public boolean touchEvent(MotionEvent event) {
        switch (currentGameState) {
            case MENU:
                menu.touchEvents(event);
                break;
            case PLAYING:
                playing.touchEvents(event);
                break;
        }

        return true;
    }

    public void startLoop() {
        loopThread.startLoop();
    }

    public enum GameState{
        MENU,
        PLAYING;
    }

    public GameState getCurrentGameState() {
        return  currentGameState;
    }
    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }
}
