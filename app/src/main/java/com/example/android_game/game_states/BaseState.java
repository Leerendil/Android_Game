package com.example.android_game.game_states;

import com.example.android_game.main.Game;

public class BaseState {

    protected Game game;
    public BaseState(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
