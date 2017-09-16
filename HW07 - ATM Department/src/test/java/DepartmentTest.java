import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import otus.hw7.atm.ATM;
import otus.hw7.atm.Cell;
import otus.hw7.atm.withdraw.GreedyWithdraw;
import otus.hw7.atm.withdraw.OutOfServiceWithdraw;
import otus.hw7.base.Event;
import otus.hw7.department.Department;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DepartmentTest {
    private Department department = new Department();
    private ATM atm1;
    private ATM atm2;

    @Before
    public void setup() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(5, 10));

        atm1 = new ATM(cells);
        department.register(atm1);

        cells = new ArrayList<>();
        cells.add(new Cell(1, 10));
        cells.add(new Cell(5, 10));
        cells.add(new Cell(10, 10));
        cells.add(new Cell(50, 10));
        cells.add(new Cell(100, 10));

        atm2 = new ATM(cells);
        department.register(atm2);
    }

    @Test
    public void getBalance() {
        int startBalance = 1710;
        assertEquals(startBalance, department.getBalance());

        atm2.withdraw(353);
        atm1.withdraw(25);

        assertEquals(startBalance - 353 - 25, department.getBalance());
    }

    @Test
    public void testNotify() {
        int balance = department.getBalance();

        department.notify(Event.EMPTY);
        assertEquals(0, department.getBalance());

        department.notify(Event.RESET);
        assertEquals(balance, department.getBalance());
    }

    @Test
    public void setStrategy() {
        department.setStrategy(new OutOfServiceWithdraw());
        assertFalse(atm1.withdraw(25));

        department.setStrategy(new GreedyWithdraw());
        assertTrue(atm1.withdraw(25));
    }
}
