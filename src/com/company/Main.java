package com.company;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        TestAIAgent o = new TestAIAgent(makeWeights());
        TestAIAgent x = new TestAIAgent(makeWeights());

        for (int i = 0; i < 100000; i++) {
            if (i > 0 && i % 1000 == 0) {
                System.out.println(i);
            }

            o.loadWeights(makeWeights());
            x.loadWeights(makeWeights());

            Game g = new Game(o, x);
            g.play();
        }
        System.out.println(o.getScore() + ":" + x.getScore()); // 61.1 : 39.9


        /* Test random TestAIAgent
        TestAIAgent o = new TestAIAgent("10");
        TestAIAgent x = new TestAIAgent("10");

        for(int i=0; i < 1000000; i++) {
            if(i > 0 && i % 10000 == 0){
                System.out.println(i);
            }
            Game g = new Game(o, x);
            g.play();
        }
        System.out.println(o.getScore() + ":" + x.getScore()); // 55.7 : 44.3
        */

        /* Testing Game and HumanAgent
        Game g = new Game(new HumanAgent(), new HumanAgent());
        g.play();
        System.out.println("Winner: " + g.getWinner());
        */

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

    private static String makeWeights() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 17; i++) {
            sb.append(r.nextInt(100));
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }
}
