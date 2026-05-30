package com.example;

public class DriverService {

    private DriverRepository repository;

    public DriverService(DriverRepository repository) {
        this.repository = repository;
    }

    public boolean addDriver(Driver driver) {

        if (!isValidDriverID(driver.getDriverID())) {
            return false;
        }

        if (repository.getDriverByID(driver.getDriverID()) != null) {
            return false;
        }

        if (!isValidAddress(driver.getAddress())) {
            return false;
        }

        if (!isValidBirthdate(driver.getBirthdate())) {
            return false;
        }

        repository.addDriver(driver);
        return true;
    }

    private boolean isValidDriverID(String driverID) {
        if (driverID == null || driverID.length() != 10) {
            return false;
        }

        if (!driverID.substring(0, 2).matches("[2-9][2-9]")) {
            return false;
        }

        String middle = driverID.substring(2, 8);
        int specialCount = 0;

        for (int i = 0; i < middle.length(); i++) {
            char ch = middle.charAt(i);

            if (!Character.isLetterOrDigit(ch)) {
                specialCount++;
            }
        }

        if (specialCount < 2) {
            return false;
        }

        if (!driverID.substring(8, 10).matches("[A-Z][A-Z]")) {
            return false;
        }

        return true;
    }

    private boolean isValidAddress(String address) {
        if (address == null) {
            return false;
        }

        String[] parts = address.split("\\|");

        return parts.length == 5;
    }

    private boolean isValidBirthdate(String birthdate) {
        if (birthdate == null) {
            return false;
        }

        return birthdate.matches("\\d{2}-\\d{2}-\\d{4}");
    }
    public boolean updateDriver(Driver updatedDriver) {

    Driver oldDriver = repository.getDriverByID(updatedDriver.getDriverID());

    if (oldDriver == null) {
        return false;
    }

    // D5: name cannot be changed
    if (!oldDriver.getName().equals(updatedDriver.getName())) {
        return false;
    }

    // D4: if experience > 10, license cannot change
    if (oldDriver.getExperienceYears() > 10 &&
            !oldDriver.getLicenseType().equals(updatedDriver.getLicenseType())) {
        return false;
    }

    if (!isValidAddress(updatedDriver.getAddress())) {
        return false;
    }

    if (!isValidBirthdate(updatedDriver.getBirthdate())) {
        return false;
    }

    repository.updateDriver(updatedDriver);
    return true;
}
}