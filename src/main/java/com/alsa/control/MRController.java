package com.alsa.control;

public interface MRController {
    void startGame();

    int getHand();

    void makeMove(int move);

    void endGame();
}
