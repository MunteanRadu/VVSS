package pizzashop.mocks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

class NoMockEntityIntegrationTests {
    static String filename = "integrationTest_payments.txt";
    private PizzaService pizzaService;
    private Payment p1;
    private Payment p2;

    @BeforeEach
    void setUp() {
        p1 = new Payment(8, PaymentType.Card, 23.99);
        p2 = new Payment(5, PaymentType.Cash, 12.99);
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
        Assertions.assertEquals(ListUtils.of(p1, p2), pizzaService.getPayments());
    }

    @Test
    void test_getTotalAmount() {
        double expected = p1.getAmount();
        double actual = pizzaService.getTotalAmount(PaymentType.Card);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_add() {
        Payment payment = new Payment(1, PaymentType.Card, 10.0);
        pizzaService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount());
        Assertions.assertEquals(ListUtils.of(p1, p2, payment), pizzaService.getPayments());
    }
}