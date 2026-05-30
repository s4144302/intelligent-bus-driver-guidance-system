package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DriverServiceTest {

    @BeforeEach
    public void clearTestFile() throws IOException {
        FileWriter writer = new FileWriter("data/test-drivers.txt", false);
        writer.write("");
        writer.close();
    }

    @Test
    public void testValidDriverIsAdded() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("23@@ab12AB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        assertTrue(service.addDriver(driver));
    }

    @Test
    public void testRejectDriverIDShorterThan10Characters() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("23@@AB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        assertFalse(service.addDriver(driver));
    }

    @Test
    public void testRejectDriverIDThatDoesNotStartWithDigitsTwoToNine() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("10@@ab12AB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        assertFalse(service.addDriver(driver));
    }

    @Test
    public void testRejectDriverIDWithLessThanTwoSpecialCharacters() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("23abCD12AB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        assertFalse(service.addDriver(driver));
    }

    @Test
    public void testRejectDuplicateDriverID() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver1 = new Driver("23@@ab12AB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        Driver driver2 = new Driver("23@@ab12AB", "John Smith", 3, "Medium",
                "15|Queen Street|Melbourne|VIC|Australia", "11-06-2000");

        assertTrue(service.addDriver(driver1));
        assertFalse(service.addDriver(driver2));
    }

    @Test
    public void testValidAddressIsAccepted() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("24@@ab12CD", "Sara Lee", 4, "Medium",
                "50|Collins Street|Melbourne|VIC|Australia", "12-08-1998");

        assertTrue(service.addDriver(driver));
    }

    @Test
    public void testRejectAddressMissingCountry() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("25@@ab12EF", "Tom Brown", 4, "Medium",
                "50|Collins Street|Melbourne|VIC", "12-08-1998");

        assertFalse(service.addDriver(driver));
    }

    @Test
    public void testRejectAddressWithTooManyParts() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("26@@ab12GH", "Emma White", 4, "Medium",
                "50|Collins Street|Melbourne|VIC|Australia|Extra", "12-08-1998");

        assertFalse(service.addDriver(driver));
    }

    @Test
    public void testValidBirthdateIsAccepted() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("27@@ab12IJ", "Mark Green", 4, "Medium",
                "20|King Street|Melbourne|VIC|Australia", "01-01-2000");

        assertTrue(service.addDriver(driver));
    }

    @Test
    public void testRejectBirthdateWithWrongSeparator() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("28@@ab12KL", "Ben Black", 4, "Medium",
                "20|King Street|Melbourne|VIC|Australia", "01/01/2000");

        assertFalse(service.addDriver(driver));
    }

    @Test
    public void testRejectBirthdateWrongOrder() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("29@@ab12MN", "Nina Blue", 4, "Medium",
                "20|King Street|Melbourne|VIC|Australia", "2000-01-01");

        assertFalse(service.addDriver(driver));
    }

    @Test
    public void testRejectLicenseChangeWhenExperienceMoreThan10Years() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("32@@ab12OP", "Adam Fox", 11, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1980");

        Driver updatedDriver = new Driver("32@@ab12OP", "Adam Fox", 11, "Medium",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1980");

        service.addDriver(oldDriver);

        assertFalse(service.updateDriver(updatedDriver));
    }

    @Test
    public void testAllowLicenseChangeWhenExperienceIs10YearsOrLess() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("33@@ab12QR", "Chris Hall", 10, "Medium",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1985");

        Driver updatedDriver = new Driver("33@@ab12QR", "Chris Hall", 10, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1985");

        service.addDriver(oldDriver);

        assertTrue(service.updateDriver(updatedDriver));
    }

    @Test
    public void testAllowUpdateWhenExperiencedDriverKeepsSameLicense() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("34@@ab12ST", "David King", 15, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1975");

        Driver updatedDriver = new Driver("34@@ab12ST", "David King", 15, "Heavy",
                "99|New Street|Melbourne|VIC|Australia", "10-10-1975");

        service.addDriver(oldDriver);

        assertTrue(service.updateDriver(updatedDriver));
    }

    @Test
    public void testRejectUpdateWhenNameIsChanged() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("35@@ab12UV", "Original Name", 5, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1990");

        Driver updatedDriver = new Driver("35@@ab12UV", "Changed Name", 5, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1990");

        service.addDriver(oldDriver);

        assertFalse(service.updateDriver(updatedDriver));
    }

    @Test
    public void testRejectUpdateWhenDriverIDIsChanged() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("36@@ab12WX", "Peter Snow", 5, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1990");

        Driver updatedDriver = new Driver("37@@ab12YZ", "Peter Snow", 5, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1990");

        service.addDriver(oldDriver);

        assertFalse(service.updateDriver(updatedDriver));
    }
    @Test
public void testAllowUpdateWhenDriverIDAndNameStaySame() {
    DriverRepository repo = new DriverRepository("data/test-drivers.txt");
    DriverService service = new DriverService(repo);

    Driver oldDriver = new Driver("38@@ab12ZA", "Same Person", 5, "Medium",
            "10|Main Street|Melbourne|VIC|Australia", "10-10-1990");

    Driver updatedDriver = new Driver("38@@ab12ZA", "Same Person", 6, "Heavy",
            "20|New Street|Melbourne|VIC|Australia", "10-10-1990");

    service.addDriver(oldDriver);

    assertTrue(service.updateDriver(updatedDriver));
}
}