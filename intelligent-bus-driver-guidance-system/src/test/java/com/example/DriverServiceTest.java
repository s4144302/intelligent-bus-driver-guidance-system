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

    // D1 Test 1: Normal case - valid driver ID is accepted.
    @Test
    public void testValidDriverIDIsAccepted() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("23@@ab12AB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        assertTrue(service.addDriver(driver));
    }

    // D1 Test 2: Invalid input - driver ID shorter than 10 characters is rejected.
    @Test
    public void testRejectDriverIDShorterThan10Characters() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("23@@ab12A", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        assertFalse(service.addDriver(driver));
    }

    // D1 Test 3: Invalid input - driver ID first two characters outside range 2 to 9 is rejected.
    @Test
    public void testRejectDriverIDFirstTwoCharsOutsideRange() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("10@@ab12AB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        assertFalse(service.addDriver(driver));
    }

    // D1 Test 4: Invalid input - driver ID with fewer than two special characters in middle is rejected.
    @Test
    public void testRejectDriverIDWithFewerThanTwoSpecialChars() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("23abcde1AB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        assertFalse(service.addDriver(driver));
    }

    // D1 Test 5: Edge case - driver ID with exactly two special characters in middle is accepted.
    @Test
    public void testAcceptDriverIDWithExactlyTwoSpecialChars() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("23@!abcdAB", "Ali Khan", 5, "Heavy",
                "12|King Street|Melbourne|VIC|Australia", "10-05-1999");

        assertTrue(service.addDriver(driver));
    }

    // D2 Test 1: Normal case - valid address with five pipe-separated parts is accepted.
    @Test
    public void testValidAddressIsAccepted() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("24@@ab12CD", "Sara Lee", 4, "Medium",
                "50|Collins Street|Melbourne|VIC|Australia", "12-08-1998");

        assertTrue(service.addDriver(driver));
    }

    // D2 Test 2: Invalid input - address with fewer than five parts is rejected.
    @Test
    public void testRejectAddressWithFewerThanFiveParts() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("25@@ab12EF", "Tom Brown", 4, "Medium",
                "50|Collins Street|Melbourne|VIC", "12-08-1998");

        assertFalse(service.addDriver(driver));
    }

    // D2 Test 3: Invalid input - address with more than five parts is rejected.
    @Test
    public void testRejectAddressWithMoreThanFiveParts() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("26@@ab12GH", "Emma White", 4, "Medium",
                "50|Collins Street|Melbourne|VIC|Australia|Extra", "12-08-1998");

        assertFalse(service.addDriver(driver));
    }

    // D2 Test 4: Edge case - address using commas instead of pipes is rejected.
    @Test
    public void testRejectAddressWithWrongSeparator() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("27@@ab12IJ", "Mark Green", 4, "Medium",
                "50,Collins Street,Melbourne,VIC,Australia", "12-08-1998");

        assertFalse(service.addDriver(driver));
    }

    // D3 Test 1: Normal case - valid birthdate in DD-MM-YYYY format is accepted.
    @Test
    public void testValidBirthdateIsAccepted() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("28@@ab12KL", "Ben Black", 4, "Medium",
                "20|King Street|Melbourne|VIC|Australia", "15-06-1995");

        assertTrue(service.addDriver(driver));
    }

    // D3 Test 2: Invalid input - birthdate using slashes instead of hyphens is rejected.
    @Test
    public void testRejectBirthdateWithWrongSeparator() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("29@@ab12MN", "Nina Blue", 4, "Medium",
                "20|King Street|Melbourne|VIC|Australia", "15/06/1995");

        assertFalse(service.addDriver(driver));
    }

    // D3 Test 3: Invalid input - birthdate in YYYY-MM-DD order is rejected.
    @Test
    public void testRejectBirthdateInWrongOrder() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("32@@ab12OP", "Adam Fox", 4, "Medium",
                "20|King Street|Melbourne|VIC|Australia", "1995-06-15");

        assertFalse(service.addDriver(driver));
    }

    // D3 Test 4: Edge case - birthdate with letters instead of digits is rejected.
    @Test
    public void testRejectBirthdateWithLettersInsteadOfDigits() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver driver = new Driver("33@@ab12QR", "Chris Hall", 4, "Medium",
                "20|King Street|Melbourne|VIC|Australia", "dd-mm-yyyy");

        assertFalse(service.addDriver(driver));
    }

    // D4 Test 1: Normal case - driver with fewer than 10 years experience can change license type.
    @Test
    public void testAllowLicenseChangeForDriverWithLessThan10YearsExperience() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("34@@ab12ST", "Laura Sky", 8, "Medium",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1988");

        Driver updatedDriver = new Driver("34@@ab12ST", "Laura Sky", 8, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1988");

        service.addDriver(oldDriver);
        assertTrue(service.updateDriver(updatedDriver));
    }

    // D4 Test 2: Invalid input - driver with more than 10 years experience cannot change license type.
    @Test
    public void testRejectLicenseChangeForDriverWithMoreThan10YearsExperience() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("35@@ab12UV", "David King", 11, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1975");

        Driver updatedDriver = new Driver("35@@ab12UV", "David King", 11, "Medium",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1975");

        service.addDriver(oldDriver);
        assertFalse(service.updateDriver(updatedDriver));
    }

    // D4 Test 3: Edge case - driver with exactly 10 years experience can still change license type.
    @Test
    public void testAllowLicenseChangeForDriverWithExactly10YearsExperience() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("36@@ab12WX", "Peter Snow", 10, "Medium",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1982");

        Driver updatedDriver = new Driver("36@@ab12WX", "Peter Snow", 10, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1982");

        service.addDriver(oldDriver);
        assertTrue(service.updateDriver(updatedDriver));
    }

    // D5 Test 1: Normal case - update is accepted when driver ID and name remain unchanged.
    @Test
    public void testAllowUpdateWhenDriverIDAndNameAreUnchanged() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("37@@ab12YZ", "Same Person", 5, "Medium",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1990");

        Driver updatedDriver = new Driver("37@@ab12YZ", "Same Person", 6, "Heavy",
                "20|New Street|Melbourne|VIC|Australia", "10-10-1990");

        service.addDriver(oldDriver);
        assertTrue(service.updateDriver(updatedDriver));
    }

    // D5 Test 2: Invalid input - update is rejected when the driver name is changed.
    @Test
    public void testRejectUpdateWhenNameIsChanged() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver oldDriver = new Driver("38@@ab12ZA", "Original Name", 5, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1990");

        Driver updatedDriver = new Driver("38@@ab12ZA", "Changed Name", 5, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1990");

        service.addDriver(oldDriver);
        assertFalse(service.updateDriver(updatedDriver));
    }

    // D5 Test 3: Invalid input - update is rejected when the driver ID does not exist in the repository.
    @Test
    public void testRejectUpdateWhenDriverIDDoesNotExist() {
        DriverRepository repo = new DriverRepository("data/test-drivers.txt");
        DriverService service = new DriverService(repo);

        Driver updatedDriver = new Driver("39@@ab12BB", "Ghost Driver", 5, "Heavy",
                "10|Main Street|Melbourne|VIC|Australia", "10-10-1990");

        assertFalse(service.updateDriver(updatedDriver));
    }
}
