package com.amarelinha;

import java.util.Objects;

public class Transport {
    private String[] cities;
    private Item[] packages;
    private int[][] unloadMap;
    private CityDistanceCalculator calculator;
    private CurrencyFormatter currencyFormatter;
    private Truck[] trucks;
    private Segment[] segments;
    private double transportCost;
    private int transportDistance;
    private double costPerKm;
    private double bigTruckCostCount;
    private double mediumTruckCostCount;
    private double smallTruckCostCount;
    private int bigTruckCount;
    private int mediumTruckCount;
    private int smallTruckCount;

    public Transport(String[] cities, Item[] packages, int[][] unloadMap, CityDistanceCalculator calculator, CurrencyFormatter currencyFormatter) {
        this.cities = cities;
        this.packages = packages;
        this.unloadMap = unloadMap;
        this.calculator = calculator;
        this.currencyFormatter = currencyFormatter;
        this.trucks = AssignTrucks(this.packages);
        this.segments = CreateSegments(this.cities, this.trucks, this.packages, this.unloadMap, this.calculator);
        this.transportCost = CalculateTransportCost(this.segments);
        this.transportDistance = CalculateTransportDistance(this.segments);
        this.costPerKm = this.transportCost / this.transportDistance;
        UpdateTruckCounts();
    }

    private Truck[] AssignTrucks(Item[] packages) {
        double totalCargoWeight = 0;
        for (Item item : packages) {
            totalCargoWeight += item.getTotalWeight();
        }

        totalCargoWeight = Math.ceil(totalCargoWeight);

        int bigTruckCount = 0;
        int mediumTruckCount = 0;
        int smallTruckCount = 0;

        while (totalCargoWeight >= 10) {
            totalCargoWeight -= 10;
            bigTruckCount++;
        }

        while (totalCargoWeight >= 4) {
            totalCargoWeight -= 4;
            mediumTruckCount++;
        }

        while (totalCargoWeight >= 1) {
            totalCargoWeight -= 1;
            smallTruckCount++;
        }

        Truck[] trucks = new Truck[bigTruckCount + mediumTruckCount + smallTruckCount];

        for (int i = 0; i < trucks.length;) {
            for (int j = 0; j < bigTruckCount; j++) {
                Truck bigTruck = Truck.createBigTruck();
                trucks[i] = bigTruck;
                i++;
            }

            for (int j = 0; j < mediumTruckCount; j++) {
                Truck mediumTruck = Truck.createMediumTruck();
                trucks[i] = mediumTruck;
                i++;
            }

            for (int j = 0; j < smallTruckCount; j++) {
                Truck smallTruck = Truck.createSmallTruck();
                trucks[i] = smallTruck;
                i++;
            }
        }

        return trucks;
    }

    private Segment[] CreateSegments(String[] cities, Truck[] trucks, Item[] packages, int[][] unloadMap, CityDistanceCalculator calculator) {
        Segment[] segments = new Segment[cities.length - 1];
        for (int i = 0; i < cities.length - 1; i++) {
            String departureCity = cities[i];
            String destinationCity = cities[i + 1];
            int[] unload = unloadMap[i + 1];
            Segment segment = new Segment(departureCity, destinationCity, trucks, packages, unload, calculator, currencyFormatter);
            segments[i] = segment;
        }
        return segments;
    }

    private double CalculateTransportCost(Segment[] segments) {
        double transportCost = 0;
        for (Segment segment : segments) {
            double segmentCost = segment.getSegmentCost();
            transportCost += segmentCost;
        }
        return transportCost;
    }

    private int CalculateTransportDistance(Segment[] segments) {
        int transportDistance = 0;
        for (Segment segment : segments) {
            int segmentDistance = segment.getDistance();
            transportDistance += segmentDistance;
        }
        return transportDistance;
    }

    private void UpdateTruckCounts() {
        for (Truck truck : trucks) {
            String truckType = truck.getType();
            double truckTotalCost = truck.getTotalCost();

            if (Objects.equals(truckType, "Big Truck")) {
                bigTruckCostCount += truckTotalCost;
                bigTruckCount++;
            }
            if (Objects.equals(truckType, "Medium Truck")) {
                mediumTruckCostCount += truckTotalCost;
                mediumTruckCount++;
            }
            if (Objects.equals(truckType, "Small Truck")) {
                smallTruckCostCount += truckTotalCost;
                smallTruckCount++;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder transportLog = new StringBuilder(String.format("""
                        Custo total: %s
                        Custo por Km: %s
                                    
                        Caminhões deslocados: %d
                        Número de caminhões grandes: %d
                        Custo de caminhões grandes: %s
                                    
                        Número de caminhões médios: %d
                        Custo de caminhões médios: %s
                                    
                        Número de caminhões pequenos: %d
                        Custo de caminhões pequenos: %s
                        
                        ---=====-- TRECHOS --=====---
                        """
                , currencyFormatter.format(transportCost), currencyFormatter.format(costPerKm), trucks.length
                , bigTruckCount, currencyFormatter.format(bigTruckCostCount), mediumTruckCount
                , currencyFormatter.format(mediumTruckCostCount), smallTruckCount, currencyFormatter.format(smallTruckCostCount)));

        for (Segment segment : this.segments) {
            transportLog.append(segment.toString());
        }
        return transportLog.toString();
    }

    public double getTransportCost() {
        return transportCost;
    }
    public double getCostPerKm() {
        return costPerKm;
    }
    public int getNumberOfTrucks() {
        return trucks.length;
    }
    public int getNumberOfItems() {
        int totalItems = 0;
        for (Item item : this.packages) {
            totalItems += item.getQuantity();
        }
        return totalItems;
    }
    public double getBigTruckCostCount() {
        return bigTruckCostCount;
    }
    public double getMediumTruckCostCount() {
        return mediumTruckCostCount;
    }
    public double getSmallTruckCostCount() {
        return smallTruckCostCount;
    }
    public int getBigTruckCount() {
        return bigTruckCount;
    }
    public int getMediumTruckCount() {
        return mediumTruckCount;
    }
    public int getSmallTruckCount() {
        return smallTruckCount;
    }
}
