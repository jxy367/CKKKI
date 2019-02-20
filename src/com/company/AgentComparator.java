package com.company;

import java.util.Comparator;

public class AgentComparator implements Comparator<ComputerAgent> {
    public int compare(ComputerAgent o1, ComputerAgent o2) {
        return (int) (o2.getScore() - o1.getScore());
    }
}
