package com.company;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Game g = new Game(new HumanAgent(), new HumanAgent());
        g.play();
        System.out.println("Winner: " + g.getWinner());

	    /* Test of board
        for(int i=0; i < 100000; i++){
            if(i > 0 && i%1000 == 0){
                System.out.println(i);
            }
            Board b = new Board();
            //System.out.println("---------\n");

            Random r = new Random();
            for(int j=0; j < 2000; j++){
                b.place(r.nextInt(100));
                if(!b.getEnd()) {
                    String b_state = b.getState();
                    b = new Board(b_state);
                }
            }

            if(!b.getEnd()) {
                System.out.println(b);
                System.out.println(b.getState());
                System.out.println(b.getPiecesPerCol());

                System.out.println(b.getEnd());
                System.out.println(b.getWinner());
            }

        }
        */
    }

}
