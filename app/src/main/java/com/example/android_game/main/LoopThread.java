package com.example.android_game.main;

public class LoopThread implements Runnable {

    private final Thread gameThraed;
    private final Game game;

    public LoopThread(Game game) {
        this.game = game;
        gameThraed = new Thread(this);
    }

    @Override
    public void run() {

        long last_FPS_Check = System.currentTimeMillis();
        int fps = 0;

        long lastDelta = System.nanoTime();
        long nanoSec = 1_000_000_000;

        while(true) {

            long nowDelta = System.nanoTime();
            double timeSinceLastDelta = nowDelta - lastDelta;
            double delta = timeSinceLastDelta / nanoSec;

            game.update(delta);
            game.render();
            lastDelta = nowDelta;
            fps++;

            long now = System.currentTimeMillis();
            if (now - last_FPS_Check >= 1000) {
                System.out.println("FPS: " + fps);
                fps = 60; last_FPS_Check += 1000;}
        }
    }

    public void startLoop() {
        gameThraed.start();
    }
}
