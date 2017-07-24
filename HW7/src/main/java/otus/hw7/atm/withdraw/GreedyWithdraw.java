package otus.hw7.atm.withdraw;

import otus.hw7.atm.Cell;
import otus.hw7.base.WithdrawStrategy;

import java.util.Iterator;

public class GreedyWithdraw implements WithdrawStrategy {
    @Override
    public boolean withdraw(Cell cell, int requested) {
        int expectedCount = Math.min(requested / cell.getNominal(), cell.getCount());
        int expectedCash = expectedCount * cell.getNominal();
        boolean nextCellResult = true;
        Iterator<Cell> iterator = cell.iterator();
        Cell c = iterator.next();
        if (requested != expectedCash) {
            nextCellResult = iterator.hasNext() && iterator.next().withdraw(requested - expectedCash);
        }
        if(nextCellResult) {
            cell.setCount(cell.getCount() - expectedCount);
            return true;
        }
        return false;
    }
}
