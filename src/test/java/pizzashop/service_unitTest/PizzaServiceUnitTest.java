package pizzashop.service_unitTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.IPaymentRepository;
import pizzashop.service.PizzaService;
import pizzashop.utils.ListUtils;

import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

class PizzaServiceUnitTest {
    @Mock
    private IPaymentRepository payRepo;

    @InjectMocks
    private PizzaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getPayments() {
        Payment p = new Payment(8, PaymentType.Card, 23.99);
        Mockito.when(payRepo.getAll()).thenReturn(ListUtils.of(p));
        Assertions.assertEquals(ListUtils.of(p), service.getPayments());
        Mockito.verify(payRepo, times(1)).getAll();
    }

    @Test
    void getTotalAmount() {
        Payment p = new Payment(8, PaymentType.Card, 23.99);
        Mockito.when(payRepo.getAll()).thenReturn(ListUtils.of(p));
        double total = service.getTotalAmount(PaymentType.Card);
        Assertions.assertEquals(23.99, total);
        Mockito.verify(payRepo, times(1)).getAll();
    }

    @Test
    void addPayment() {
        Payment p1 = new Payment(8, PaymentType.Card, 23.99);
        Payment p2 = new Payment(5, PaymentType.Cash, 12.99);
        Mockito.when(payRepo.getAll()).thenReturn(List.of(p1));
        Mockito.doNothing().when(payRepo).add(p2);
        service.addPayment(p2.getTableNumber(), p2.getType(), p2.getAmount());
        Mockito.verify(payRepo, times(1)).add(p2);
        Mockito.verify(payRepo, never()).getAll();
    }

}