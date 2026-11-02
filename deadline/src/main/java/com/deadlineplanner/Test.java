package com.deadlineplanner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Test extends Task {

    public Test(String course, String title, LocalDate dueDate, boolean done) {
        super(course, title, dueDate, done);
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

        int priority = 50;

        // Increase priority during the final 7 days
        if (daysLeft <= 7) {
            priority += (int) (50.0 / (daysLeft + 1));
        }

        return Math.min(priority, 100);
    }
}

