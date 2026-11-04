
package com.deadlineplanner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Assignment extends Task {

    private double weightPercent;

    public Assignment(String course, String title, LocalDate dueDate,
                      boolean done, double weightPercent) {
        super(course, title, dueDate, done);
        setWeightPercent(weightPercent);
    }

    public double getWeightPercent() {
        return weightPercent;
    }

    public void setWeightPercent(double weightPercent) {
        if (weightPercent < 0 || weightPercent > 100) {
            throw new IllegalArgumentException("Weight must be 0 to 100");
        }

        this.weightPercent = weightPercent;
    }

    @Override
    public int getPriorityScore() {

        if (isDone()) {
            return 0;
        }

        long daysLeft = ChronoUnit.DAYS.between(
            LocalDate.now(), getDueDate()
        );

        if (daysLeft < 0) {
            return 100;
        }

        return (int) Math.min(
            100,
            Math.round(weightPercent / Math.max(daysLeft, 1))
        );
    }
}

