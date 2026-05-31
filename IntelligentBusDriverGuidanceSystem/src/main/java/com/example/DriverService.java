package com.example;

public class DriverService {

    // Repository object used to save, read, and update driver data.
    private DriverRepository repository;

    // Constructor receives the repository object.
    public DriverService(DriverRepository repository) {
        this.repository = repository;
    }

    // Adds a driver only if all validation rules pass.
    public boolean addDriver(Driver driver) {

        // Check driver ID format.
        if (!isValidDriverID(driver.getDriverID())) {
            return false;
        }

        // Reject duplicate driver ID.
        if (repository.getDriverByID(driver.getDriverID()) != null) {
            return false;
        }

        // Check address format.
        if (!isValidAddress(driver.getAddress())) {
            return false;
        }

        // Check birthdate format.
        if (!isValidBirthdate(driver.getBirthdate())) {
            return false;
        }

        // Save driver after all checks pass.
        repository.addDriver(driver);
        return true;
    }

    // D1: Validates driver ID format.
    private boolean isValidDriverID(String driverID) {

     // D1: Driver ID must exist and be exactly 10 characters.
        if (driverID == null || driverID.length() != 10) {
            return false;
        }

        // D1: First two characters must be digits from 2 to 9.
        if (!driverID.substring(0, 2).matches("[2-9][2-9]")) {
            return false;
        }

        // D1:Middle six characters must contain at least two special characters.
        String middle = driverID.substring(2, 8);
        int specialCount = 0;

        for (int i = 0; i < middle.length(); i++) {
            char ch = middle.charAt(i);

            // Count characters that are not letters or digits.
            if (!Character.isLetterOrDigit(ch)) {
                specialCount++;
            }
        }

        // D1:Reject if fewer than two special characters exist.
        if (specialCount < 2) {
            return false;
        }

        // D1:Last two characters must be uppercase letters.
        if (!driverID.substring(8, 10).matches("[A-Z][A-Z]")) {
            return false;
        }

        return true;
    }

    // D2:Validates address format.
    private boolean isValidAddress(String address) {

        //Address must not be null.
        if (address == null) {
            return false;
        }

        // D2:Address split into five parts using |.
        String[] parts = address.split("\\|");

        return parts.length == 5;
    }

    // D3: Validates birthdate format.
    private boolean isValidBirthdate(String birthdate) {

        // Birthdate must not be null.
        if (birthdate == null) {
            return false;
        }

        // D3:Birthdate must follow dd-mm-yyyy format.
        return birthdate.matches("\\d{2}-\\d{2}-\\d{4}");
    }

    // Updates a driver only if all update rules pass.
    public boolean updateDriver(Driver updatedDriver) {

        // Find the existing driver using the updated driver's ID.
        Driver oldDriver = repository.getDriverByID(updatedDriver.getDriverID());

        // Reject update if the driver does not exist.
        if (oldDriver == null) {
            return false;
        }

        // D5: Driver name cannot be changed.
        if (!oldDriver.getName().equals(updatedDriver.getName())) {
            return false;
        }

        // D4: Drivers with more than 10 years experience cannot change licence type.
        if (oldDriver.getExperienceYears() > 10 &&
                !oldDriver.getLicenseType().equals(updatedDriver.getLicenseType())) {
            return false;
        }

        // Updated address must still be valid.
        if (!isValidAddress(updatedDriver.getAddress())) {
            return false;
        }

        // Updated birthdate must still be valid.
        if (!isValidBirthdate(updatedDriver.getBirthdate())) {
            return false;
        }

        // Save updated driver after all checks pass.
        repository.updateDriver(updatedDriver);
        return true;
    }
}