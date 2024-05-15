package com.nopalsoft.ninjarunner.handlers;

public interface GameServicesHandler {

    /**
     * Este metodo abstrae a GPGS o a AGC
     */
    void submitScore(long score);

    void getLeaderboard();

    void getScores();

    void getAchievements();

    boolean isSignedIn();

    void signIn();

}
