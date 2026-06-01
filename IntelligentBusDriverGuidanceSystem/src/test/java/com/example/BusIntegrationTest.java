package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*; 

public class BusIntegrationTest {
    private BusRepository busRepo;
    private BusService busService;

    @BeforeEach
    public void setup() throws IOException {
        // Clear the test file before each test to ensure a clean slate
        FileWriter writer = new FileWriter("data/test-buses.txt", false);
        writer.write("");
        writer.close();

        busRepo = new BusRepository("data/test-buses.txt");
        busService = new BusService(busRepo);
    }

    //test 1: valid bus is correclty stored and retrieved form txt file
    @Test
    public void testAddAndRetrieveBus() {
        Bus bus = new Bus("12345678", 50, 100, "Diesel");

        boolean result = busService.addBus(bus);
        Bus savedBus = busRepo.getBusByID("12345678");
        assertTrue(result);
        assertNotNull(savedBus);
        assertEquals("12345678", savedBus.getBusID());
        assertEquals(50, savedBus.getCapacity());
        assertEquals(100, savedBus.getFuelLevel());
        assertEquals("Diesel", savedBus.getFuelType());
    }

    //Test 2: invalid bus must be rejected and not written to txt file
    @Test
    public void testAddInvalidBus() {
        Bus bus = new Bus("12AB34", 50, 100, "Diesel"); // invalid ID

        boolean result = busService.addBus(bus);
        Bus savedBus = busRepo.getBusByID("12AB34");

        assertFalse(result);
        assertNull(savedBus); // should not be saved to file
    }

    //test 3: Updates bus details must be correctly written to txt file
    @Test
    public void testUpdateBus() {
        Bus OldBus = new Bus("12345678", 50, 100, "Diesel");
        Bus UpdatedBus = new Bus("12345678", 50, 80, "Electricity");

        busService.addBus(OldBus);
        boolean result = busService.updateBus(UpdatedBus);

        Bus savedBus = busRepo.getBusByID("12345678");

        assertTrue(result);
        assertEquals(80, savedBus.getFuelLevel());
        assertEquals("Electricity", savedBus.getFuelType());
    }

    //Test 4: bus count in the txt must be updated correctly after adding buses
    @Test
    public void testBusCountInFile() {
        Bus bus1 = new Bus("12345678", 50, 100, "Diesel");
        Bus bus2 = new Bus("87654321", 40, 80, "Electricity");

        busService.addBus(bus1);
        busService.addBus(bus2);

        assertEquals(2, busRepo.getAllBuses().size());
    }
}
