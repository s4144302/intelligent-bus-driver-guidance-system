package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DriverIntegrationTest {

    private DriverRepository repo;
    private DriverService service;

    // Clear the integration test file and set up fresh repo and service before each test.
    @BeforeEach
    public void setUp() throws IOException {
        FileWriter writer = new FileWriter("data/integration-drivers.txt", false);
        writer.write("");
        writer.close();

        repo = new DriverRepository("data/integration-drivers.txt");
        service = new DriverService(repo);
    }

    // Integration Test 1: Valid driver is correctly stored and retrieved from the TXT file.
    @Test
    public void testValidDriverIsStoredAndRetrievedCorrectly() {
        Driver driver = new Driver("23@@ab12AB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        boolean result = service.addDriver(driver);
        Driver savedDriver = repo.getDriverByID("23@@ab12AB");

        assertTrue(result);
        assertNotNull(savedDriver);
        assertEquals("Ali Khan", savedDriver.getName());
        assertEquals("Heavy", savedDriver.getLicenseType());
    }

    // Integration Test 2: Invalid driver is rejected by the service and not written to the TXT file.
    @Test
    public void testInvalidDriverIsRejectedAndNotStored() {
        Driver driver = new Driver("10@@ab12AB", "Bad Driver", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        boolean result = service.addDriver(driver);
        Driver savedDriver = repo.getDriverByID("10@@ab12AB");

        assertFalse(result);
        assertNull(savedDriver);
    }

    // Integration Test 3: Updated driver details are correctly persisted to the TXT file.
    @Test
    public void testDriverUpdateIsPersistedCorrectly() {
        Driver oldDriver = new Driver("24@@ab12CD", "Sara Lee", 5, "Medium",
                "50|Collins Street|Melbourne|VIC|Australia", "12-08-1998");

        Driver updatedDriver = new Driver("24@@ab12CD", "Sara Lee", 6, "Heavy",
                "99|New Street|Melbourne|VIC|Australia", "12-08-1998");

        service.addDriver(oldDriver);
        boolean result = service.updateDriver(updatedDriver);

        Driver savedDriver = repo.getDriverByID("24@@ab12CD");

        assertTrue(result);
        assertEquals(6, savedDriver.getExperienceYears());
        assertEquals("Heavy", savedDriver.getLicenseType());
        assertEquals("99|New Street|Melbourne|VIC|Australia", savedDriver.getAddress());
    }

    // Integration Test 4: Driver count in the TXT file is updated correctly after adding multiple drivers.
    @Test
    public void testDriverCountIsUpdatedCorrectly() {
        Driver driver1 = new Driver("25@@ab12EF", "Tom Brown", 3, "Medium",
                "10|Main Street|Melbourne|VIC|Australia", "01-01-2000");

        Driver driver2 = new Driver("26@@ab12GH", "Emma White", 4, "Heavy",
                "20|High Street|Melbourne|VIC|Australia", "02-02-2001");

        service.addDriver(driver1);
        service.addDriver(driver2);

        assertEquals(2, repo.countDrivers());
    }
}