package pizzashop.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentTest {
    private Payment p;

    @BeforeEach
    void setUp() {
        p = new Payment(8, PaymentType.Card, 23.99);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getTableNumber() {
        Assertions.assertEquals(8, p.getTableNumber());
    }

    @Test
    void setTableNumber() {
        Assertions.assertEquals(8, p.getTableNumber());
        p.setTableNumber(5);
        Assertions.assertEquals(5, p.getTableNumber());
    }

    @Test
    void getType() {
        Assertions.assertEquals(PaymentType.Card, p.getType());
    }

    @Test
    void setType() {
        Assertions.assertEquals(PaymentType.Card, p.getType());
        p.setType(PaymentType.Cash);
        Assertions.assertEquals(PaymentType.Cash, p.getType());
    }

    @Test
    void getAmount() {
        Assertions.assertEquals(23.99, p.getAmount());
    }

    @Test
    void setAmount() {
        Assertions.assertEquals(23.99, p.getAmount());
        p.setAmount(12.99);
        Assertions.assertEquals(12.99, p.getAmount());
    }

    @Test
    void testToString() {
        String toString = "8,Card,23.99";
        Assertions.assertEquals(toString, p.toString());
    }
}