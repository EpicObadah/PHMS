package com.example.mavhealth;

import com.google.firebase.Timestamp;

public class Medication {
    String name;
    int rxNumber;
    int dosage;
    float strength;
    int hour;
    int minutes;

    public Medication() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRxNumber() {
        return rxNumber;
    }

    public void setRxNumber(int rxNumber) {
        this.rxNumber = rxNumber;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
