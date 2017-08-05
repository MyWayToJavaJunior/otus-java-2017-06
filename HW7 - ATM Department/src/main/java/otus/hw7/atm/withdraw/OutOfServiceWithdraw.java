package otus.hw7.atm.withdraw;

import otus.hw7.atm.Cell;
import otus.hw7.base.WithdrawStrategy;

public class OutOfServiceWithdraw implements WithdrawStrategy {
    @Override
    public boolean withdraw(Cell cell, int requested) {
        return false;
    }
}
