package com.company;

import java.util.HashMap;
import java.util.stream.DoubleStream;

public class TestAIAgent implements AI_Agent {
    private HashMap<Result, Integer> win_lose_draw;
    private double[] weights;

    public TestAIAgent(String weights) {
        // Initialize WLD HashMap
        win_lose_draw = new HashMap<>();
        for (Result r : Result.values()) {
            win_lose_draw.put(r, 0);
        }

        // Setup weights
        String[] str_weights = weights.split(",");
        this.weights = new double[str_weights.length];
        for (int i = 0; i < str_weights.length; i++) {
            this.weights[i] = Double.valueOf(str_weights[i]);
        }

        normalizeWeights();

    }

    public void loadWeights(String weights) {
        String[] str_weights = weights.split(",");
        this.weights = new double[str_weights.length];
        for (int i = 0; i < str_weights.length; i++) {
            this.weights[i] = Double.valueOf(str_weights[i]);
        }

        normalizeWeights();
    }

    public String saveWeights() {
        StringBuilder sb = new StringBuilder();
        for (double d : weights) {
            sb.append(d);
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }

    private void normalizeWeights() {
        double sum = DoubleStream.of(weights).sum();
        if (sum != 11) {
            sum = sum - weights[weights.length - 1];
            for (int i = 0; i < weights.length; i++) {
                weights[i] = weights[i] / (sum / 10);
            }
            weights[weights.length - 1] = 10;
        }
    }

    public double getScore() {
        return (1.0 * win_lose_draw.get(Result.WIN)) + (0.5 * win_lose_draw.get(Result.DRAW)) + (0.0 * win_lose_draw.get(Result.LOSE));
    }

    public int play(String board_state) {
        return calculateBestColumn(board_state);
    }

    public void onGameFinished(String[] board_states, Piece ownPiece, Result result) {
        win_lose_draw.put(result, win_lose_draw.get(result) + 1);
    }

    private int calculateBestColumn(String board_state) {
        Board current_board = new Board(board_state);

        int best_col = -1;
        double best_next_board_value = -100;
        //System.out.println("--------");
        for (int col = 0; col <= 7; col++) {
            Board test_board = new Board(board_state);
            if (test_board.place(col)) {
                String test_board_state = test_board.getState();
                double test_next_board_value = calculateBoardValue(test_board_state, current_board.getTurn());
                //System.out.println(test_next_board_value);
                if (test_next_board_value > best_next_board_value) {
                    best_col = col;
                    best_next_board_value = test_next_board_value;
                }
            }
        }

        if (best_col == -1) {
            throw new IllegalArgumentException("Board state has no more moves");
        }

        return best_col;


        //Random r = new Random();
        //return r.nextInt(10);
    }

    private double calculateBoardValue(String board_state, Piece player) {
        Board b = new Board(board_state);
        int[] board_features = b.getBoardFeatures(player);
        assert board_features.length == weights.length : "Board features and weights are different lengths";

        double board_value = 0;

        for (int i = 0; i < board_features.length; i++) {
            if (i < 8) {
                board_value = board_value - (board_features[i] * weights[i]);
            } else {
                board_value = board_value + (board_features[i] * weights[i]);
            }
        }
        return board_value;
    }


}
