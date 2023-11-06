package com.amarelinha;

public class Item {
    private String name;
    private double weight;
    private int quantity;
    private double totalWeight;

    public Item(String name, double weight, int quantity) {
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
        this.totalWeight = calculateTotalWeight();
    }

    public void unload(int unloadQuantity) {
        this.quantity -= unloadQuantity;
        this.totalWeight = calculateTotalWeight();
    }

    private double calculateTotalWeight() {
        return this.weight * this.quantity / 1000;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalWeight() {
        return totalWeight;
    }
}
