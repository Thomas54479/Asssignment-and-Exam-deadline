package com.deadlineplanner;
import java.time.LocalDate;

public abstract  class Task {
    private String course;
    private String title;
    private LocalDate dueDate;
    private boolean done;

    public Task(String course, String title, LocalDate duDate, boolean done){
        this.course = course;
        this.title = title;
        this.dueDate = duDate;
        this.done = done;
    }
    public String getCourse()       { return course; }
    public String getTitle()        { return title; }
    public LocalDate getDueDate()    { return dueDate; }
    public boolean isDone()         { return done; }

    public void setDone(boolean done) {
    this.done = done;
    }
    public void markDone() {
        this.done = true;
    }
    public abstract int getPriorityScore();

}