package pizzashop.mocks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;
import pizzashop.utils.ListUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class MockEntityIntegrationTest {
    static String filename = "integrationTest_payments.txt";
    private PizzaService pizzaService;
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
        Mockito.when(p2.getAmount()).thenReturn(12.99);
        Mockito.when(p2.toString()).thenReturn("5,Cash,12.99");

        List<Payment> payments = ListUtils.of(p1, p2);

        File file = new File(filename);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            for (Payment p : payments) {
                System.out.println(p.toString());
                bw.write(p.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MenuRepository repoMenu = new MenuRepository("data/menu.txt");
            PaymentRepository paymentRepository = new PaymentRepository(filename);
            pizzaService = new PizzaService(repoMenu, paymentRepository);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void test_getAll() {
        List<Payment> expected = ListUtils.of(p1, p2);
        List<Payment> actual = pizzaService.getPayments();

        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(comparePaymentWithMock(expected.get(i), actual.get(i)));
        }
    }

    @Test
    void test_getTotalAmount() {
        double expected = p1.getAmount();
        double actual = pizzaService.getTotalAmount(PaymentType.Card);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_add() {
        Payment payment = Mockito.mock(Payment.class);
        Mockito.when(payment.getTableNumber()).thenReturn(1);
        Mockito.when(payment.getType()).thenReturn(PaymentType.Card);
        Mockito.when(payment.getAmount()).thenReturn(10.0);

        pizzaService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount());
        List<Payment> expected = ListUtils.of(p1, p2, payment);
        List<Payment> actual = pizzaService.getPayments();

        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(comparePaymentWithMock(expected.get(i), actual.get(i)));
        }
    }

    private boolean comparePaymentWithMock(Payment expected, Payment actual) {
        return expected.getTableNumber() == actual.getTableNumber() && expected.getType() == actual.getType() && Double.compare(expected.getAmount(), actual.getAmount()) == 0;
    }
}