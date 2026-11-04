
package com.deadlineplanner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Exam extends Task {

    public Exam(String course, String title, LocalDate dueDate, boolean done) {
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

        int priority = 70;

        // Increase priority during the final 14 days
        if (daysLeft <= 14) {
            priority += (int) (30.0 * (15 - daysLeft) / 15);
        }

        return Math.min(priority, 100);
    }
}
