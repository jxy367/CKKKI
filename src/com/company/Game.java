package com.company;

public class Game {
    private Agent O;
    private Agent X;
    private Board board;

    public Game(Agent agent1, Agent agent2){
        this.O = agent1;
        this.X = agent2;
        this.board = new Board();
    }

    private Board getBoard(){
        return board;
    }

    public void play(){
        while(!board.getEnd()){
            int play_col;
            Piece turn = board.getTurn();

            if(turn == Piece.O){
                play_col = O.play(board.getState());
            }
            else{
                play_col = X.play(board.getState());
            }

            board.place(play_col);
        }
    }

    public String getWinner(){
        return board.getWinner().toString();
    }
}
