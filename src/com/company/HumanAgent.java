package com.company;
import java.util.Scanner;

public class HumanAgent implements Agent{
    private Board board;

    public int play(String board_state){
        board = new Board(board_state);
        showGame();
        int col_decision = getDecision();
        while(!board.place(col_decision)){
            col_decision = getDecision();
        }
        return col_decision;
    }

    public void onGameFinished(String[] board_states, Piece ownPiece, Result result) {
    }

    private void showGame(){
        System.out.println(board);
    }

    private int getDecision() {
        Scanner scan = new Scanner(System.in);
        int col_decision;
        do {
            System.out.println("Please select a column (0-6)");
            while (!scan.hasNextInt()) {
                System.out.println("Please select a column (0-6)");
                scan.next();
            }
            col_decision = scan.nextInt();
        } while (col_decision < 0 || col_decision > 6);

        return col_decision;
    }
}
