package org.firstinspires.ftc.teamcode.util;

import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {
    private final Queue<Double> Dataset = new LinkedList<Double>();
    private final int period;
    private double sum;

    public MovingAverage(int period) {
        this.period = period;
    }

    public void addData(double num) {
        sum += num;
        Dataset.add(num);

        if (Dataset.size() > period) {
            sum -= Dataset.remove();
        }
    }

    public double getMean() {
        return sum / Math.min(Dataset.size(), period);
    }

    public boolean isFull() {
        return Dataset.size() == period;
    }
}