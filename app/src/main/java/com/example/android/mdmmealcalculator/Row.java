package com.example.android.mdmmealcalculator;

import java.util.Date;

public class Row {
    Date date;
    double amount, budget, cummulative;
    int students;

    Row(){}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getCummulative() {
        return cummulative;
    }

    public void setCummulative(double cummulative) {
        this.cummulative = cummulative;
    }

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }
}
