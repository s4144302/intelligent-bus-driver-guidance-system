package com.example;
import java.util.*;
import java.io.*;


public class BusRepository {

    private String filePath;

    public BusRepository(String filePath) {
        this.filePath = filePath;
    }

    // Adds a new bus record to the text file.
    public void addBus(Bus bus) {
        try {
            FileWriter writer = new FileWriter(filePath, true);
            writer.write(bus.getBusID() + "," +
                    bus.getCapacity() + "," +
                    bus.getFuelLevel() + "," +
                    bus.getFuelType() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving bus.");
        }
    }

    public ArrayList<Bus> getAllBuses() {
        ArrayList<Bus> buses = new ArrayList<>();

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return buses; // Return empty list if file doesn't exist
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                Bus bus = new Bus(
                        data[0],
                        Integer.parseInt(data[1]),
                        Double.parseDouble(data[2]),
                        data[3]
                );
                buses.add(bus);
            }
            scanner.close();

        } catch (IOException e) {
            System.out.println("Error reading bus data.");
        }
        return buses;
    }

    public Bus getBusByID(String busID) {
        for (Bus bus : getAllBuses()) {
            if (bus.getBusID().equals(busID)) {
                return bus;
            }
        }
        return null; // Return null if bus not found
    }


    public void updateBus(Bus updatedBus) {
        ArrayList<Bus> buses = getAllBuses();
        try {
            FileWriter writer = new FileWriter(filePath, false); // Overwrite file to add
            for (Bus b : buses) {
                if (b.getBusID().equals(updatedBus.getBusID())) {
                    writer.write(updatedBus.getBusID() + "," +
                            updatedBus.getCapacity() + "," +
                            updatedBus.getFuelLevel() + "," +
                            updatedBus.getFuelType() + "\n");
                } else { //else write original data back
                    writer.write(b.getBusID() + "," +
                            b.getCapacity() + "," +
                            b.getFuelLevel() + "," +
                            b.getFuelType() + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error updating bus.");
        }
    }

    public int countBuses() {
        return getAllBuses().size();
    }
}
