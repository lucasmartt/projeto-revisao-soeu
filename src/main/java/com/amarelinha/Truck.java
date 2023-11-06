package com.amarelinha;

public class Truck {
    private String type;
    private double capacity;
    private double costPerKm;
    private boolean active;
    private double totalCost;

    private Truck(String type, double capacity, double costPerKm) {
        this.type = type;
        this.capacity = capacity;
        this.costPerKm = costPerKm;
        this.active = true;
        this.totalCost = 0;
    }

    public static Truck createSmallTruck() {
        return new Truck("Small Truck", 1, 5.83);
    }

    public static Truck createMediumTruck() {
        return new Truck("Medium Truck", 4, 13.42);
    }

    public static Truck createBigTruck() {
        return new Truck("Big Truck", 10, 29.21);
    }

    public String getType() {
        return type;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getCostPerKm() {
        return costPerKm;
    }

    public boolean isActive() {
        return active;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void addTotalCost(double add) {
        this.totalCost += add;
    }

    public void deactivate() {
        this.active = false;
    }
}
