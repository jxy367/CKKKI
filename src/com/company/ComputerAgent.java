package com.company;

public interface ComputerAgent extends Agent {
    void loadWeights(String weights);

    String saveWeights();

    double getScore();
}
