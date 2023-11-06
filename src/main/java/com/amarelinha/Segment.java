package com.amarelinha;

import java.util.*;

public class Segment {
    private String departureCity;
    private String destinationCity;
    private Truck[] trucks;
    private CityDistanceCalculator calculator;
    private CurrencyFormatter currencyFormatter;
    private int distance;
    private Map<Truck, Double> truckSegmentCostMap;
    private double segmentCost;
    private double costPerKm;
    private Item[] packages;
    private Item[] unloadedPackages;

    public Segment(String departureCity, String destinationCity, Truck[] trucks, Item[] packages, int[] unloadMap, CityDistanceCalculator calculator, CurrencyFormatter currencyFormatter) {
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.trucks = trucks;
        this.calculator = calculator;
        this.currencyFormatter = currencyFormatter;
        this.distance = GetCityDistance(this.calculator, this.departureCity, this.destinationCity);
        this.truckSegmentCostMap = CreateTruckSegmentCostMap(this.trucks);
        this.segmentCost = CalculateSegmentCost(this.trucks, this.distance, this.truckSegmentCostMap);
        this.costPerKm = this.segmentCost / this.distance;
        this.packages = packages;
        this.unloadedPackages = UnloadPackages(this.packages, unloadMap);
        RearrangePackages(this.packages, this.trucks);
    }

    private int GetCityDistance(CityDistanceCalculator calculator, String departureCity, String destinationCity) {
        return calculator.getDistance(departureCity, destinationCity);
    }

    private Map<Truck, Double> CreateTruckSegmentCostMap(Truck[] trucks) {
        Map<Truck, Double> truckSegmentCostMap = new HashMap<>();
        for (Truck truck : trucks) {
            truckSegmentCostMap.put(truck, (double) 0);
        }
        return truckSegmentCostMap;
    }

    private double CalculateSegmentCost(Truck[] trucks, int distance, Map<Truck, Double> truckSegmentCostMap) {
        double segmentCost = 0;
        for (Truck truck : trucks) {
            if (truck.isActive()) {
                double costPerSegment = distance * truck.getCostPerKm();
                segmentCost += costPerSegment;
                truckSegmentCostMap.put(truck, costPerSegment);
                truck.addTotalCost(segmentCost);

            }
        }
        return segmentCost;
    }

    private Item[] UnloadPackages(Item[] packages, int[] unloadMap) {
        List<Item> unloadedPackages = new ArrayList<>();
        for (int i = 0; i < packages.length; i++) {
            if (unloadMap[i] > 0) {
                // packages[i].unload(unloadMap[i]);
                Item unloadedPackage = new Item(packages[i].getName(), packages[i].getWeight(), unloadMap[i]);
                unloadedPackages.add(unloadedPackage);
            }
        }
        return unloadedPackages.toArray(new Item[unloadedPackages.size()]);
    }

    private void RearrangePackages(Item[] packages, Truck[] trucks) {
        double packagesTotalWeight = 0;
        for (Item item : packages) {
            packagesTotalWeight += item.getTotalWeight();
        }

        Arrays.sort(trucks, new Comparator<Truck>() {
            @Override
            public int compare(Truck truck1, Truck truck2) {
                return Double.compare(truck2.getCapacity(), truck1.getCapacity());
            }
        });

        for (Truck truck : trucks) {
            if (packagesTotalWeight > 0) {
                packagesTotalWeight -= truck.getCapacity();
            } else {
                truck.deactivate();
            }
        }
    }

    @Override
    public String toString() {
        //        for (Item item : this.unloadedPackages) {
//            String unloadedPackagesLog = String.format("""
//                %s: """
//                        ,this.departureCity, this.destinationCity, this.distance, this.segmentCost, this.costPerKm);
//        }
        return String.format("""
            Trecho %s para %s:
            Distância: %d km
            Custo total: %s
            Custo por Km: %s
            ---=====---=======---=====---
            """
                    , departureCity, destinationCity, distance, currencyFormatter.format(segmentCost), currencyFormatter.format(costPerKm));
    }

    public int getDistance() {
        return distance;
    }
    public double getSegmentCost() {
        return segmentCost;
    }
}
