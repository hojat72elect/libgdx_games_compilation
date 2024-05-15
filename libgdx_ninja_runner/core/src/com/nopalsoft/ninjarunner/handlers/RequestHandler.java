package com.nopalsoft.ninjarunner.handlers;

public interface RequestHandler {
    void showRater();

    /**
     * Normalmente se llama cuando se inicia el juego
     */
    void loadInterstitial();

    void showInterstitial();

    void showMoreGames();

    void removeAds();

    void showAdBanner();

    void hideAdBanner();

    void shareApp();

    void buy5milCoins();

    void buy15milCoins();

    void buy30milCoins();

    void buy50milCoins();

}
