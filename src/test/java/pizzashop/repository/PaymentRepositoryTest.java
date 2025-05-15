package pizzashop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class PaymentRepositoryTest {
    private static final String FILENAME = "unitTest_payments.txt";
    private PaymentRepository repo;

    @Mock
    private Payment p1;
    @Mock
    private Payment p2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(p1.getTableNumber()).thenReturn(8);
        Mockito.when(p1.getType()).thenReturn(PaymentType.Card);
        Mockito.when(p1.getAmount()).thenReturn(23.99);
        Mockito.when(p1.toString()).thenReturn("8,Card,23.99");

        Mockito.when(p2.getTableNumber()).thenReturn(5);
        Mockito.when(p2.getType()).thenReturn(PaymentType.Cash);
        Mockito.when(p2.getAmount()).thenReturn(12.50);
        Mockito.when(p2.toString()).thenReturn("5,Cash,12.50");
        repo = new PaymentRepository(FILENAME);
    }

    @AfterEach
    void tearDown() {
        File file = new File(FILENAME);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("8,Card,23.99");
                bw.newLine();
                bw.write("5,Cash,12.50");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void add() {
        Payment p = Mockito.mock(Payment.class);
        Mockito.when(p.getTableNumber()).thenReturn(8);
        Mockito.when(p.getType()).thenReturn(PaymentType.Card);
        Mockito.when(p.getAmount()).thenReturn(23.99);

        repo.add(p);
        List<Payment> payments = repo.getAll();
        Assertions.assertEquals(3, payments.size());
        Assertions.assertEquals(p1.getTableNumber(), payments.get(0).getTableNumber());
        Assertions.assertEquals(p1.getType(), payments.get(0).getType());
        Assertions.assertEquals(p1.getAmount(), payments.get(0).getAmount());

        Assertions.assertEquals(p2.getTableNumber(), payments.get(1).getTableNumber());
        Assertions.assertEquals(p2.getType(), payments.get(1).getType());
        Assertions.assertEquals(p2.getAmount(), payments.get(1).getAmount());
        Assertions.assertEquals(p, payments.get(2));

    }

    @Test
    void getAll() {
        List<Payment> payments = repo.getAll();
        Assertions.assertEquals(2, payments.size());
        Assertions.assertEquals(p1.getTableNumber(), payments.get(0).getTableNumber());
        Assertions.assertEquals(p1.getType(), payments.get(0).getType());
        Assertions.assertEquals(p1.getAmount(), payments.get(0).getAmount());

        Assertions.assertEquals(p2.getTableNumber(), payments.get(1).getTableNumber());
        Assertions.assertEquals(p2.getType(), payments.get(1).getType());
        Assertions.assertEquals(p2.getAmount(), payments.get(1).getAmount());
    }
}