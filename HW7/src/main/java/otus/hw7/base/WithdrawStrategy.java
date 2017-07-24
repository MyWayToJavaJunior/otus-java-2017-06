package otus.hw7.base;

import otus.hw7.atm.Cell;

public interface WithdrawStrategy {
    public boolean withdraw(Cell cell, int requested);
}
