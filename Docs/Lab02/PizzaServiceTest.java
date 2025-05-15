package pizzashop.service;

import org.junit.jupiter.api.*;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceTest {
    private int table;
    private PaymentType type;
    private double amount;
    private PizzaService service;
    List<Payment> payments = new ArrayList<>();

    static String filename = "test_payments.txt";

    @BeforeEach
    void setUp() {
        this.service = new PizzaService(new MenuRepository(), new PaymentRepository(filename));
        payments.add(new Payment(3, PaymentType.Card, 10.49));
        payments.add(new Payment(5, PaymentType.Cash, Double.MAX_VALUE - 1));

        File file = new File(filename);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (Payment p:payments) {
                System.out.println(p.toString());
                bw.write(p.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.service = new PizzaService(new MenuRepository(), new PaymentRepository(filename));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Tag("ecp")
    @Order(1)
    void addPayment_TC1_ECP() {
        // Arrange
        table = 1;
        type = PaymentType.Card;
        amount = 23.99;

        // Act
        service.addPayment(table, type, amount);

        // Assert
        check(table, type, amount);
    }

    @Test
    @DisplayName("TC2_ECP")
    void addPayment_TC2_ECP() {
        // Arrange
        table = 0;
        type = PaymentType.Cash;
        amount = 23.99;

        // Act & Assert
        try {
            service.addPayment(table, type, amount);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    @Timeout(10)
    void addPayment_TC3_ECP() {
        // Arrange
        table = 9;
        type = PaymentType.Card;
        amount = 0;

        // Act & Assert
        try {
            service.addPayment(table, type, amount);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void addPayment_TC4_ECP() {
        // Arrange
        table = 5;
        type = PaymentType.Cash;
        amount = Double.MAX_VALUE - 1;

        // Act
        service.addPayment(table, type, amount);

        // Assert
        check(table, type, amount);
    }

    @Test
    void addPayment_TC5_ECP() {
        // Arrange
        table = 5;

        // Act & Assert
        try {
            service.addPayment(table, PaymentType.valueOf("other"), Double.parseDouble(""));
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void addPayment_TC6_ECP() {
        // Arrange
        table = 9;
        type = PaymentType.Card;
        amount = 0;

        // Act & Assert
        try {
            service.addPayment(Integer.parseInt("8"), PaymentType.valueOf("other"), Double.parseDouble("23.99"));
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void addPayment_TC1_BVA() {
        // Arrange
        table = 1;
        type = PaymentType.Card;
        amount = 0.01;

        // Act
        service.addPayment(table, type, amount);

        // Assert
        check(table, type, amount);
    }

    @Test
    void addPayment_TC2_BVA() {
        // Arrange
        table = 0;
        type = PaymentType.Cash;
        amount = 0.01;

        // Act & Assert
        try {
            service.addPayment(Integer.parseInt("8"), PaymentType.valueOf("other"), Double.parseDouble("23.99"));
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void addPayment_TC3_BVA() {
        // Arrange
        table = 9;
        type = PaymentType.Card;
        amount = 0;

        // Act & Assert
        try {
            service.addPayment(Integer.parseInt("8"), PaymentType.valueOf("other"), Double.parseDouble("23.99"));
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void addPayment_TC4_BVA() {
        // Arrange
        table = 5;
        type = PaymentType.Cash;
        amount = Double.MAX_VALUE - 1;

        // Act
        service.addPayment(table, type, amount);

        // Assert
        check(table, type, amount);
    }

    @Test
    void addPayment_TC5_BVA() {
        // Arrange
        table = 5;
        type = PaymentType.Card;
        amount = Double.POSITIVE_INFINITY;

        // Act & Assert
        try {
            service.addPayment(table, type, amount);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void addPayment_TC6_BVA() {
        // Arrange
        table = 9;
        type = PaymentType.Cash;
        amount = Double.MAX_VALUE + 1;

        // Act & Assert
        try {
            service.addPayment(table, type, amount);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    @Disabled
    void addPayment_TC6_BVA_Disabled() {
        // Arrange
        table = 9;
        type = PaymentType.Cash;
        amount = Double.MAX_VALUE + 1;

        // Act & Assert
        try {
            service.addPayment(table, type, amount);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    void check(int table, PaymentType type, double amount) {
        File file = new File(filename);
        String lastLine = null;
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = null;
            while((line = br.readLine()) != null) {
                lastLine = line;
            }
        } catch (IOException e) {
            assert false;
        }
        assert Objects.equals(lastLine, new Payment(table, type, amount).toString());
    }
}