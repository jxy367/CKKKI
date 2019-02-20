package com.company;

public interface Agent {
    int play(String board_state);

    void onGameFinished(String[] board_states, Piece ownPiece, Result result);
}
