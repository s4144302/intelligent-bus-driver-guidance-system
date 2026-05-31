package com.example;

import java.io.*;
import java.util.*;

public class DriverRepository {

    // Stores the path of the text file used for driver data.
    private String filePath;

    // Constructor receives the file path.
    public DriverRepository(String filePath) {
        this.filePath = filePath;
    }

    // Adds a new driver record to the text file.
    public void addDriver(Driver driver) {
        try {
            // Open file in append mode so existing data is not deleted.
            FileWriter writer = new FileWriter(filePath, true);

            // Write driver details as one comma-separated line.
            writer.write(driver.getDriverID() + "," +
                    driver.getName() + "," +
                    driver.getExperienceYears() + "," +
                    driver.getLicenseType() + "," +
                    driver.getAddress() + "," +
                    driver.getBirthdate() + "\n");

            // Close the writer after saving.
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving driver.");
        }
    }

    // Reads all driver records from the text file.
    public ArrayList<Driver> getAllDrivers() {
        ArrayList<Driver> drivers = new ArrayList<>();

        try {
            // Create a File object using the stored file path.
            File file = new File(filePath);

            // Scanner reads the file line by line.
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Split each line into driver fields.
                String[] data = line.split(",");

                // Convert the saved text data back into a Driver object.
                Driver driver = new Driver(
                        data[0],
                        data[1],
                        Integer.parseInt(data[2]),
                        data[3],
                        data[4],
                        data[5]
                );

                // Add the Driver object to the list.
                drivers.add(driver);
            }

            // Close the scanner after reading.
            scanner.close();

        } catch (Exception e) {
            System.out.println("Error reading drivers.");
        }

        // Return the full list of drivers.
        return drivers;
    }

    // Finds and returns a driver with the matching driver ID.
    public Driver getDriverByID(String driverID) {
        ArrayList<Driver> drivers = getAllDrivers();

        // Search through each saved driver.
        for (Driver driver : drivers) {
            if (driver.getDriverID().equals(driverID)) {
                return driver;
            }
        }

        // Return null if no matching driver is found.
        return null;
    }

    // Updates an existing driver record in the text file.
    public void updateDriver(Driver updatedDriver) {
        ArrayList<Driver> drivers = getAllDrivers();

        try {
            // Open file in overwrite mode to rewrite all records.
            FileWriter writer = new FileWriter(filePath, false);

            for (Driver driver : drivers) {

                // If IDs match, write the updated driver details.
                if (driver.getDriverID().equals(updatedDriver.getDriverID())) {
                    writer.write(updatedDriver.getDriverID() + "," +
                            updatedDriver.getName() + "," +
                            updatedDriver.getExperienceYears() + "," +
                            updatedDriver.getLicenseType() + "," +
                            updatedDriver.getAddress() + "," +
                            updatedDriver.getBirthdate() + "\n");
                } else {
                    // Otherwise, write the original driver details back.
                    writer.write(driver.getDriverID() + "," +
                            driver.getName() + "," +
                            driver.getExperienceYears() + "," +
                            driver.getLicenseType() + "," +
                            driver.getAddress() + "," +
                            driver.getBirthdate() + "\n");
                }
            }

            // Close the writer after updating.
            writer.close();

        } catch (IOException e) {
            System.out.println("Error updating driver.");
        }
    }

    // Counts how many drivers are stored in the file.
    public int countDrivers() {
        return getAllDrivers().size();
    }
}