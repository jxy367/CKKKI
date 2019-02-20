package com.company;

enum Result {
    WIN, DRAW, LOSE
}


public class Game {
    private Agent O;
    private Agent X;
    private Board board;
    private String[] board_states;
    private int turn_number;

    public Game(Agent agent1, Agent agent2){
        this.O = agent1;
        this.X = agent2;
        this.board = new Board();
        this.board_states = new String[43];
        this.turn_number = 0;
    }


    public void play(){
        board_states[turn_number] = board.getState();

        while(!board.getEnd()){
            int play_col;
            Piece turn = board.getTurn();

            if(turn == Piece.O){
                play_col = O.play(board.getState());
            } else {
                play_col = X.play(board.getState());
            }

            if (board.place(play_col)) {
                turn_number++;
                board_states[turn_number] = board.getState();
            }
        }

        //System.out.println(board);

        onGameFinished();

    }

    private Piece getWinner() {
        return board.getWinner();
    }

    /* Agent updating to be done on completion of a game */
    private void onGameFinished() {
        if (getWinner() == Piece.O) {
            O.onGameFinished(board_states, Piece.O, Result.WIN);
            X.onGameFinished(board_states, Piece.X, Result.LOSE);
        } else if (getWinner() == Piece.X) {
            O.onGameFinished(board_states, Piece.O, Result.LOSE);
            X.onGameFinished(board_states, Piece.X, Result.WIN);

        } else {
            O.onGameFinished(board_states, Piece.O, Result.DRAW);
            X.onGameFinished(board_states, Piece.X, Result.DRAW);
        }
    }
}
