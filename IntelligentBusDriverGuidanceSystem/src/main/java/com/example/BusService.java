package com.example;
import java.time.LocalDate;


//service file is to handle all logic that is to be tested, logic found in B1-B5
public class BusService {
    private BusRepository repository;

    public BusService(BusRepository repository) {
        this.repository = repository;
    }

    public boolean addBus(Bus bus){
        // is valid format?
        if (!isValidBusID(bus.getBusID())) {
            return false;
        }

        if (repository.getBusByID(bus.getBusID()) != null) {
            return false; // reject if busID already exists
        }

        repository.addBus(bus);
        return true;
    }

    public boolean updateBus(Bus updatedBus) {
        Bus existingBus = repository.getBusByID(updatedBus.getBusID());
        if (existingBus == null) {
            return false; // Reject if bus doesn't exist
        }
        if(updatedBus.getCapacity() > existingBus.getCapacity()) {
            return false; // bus capacity cannot be increased
        }
        repository.addBus(updatedBus); // Update bus data
        return true;
    }

    public boolean canDriverOperateBus(Driver driver, Bus bus) {
        int age = calculateAge(driver.getBirthdate());
        if (age > 50 && bus.getCapacity() >= 50) {
            return false; // drivers over 50 years old cannot operate buses with capacity of 50 or more
        }
        if (bus.getFuelType().equalsIgnoreCase("Electricity") && driver.getExperienceYears() < 5) {
            return false; // bus driver with less than 5 years of experience cannot operate electric buses
        }

        boolean isRestrictedBus = bus.getFuelType().equalsIgnoreCase("Electricity") || bus.getFuelType().equalsIgnoreCase("Hybrid");
        boolean hasValidLicense = driver.getLicenseType().equalsIgnoreCase("Heavy") || driver.getLicenseType().equalsIgnoreCase("PublicTransport");
        
        if (isRestrictedBus && !hasValidLicense) {
            return false; // only drivers with Heavy or Public Transport license can operate electric or hybrid buses
        }
        return true; //only if all checks pass
    }
    

    private boolean isValidBusID(String busID) {
        //id cant be nulkl
        if (busID == null) {
            return false;
        }
        //id must be 8 characters
        if (busID.length() != 8) {
            return false; 
        }

        return busID.matches("\\d{8}"); //every character must be a digit
    }

    //calcs age based on birthdate in format "dd-MM-yyyy"
    private int calculateAge(String birthdate) {
        String[] parts = birthdate.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate currentDate = LocalDate.now();

        int age = currentDate.getYear() - birthDate.getYear();

        //subtract one year if current date is before the birthdate in the current year
        if (currentDate.getMonthValue() < birthDate.getMonthValue() ||
            (currentDate.getMonthValue() == birthDate.getMonthValue() && currentDate.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }
        return age;
    }

}
