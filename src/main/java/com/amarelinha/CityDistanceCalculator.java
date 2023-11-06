package com.amarelinha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CityDistanceCalculator {
    private String[] cities;
    private Map<String, Map<String, Integer>> distanceData;

    public CityDistanceCalculator(String csvFilePath) {
        this.distanceData = parseCSV(csvFilePath);
    }

    private Map<String, Map<String, Integer>> parseCSV(String csvFilePath) {
        Map<String, Map<String, Integer>> cityDistances = new HashMap<>();

        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(csvFilePath);

        try {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                String[] cities = null;
                int row = 0;

                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(";");

                    if (row == 0) {
                        cities = values;
                        this.cities = cities;
                    } else {
                        String fromCity = cities[row - 1];
                        Map<String, Integer> distances = new HashMap<>();

                        for (int col = 1; col < values.length; col++) {
                            String toCity = cities[col - 1];
                            int distance = Integer.parseInt(values[col - 1]);
                            distances.put(toCity, distance);
                        }

                        cityDistances.put(fromCity, distances);
                    }

                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityDistances;
    }
    public int getDistance(String departureCity, String destinationCity) {
        Map<String, Integer> distances = distanceData.get(departureCity);
        if (distances != null) {
            Integer distance = distances.get(destinationCity);
            if (distance != null) {
                return distance;
            }
        }
        return -1;
    }
    public String[] getCities() {
        return cities;
    }
}
