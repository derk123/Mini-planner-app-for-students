package com.example.login;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDate;

public class Task {
    private int id;
    private int userId;
    private String description;
    private LocalDate date;
    private BooleanProperty completed;

    public Task(int id, int userId, String description, LocalDate date, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.date = date;
        this.completed = new SimpleBooleanProperty(completed);
    }

    public Task(int userId, String description, LocalDate date, boolean completed) {
        this.userId = userId;
        this.description = description;
        this.date = date;
        this.completed = new SimpleBooleanProperty(completed);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    @Override
    public String toString() {
        return description + " (Due: " + date + ")";
    }
}
