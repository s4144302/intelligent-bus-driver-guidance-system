package com.example;

import java.io.*;
import java.util.*;

public class DriverRepository {

    private String filePath;

    public DriverRepository(String filePath) {
        this.filePath = filePath;
    }

    public void addDriver(Driver driver) {
        try {
            FileWriter writer = new FileWriter(filePath, true);

            writer.write(driver.getDriverID() + "," +
                    driver.getName() + "," +
                    driver.getExperienceYears() + "," +
                    driver.getLicenseType() + "," +
                    driver.getAddress() + "," +
                    driver.getBirthdate() + "\n");

            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving driver.");
        }
    }

    public ArrayList<Driver> getAllDrivers() {
        ArrayList<Driver> drivers = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                Driver driver = new Driver(
                        data[0],
                        data[1],
                        Integer.parseInt(data[2]),
                        data[3],
                        data[4],
                        data[5]
                );

                drivers.add(driver);
            }

            scanner.close();

        } catch (Exception e) {
            System.out.println("Error reading drivers.");
        }

        return drivers;
    }

    public Driver getDriverByID(String driverID) {
        ArrayList<Driver> drivers = getAllDrivers();

        for (Driver driver : drivers) {
            if (driver.getDriverID().equals(driverID)) {
                return driver;
            }
        }

        return null;
    }

    public void updateDriver(Driver updatedDriver) {
        ArrayList<Driver> drivers = getAllDrivers();

        try {
            FileWriter writer = new FileWriter(filePath, false);

            for (Driver driver : drivers) {
                if (driver.getDriverID().equals(updatedDriver.getDriverID())) {
                    writer.write(updatedDriver.getDriverID() + "," +
                            updatedDriver.getName() + "," +
                            updatedDriver.getExperienceYears() + "," +
                            updatedDriver.getLicenseType() + "," +
                            updatedDriver.getAddress() + "," +
                            updatedDriver.getBirthdate() + "\n");
                } else {
                    writer.write(driver.getDriverID() + "," +
                            driver.getName() + "," +
                            driver.getExperienceYears() + "," +
                            driver.getLicenseType() + "," +
                            driver.getAddress() + "," +
                            driver.getBirthdate() + "\n");
                }
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error updating driver.");
        }
    }

    public int countDrivers() {
        return getAllDrivers().size();
    }
}