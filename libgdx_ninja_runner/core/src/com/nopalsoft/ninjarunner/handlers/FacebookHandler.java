package com.nopalsoft.ninjarunner.handlers;

public interface FacebookHandler {

    void facebookSignIn();

    boolean facebookIsSignedIn();

    void facebookGetScores();

    void facebookSubmitScore(final long score);

}
