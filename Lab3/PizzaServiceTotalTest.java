package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceTotalTest {
    private static int table = 1;
    private PizzaService service;
    List<Payment> payments;
    static String filename = "test_total.txt";

    @BeforeEach
    void setUp() {
        this.service = new PizzaService(new MenuRepository(), new PaymentRepository(filename));
    }

    @AfterEach
    void tearDown() {
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTotalAmount_TC01() {
        // Arrange
        this.payments = null;

        // Act
        double total = service.getTotalAmount(PaymentType.Card);

        // Assert
        assertEquals(0, total);

        writeToFile();
    }

    @Test
    void getTotalAmount_TC02() {
        // Arrange
        this.payments = new ArrayList<>();

        // Act
        double total = service.getTotalAmount(PaymentType.Card);

        // Assert
        assertEquals(0, total);

        writeToFile();
    }

    @Test
    void getTotalAmount_TC04() {
        // Arrange
        double expected = 23.99;
        this.payments = new ArrayList<>();
        service.addPayment(table, PaymentType.Card, 23.99);
        writeToFile();

        // Act
        double total = service.getTotalAmount(PaymentType.Card);

        // Assert
        assertEquals(expected, total);
    }

    @Test
    void getTotalAmount_TC05() {
        // Arrange
        double expected = 0;
        this.payments = new ArrayList<>();
        service.addPayment(table, PaymentType.Card, 23.99);
        writeToFile();

        // Act
        double total = service.getTotalAmount(PaymentType.Cash);

        // Assert
        assertEquals(expected, total);
    }

    @Test
    void getTotalAmount_TC06() {
        // Arrange
        double expected = 36.98;
        this.payments = new ArrayList<>();
        service.addPayment(table, PaymentType.Cash, 23.99);
        service.addPayment(table, PaymentType.Cash, 12.99);
        writeToFile();

        // Act
        double total = service.getTotalAmount(PaymentType.Cash);

        // Assert
        assertEquals(expected, total);
    }

    @Test
    void getTotalAmount_TC07() {
        // Arrange
        double expected = 12.99;
        this.payments = new ArrayList<>();
        service.addPayment(table, PaymentType.Cash, 23.99);
        service.addPayment(table, PaymentType.Card, 12.99);
        writeToFile();

        // Act
        double total = service.getTotalAmount(PaymentType.Card);

        // Assert
        assertEquals(expected, total);
    }

    @Test
    void getTotalAmount_TC08() {
        // Arrange
        double expected = 23.99;
        this.payments = new ArrayList<>();
        service.addPayment(table, PaymentType.Cash, 23.99);
        service.addPayment(table, PaymentType.Card, 12.99);
        writeToFile();

        // Act
        double total = service.getTotalAmount(PaymentType.Cash);

        // Assert
        assertEquals(expected, total);
    }

    @Test
    void getTotalAmount_TC09() {
        // Arrange
        double expected = 0;
        this.payments = new ArrayList<>();
        service.addPayment(table, PaymentType.Card, 23.99);
        service.addPayment(table, PaymentType.Card, 12.99);
        writeToFile();

        // Act
        double total = service.getTotalAmount(PaymentType.Cash);

        // Assert
        assertEquals(expected, total);
    }

    void writeToFile() {
        if(this.payments != null) {
            File file = new File(filename);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (Payment p:payments) {
                    bw.write(p.toString());
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}