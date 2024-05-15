package com.nopalsoft.ninjarunner.handlers;

public interface RequestHandler {
    void showRater();

    /**
     * Normalmente se llama cuando se inicia el juego
     */
    void loadInterstitial();

    void showMoreGames();

    void shareApp();

}
