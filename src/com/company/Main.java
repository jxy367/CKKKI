package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        ArrayList<TestAIAgent> agents = new ArrayList<>();
        ArrayList<TestAIAgent> newAgents = new ArrayList<>();
        ArrayList<TestAIAgent> oldAgents = new ArrayList<>();
        TestAIAgent bestAgent = new TestAIAgent(makeWeights());
        int num_agents = 201;

        for (int round = 1; round <= 50; round++) {
            // Create new agents from old ones and add them to new agents
            for (TestAIAgent a : oldAgents) {
                String old_weights = a.saveWeights();

                newAgents.add(new TestAIAgent(old_weights));

                for (int i = 0; i < 2; i++) {
                    String new_weights = makeNewWeights(old_weights);
                    newAgents.add(new TestAIAgent(new_weights));
                }
            }

            oldAgents = new ArrayList<>();

            //Fill remaining slots of agents
            while (newAgents.size() < num_agents) {
                newAgents.add(new TestAIAgent(makeWeights()));
            }

            agents = newAgents;
            newAgents = new ArrayList<>();

            int total_game_count = num_agents * (num_agents - 1);
            System.out.println("Round: " + round);
            System.out.println("Total number of games: " + total_game_count);
            System.out.println("Number of Games Played By Agent: " + 2 * (num_agents - 1));
            int game_count = 0;
            for (TestAIAgent agent1 : agents) {
                for (TestAIAgent agent2 : agents) {
                    if (agents.indexOf(agent1) != agents.indexOf(agent2)) {
                        if (game_count > 0 && game_count % 1000 == 0) {
                            System.out.println(game_count);
                        }

                        Game g = new Game(agent1, agent2);
                        g.play();
                        game_count++;

                    }
                }
            }

            agents.sort(new AgentComparator());

            for (TestAIAgent a : agents) {
                System.out.println("Score: " + a.getScore() + ", Weights: " + a.saveWeights());
            }

            for (int oldAgentIndex = 0; oldAgentIndex < 5; oldAgentIndex++) {
                oldAgents.add(agents.get(oldAgentIndex));
            }

            bestAgent = agents.get(0);
        }

        System.out.println(bestAgent.saveWeights());
        Game humanGame = new Game(new TestAIAgent(bestAgent.saveWeights()), new HumanAgent());
        humanGame.play();

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
            sb.append(r.nextInt(1000));
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }

    private static String makeNewWeights(String old_weights) {
        String[] str_weights = old_weights.split(",");
        double[] weights = new double[str_weights.length];

        Random r = new Random();

        for (int i = 0; i < str_weights.length; i++) {
            int random_value = r.nextInt(201);
            double change = (random_value - 101.0) / (1000.0);
            weights[i] = Double.valueOf(str_weights[i]) * (1 + change);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 17; i++) {
            sb.append(weights[i]);
            sb.append(",");
        }

        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }
}
