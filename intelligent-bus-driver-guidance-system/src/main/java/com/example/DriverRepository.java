package com.example;

import java.util.ArrayList;
import java.util.List;

public class DriverRepository {

    private List<Driver> drivers;

    public DriverRepository() {
        drivers = new ArrayList<>();
    }

    public boolean addDriver(Driver driver) {
        drivers.add(driver);
        return true;
    }

    public int countDrivers() {
        return drivers.size();
    }

    public Driver getDriverById(String driverID) {
        for (Driver driver : drivers) {
            if (driver.getDriverID().equals(driverID)) {
                return driver;
            }
        }

        return null;
    }

    public List<Driver> getAllDrivers() {
        return drivers;
    }
}