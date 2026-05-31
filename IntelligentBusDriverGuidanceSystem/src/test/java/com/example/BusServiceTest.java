package com.example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class BusServiceTest {

    //clear the test file before each test to ensure a clean slate
    @BeforeEach
    public void clearTestFile() throws IOException {
        FileWriter writer = new FileWriter("data/test-buses.txt", false);
        writer.write("");
        writer.close();
    }


    //B1: BUS ID RULES................

    //B1: normal: valid 8-digit ID should be accepted.
    @Test
    public void testValidBusIDIsAccepted() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Bus bus = new Bus("12345678", 32, 85.0, "Diesel");
        
        assertTrue(service.addBus(bus));
    }

    //B1: invalid: ID with less than 8 characters should be rejected.
    @Test
    public void testShortBusIDIsRejected() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Bus bus = new Bus("1234567", 32, 85.0, "Diesel");
        
        assertFalse(service.addBus(bus));
    }

    //B1: invalid: bus ID with non digit characters should be rejected.
    @Test
    public void testNonDigitBusIDIsRejected() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Bus bus = new Bus("ABCDEFGH", 32, 85.0, "Diesel");
        
        assertFalse(service.addBus(bus));
    }

    //B1: Edge case: duplicate bus ID should be rejected.
    @Test
    public void testDuplicateBusIDIsRejected() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Bus bus1 = new Bus("12345678", 32, 85.0, "Diesel");
        Bus bus2 = new Bus("12345678", 40, 90.0, "Electric");

        assertTrue(service.addBus(bus1));
        assertFalse(service.addBus(bus2)); // should reject duplicate ID
    }

    //B2: CAPACITY UPDATE RESTRICTIONS.............

    //B2: normal: lowerinf bus capacity should be accepted.
    @Test
    public void testLoweringBusCapacityIsAccepted() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Bus bus = new Bus("12345678", 32, 85.0, "Diesel");
        service.addBus(bus);

        Bus updatedBus = new Bus("12345678", 30, 85.0, "Diesel");
        
        assertTrue(service.updateBus(updatedBus));
    }

    //B2: edge case: having capacioty stay same during update should be accepted.
    @Test
    public void testSameBusCapacityIsAccepted() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Bus bus = new Bus("12345678", 32, 85.0, "Diesel");
        service.addBus(bus);

        Bus updatedBus = new Bus("12345678", 32, 80.0, "Diesel");
        
        assertTrue(service.updateBus(updatedBus));
    }

    //b2: invalid: increasing bus capacity during an update should be rejected.
    @Test
    public void testIncreasingBusCapacityIsRejected() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Bus bus = new Bus("12345678", 32, 85.0, "Diesel");
        service.addBus(bus);

        Bus updatedBus = new Bus("12345678", 40, 90.0, "Diesel");
        
        assertFalse(service.updateBus(updatedBus));
    }

    //B3: DRIVER AGE RULES.........

    //B3: invalid: driver older than 50 should not be able to operate buses with capacity of 50 or more.
    @Test
    public void testDriverOver50CannotOperateLargeBus() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Driver driver = new Driver("23@@ab12AB", "Sheev Palpatine", 20, "Heavy",
                "123|sesemae Street|Melbourne|VIC|Australia", "10-05-1960"); // age 64
        Bus bus = new Bus("12345678", 50, 85.0, "Diesel");

        assertFalse(service.canDriverOperateBus(driver, bus));
    }

    //B3: edge case: driver exactly 50 should be allowed.
    @Test
    public void testDriverExactly50CanOperateLargeBus() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        int birthYear = LocalDate.now().getYear() - 50; // calculate birth year for age 50
        // format birthdate as dd-MM-yyyy
        String birthdate = String.format("%02d-%02d-%04d", LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(), birthYear);


        Driver driver = new Driver("23@@ab12AB", "Sheev Palpatine", 20, "Heavy",
                "123|sesamae Street|Melbourne|VIC|Australia", birthdate); // age 50
        Bus bus = new Bus("12345678", 50, 85.0, "Diesel");

        assertTrue(service.canDriverOperateBus(driver, bus));
    }

    //B3: normal: driver aged 30 should be able to operate bus with capacity of 50 or more.
    @Test
    public void testDriverUnder50CanOperateLargeBus() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Driver driver = new Driver("23@@ab12AB", "Sheev Palpatine", 20, "Heavy",
                "123|sesamae Street|Melbourne|VIC|Australia", "10-05-1993"); // age 30
        Bus bus = new Bus("12345678", 50, 85.0, "Diesel");

        assertTrue(service.canDriverOperateBus(driver, bus));
    }

    //B4: ELECTRIC BUS RESTRICTIONS.........
    
    //B4: normal: driver with exaclty 5 yrs experience should be able to drive electric bus.
    @Test
    public void testDriverWith5YearsExperienceCanDriveElectricBus() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Driver driver = new Driver("43@@nb75AS", "Anakin Skywalker", 5, "Heavy",
                "123|sesamae Street|Melbourne|VIC|Australia", "10-05-1990"); // 5 years experience
        Bus bus = new Bus("12345678", 30, 100.0, "Electric");

        assertTrue(service.canDriverOperateBus(driver, bus));
    }

    //B4: invalid: driver with less than 5 yrs experience should not be able to drive electric bus.
    @Test
    public void testDriverLessThan5YearsExperienceCannotDriveElectricBus() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Driver driver = new Driver("43@@nb75AS", "Anakin Skywalker", 4, "Heavy",
                "123|sesamae Street|Melbourne|VIC|Australia", "10-05-1990"); // 4 years experience
        Bus bus = new Bus("12345678", 30, 100.0, "Electric");

        assertFalse(service.canDriverOperateBus(driver, bus));
    }

    //b4: normal: driver with more than 5 yrs experience should be able to drive electric bus.
    @Test
    public void testDriverWithMoreThan5YearsExperienceCanDriveElectricBus() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Driver driver = new Driver("43@@nb75AS", "Anakin Skywalker", 12, "Heavy",
                "123|sesamae Street|Melbourne|VIC|Australia", "10-05-1990"); // 12 years experience
        Bus bus = new Bus("12345678", 30, 100.0, "Electric");

        assertTrue(service.canDriverOperateBus(driver, bus));
    }

    //B5: DRIVER LICENSE RESTRICTIONS.............

    //B5: normal: driver with heavy license should be able to operate hybrid bus.
    @Test
    public void testDriverWithHeavyLicenseCanOperateHybridBus() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Driver driver = new Driver("13@@hn24CB", "Luke Skywalker", 10, "Heavy",
                "123|sesamae Street|Melbourne|VIC|Australia", "10-05-1990");
        Bus bus = new Bus("12345678", 30, 100.0, "Hybrid");

        assertTrue(service.canDriverOperateBus(driver, bus));
    }

    //B5: normal: driver with public transport license should be able to operate electric bus.
    @Test
    public void testDriverWithPublicTransportLicenseCanOperateElectricBus() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Driver driver = new Driver("13@@hn24CB", "Luke Skywalker", 10, "PublicTransport",
                "123|sesamae Street|Melbourne|VIC|Australia", "10-05-1990");
        Bus bus = new Bus("12345678", 30, 100.0, "Electric");

        assertTrue(service.canDriverOperateBus(driver, bus));
    }

    //B5: invalid: driver with medium license should not be able to operate electric bus.
    @Test
    public void testDriverWithMediumLicenseCannotOperateElectricBus() {
        BusRepository repo = new BusRepository("data/test-buses.txt");
        BusService service = new BusService(repo);

        Driver driver = new Driver("13@@hn24CB", "Luke Skywalker", 10, "Medium",
                "123|sesamae Street|Melbourne|VIC|Australia", "10-05-1990");
        Bus bus = new Bus("12345678", 30, 100.0, "Electric");

        assertFalse(service.canDriverOperateBus(driver, bus));
    }
}

//et voila
