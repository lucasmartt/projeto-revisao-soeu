package com.amarelinha;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Menu {
    private ArrayList<Transport> transportList;
    private CityDistanceCalculator calculator;
    private Scanner scanner;
    private CurrencyFormatter currencyFormatter;

    public Menu(String csvFilePath) {
        this.transportList = new ArrayList<>();
        this.calculator = new CityDistanceCalculator(csvFilePath);
        this.scanner = new Scanner(System.in);
        this.currencyFormatter = new CurrencyFormatter("pt", "BR");
    }

    public void initiate() {
        String menu = """
                    
                    Menu:
                    1. Consultar trechos e modalidades
                    2. Cadastrar transporte
                    3. Dados estatísticos
                    4. Sair
                    """;
        while (true) {
            System.out.println(menu);
            int escolha = inputInt("Escolha uma opção: ", 1,4, scanner);

            if (escolha == 1) {
                Option1();
            } else if (escolha == 2) {
                Option2();
            } else if (escolha == 3) {
                Option3();
            } else if (escolha == 4) {
                scanner.close();
                break;
            }
        }
    }

    private void Option1() {
        String[] cities = Option2Question1(calculator, scanner, false);
        double truckCost = Option1Question2();

        int distance = calculator.getDistance(cities[0], cities[1]);
        double cost = distance * truckCost;

        String print = String.format("""
                A distância é de %d km, e o custo será de %s.""", distance, currencyFormatter.format(cost));
        System.out.print(print);
    }
    private void Option2() {
        String[] cities = Option2Question1(calculator, scanner, true);
        Item[] packages = Option2Question2();
        int[][] unloadMap = Option2Question3(cities, packages);

        Transport transport = new Transport(cities, packages, unloadMap, calculator, currencyFormatter);
        transportList.add(transport);
    }
    private void Option3() {
        double totalCost = 0;
        double costPerKm = 0;

        int totalItems = 0;
        int totalTrucks = 0;

        int totalBigTrucks = 0;
        double totalBigTrucksCost = 0;

        int totalMediumTrucks = 0;
        double totalMediumTrucksCost = 0;

        int totalSmallTrucks = 0;
        double totalSmallTrucksCost = 0;

        StringBuilder log = new StringBuilder("""
                        
                        -==- DADOS ESTATÍSTICOS -==-
                        Custo total: %s
                        Custo por Km: %s
                        
                        Total items transportados: %d
                        Total caminhões deslocados: %d
                        
                        Número de caminhões grandes: %d
                        Custo de caminhões grandes: %s
                                    
                        Número de caminhões médios: %d
                        Custo de caminhões médios: %s
                                    
                        Número de caminhões pequenos: %d
                        Custo de caminhões pequenos: %s
                        
                        ---====- TRANSPORTES -====---
                        """);
        for (int i = 0; i < transportList.size(); i++) {
            Transport transport = transportList.get(i);
            totalCost += transport.getTransportCost();
            costPerKm += transport.getCostPerKm();

            totalItems += transport.getNumberOfItems();
            totalTrucks += transport.getNumberOfTrucks();

            totalBigTrucks += transport.getBigTruckCount();
            totalBigTrucksCost += transport.getBigTruckCostCount();

            totalMediumTrucks += transport.getMediumTruckCount();
            totalMediumTrucksCost += transport.getMediumTruckCostCount();

            totalSmallTrucks += transport.getSmallTruckCount();
            totalSmallTrucksCost += transport.getSmallTruckCostCount();

            String transportLog = String.format("""
                            Transporte: %d
                            %s
                            """, i+1, transport.toString());
            log.append(transportLog);
        }
        System.out.println(String.format(log.toString()
                , currencyFormatter.format(totalCost), currencyFormatter.format(costPerKm), totalItems, totalTrucks, totalBigTrucks,
                currencyFormatter.format(totalBigTrucksCost), totalMediumTrucks, currencyFormatter.format(totalMediumTrucksCost),
                totalSmallTrucks, currencyFormatter.format(totalSmallTrucksCost)));

    }

    private double Option1Question2() {
        double truckCost = 0;
        String truckMenu = """
            1. Caminhão grande
            2. Caminhão médio
            3. Caminhão pequeno
            """;
        System.out.println(truckMenu);
        int escolha = inputInt("Insira o número correspondente ao tipo de caminhão desejado: ",1,3, scanner);

        if (escolha == 1) {
            Truck bigTruck = Truck.createBigTruck();
            truckCost = bigTruck.getCostPerKm();
        } else if (escolha == 2) {
            Truck mediumTruck = Truck.createMediumTruck();
            truckCost = mediumTruck.getCostPerKm();
        } else if (escolha == 3) {
            Truck smallTruck = Truck.createSmallTruck();
            truckCost = smallTruck.getCostPerKm();
        }
        return truckCost;
    }

    private String[] Option2Question1(CityDistanceCalculator calculator, Scanner scanner, boolean isOption2) {
        String[] cities =  calculator.getCities();
        int escolha;

        if (isOption2) {
            escolha = inputInt("Quantas cidades farão parte do percurso: ", 2, cities.length, scanner);
        } else { escolha = 2; }

        String[] selectedCities = new String[escolha];

        StringBuilder cityMenuSelect = new StringBuilder();
        for (int i = 0; i < cities.length / 2; i++) {
            String city1 = cities[i];
            String city2 = "";

            try {
                city2 = cities[i + cities.length / 2];
            } catch (ArrayIndexOutOfBoundsException ignored) {}

            String[] inbetweenSpaceArray = new String[20 - cities[i].length() - Integer.toString(i + 1).length() - 1];
            Arrays.fill(inbetweenSpaceArray, " ");
            String inbetweenSpace = String.join("", inbetweenSpaceArray);

            String printCityLine = String.format("""
            %d. %s%s%d. %s
            """,i + 1, city1, inbetweenSpace, i + 1 + cities.length / 2, city2);
            cityMenuSelect.append(printCityLine);
        }
        System.out.println(cityMenuSelect);

        System.out.println("Insira o número correspondente a cidade desejada.");
        for (int i = 0; i < selectedCities.length ; i++) {
            if (i == 0) {
                escolha = inputInt("Cidade de partida: ", 1, cities.length + 1, scanner);
            } else if (i == selectedCities.length - 1){
                escolha = inputInt("Cidade final: ", 1, cities.length + 1, scanner);
            } else {
                escolha = inputInt("Próxima cidade do trajeto: ", 1, cities.length + 1, scanner);
            }
            selectedCities[i] = cities[escolha - 1];
        }
        return selectedCities;
    }
    private Item[] Option2Question2() {
        int escolha = inputInt("Quantos tipos de items serão transportados: ", 1, 1000, scanner);

        Item[] selectedPackages = new Item[escolha];

        for (int i = 0; i < selectedPackages.length; i++) {
            System.out.print("\nInsira o nome do item: ");
            String name = scanner.nextLine();

            double weight = inputDouble("Insira o peso unitário do item (em Kg): ", 0, 10000, scanner);

            int quantity = inputInt("Insira a quantidade de itens: ", 1, 10000, scanner);

            selectedPackages[i] = new Item(name, weight, quantity);
        }
        return selectedPackages;
    }
    private int[][] Option2Question3(String[] cities, Item[] packages) {
        int[] itemsQuantity = new int[packages.length];
        for (int i = 0; i < packages.length; i++) {
            itemsQuantity[i] = packages[i].getQuantity();
        }

        int[][] unload = new int[cities.length][packages.length];
        for (int i = 0; i < cities.length; i++) {
            boolean first = i == 0;
            boolean last = i + 1 == cities.length;

            int[] unloadedCargo = new int[packages.length];
            if (first) {
                for (int item : unloadedCargo) {
                    item = 0;
                }
                unload[i] = unloadedCargo;
                continue;
            } else if (last) {
                for (int j = 0; j < unloadedCargo.length; j++) {
                    unloadedCargo[j] = itemsQuantity[j];
                    itemsQuantity[j] = 0;
                }
                unload[i] = unloadedCargo;
                break;
            }

            System.out.println("\nInsira o número de itens a serem descarregados na cidade: " + cities[i]);

            for (int j = 0; j < unloadedCargo.length; j++) {
                int maxValue = itemsQuantity[j];
                int unloadValue = 0;
                if (maxValue > 0) {
                    unloadValue = inputInt(packages[j].getName() + " (limite: " + maxValue + "): ", 0, maxValue, scanner);
                    itemsQuantity[j] -= unloadValue;
                }
                unloadedCargo[j] = unloadValue;
            }
            unload[i] = unloadedCargo;
        }
        return unload;
    }

    private double inputDouble(String message, int minValue, int maxValue, Scanner scanner) {
        double result;
        while (true) {
            System.out.print(message);
            String escolha = scanner.nextLine();
            try {
                result = Double.parseDouble(escolha);
            } catch ( Exception e) { continue; }
            if (result == -1) {
                System.exit(0);
            } else if (result >= minValue && result <= maxValue) {
                return result;
            }
        }
    }

    private int inputInt(String message, int minValue, int maxValue, Scanner scanner) {
        int result;
        while (true) {
            System.out.print(message);
            String escolha = scanner.nextLine();
            try {
                double resultDouble = Double.parseDouble(escolha);
                result = (int) resultDouble;
            } catch ( Exception e) { continue; }
            if (result == -1) {
                System.exit(0);
            } else if (result >= minValue && result <= maxValue) {
                return result;
            }
        }
    }
}
