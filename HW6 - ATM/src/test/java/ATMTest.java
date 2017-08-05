import atm.ATMImpl;
import atm.Banknote;
import atm.Denomination;
import base.ATM;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class ATMTest {
    private ATM atm;

    @Before
    public void before() {
        atm = new ATMImpl();
    }

    @Test
    public void testPut() throws Exception {
        atm.put(new Banknote[] {new Banknote(Denomination._50)});
        Assert.assertEquals(50, atm.getRestInfo());

        atm.put(new Banknote[] {new Banknote(Denomination._50), new Banknote(Denomination._50)});
        Assert.assertEquals(150, atm.getRestInfo());

        atm.put(new Banknote[] {new Banknote(Denomination._500), new Banknote(Denomination._5000), new Banknote(Denomination._5000)});
        Assert.assertEquals(10_650, atm.getRestInfo());
    }

    @Test
    public void testGet() throws Exception {
        atm.put(new Banknote[] {new Banknote(Denomination._500), new Banknote(Denomination._5000),
                new Banknote(Denomination._5000), new Banknote(Denomination._50), new Banknote(Denomination._50)});

        Assert.assertEquals(2, atm.get(100).length);
        Assert.assertEquals(2, atm.get(5500).length);
    }

    @Test(expected = InsufficientFundsException.class)
    public void testInsufficientFunds() throws Exception {
        atm.get(100);
    }

    @Test(expected = InvalidAmountException.class)
    public void testInvalidAmount() throws Exception {
        atm.put(new Banknote[] {new Banknote(Denomination._500), new Banknote(Denomination._1000), new Banknote(Denomination._5000)});
        atm.get(2500);
    }
}
