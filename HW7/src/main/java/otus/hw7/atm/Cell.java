package otus.hw7.atm;

import otus.hw7.atm.withdraw.GreedyWithdraw;
import otus.hw7.base.WithdrawStrategy;
import otus.hw7.base.Withdrawable;

import java.util.Iterator;

/**
 * Created by tully.
 */
public class Cell implements Comparable<Cell>, Iterable<Cell>, Withdrawable {
    private final int nominal;
    private int count;
    private final int startCount;
    private WithdrawStrategy withdrawStrategy = new GreedyWithdraw();;

    private Cell next;

    public Cell(int nominal, int count) {
        this.nominal = nominal;
        this.count = count;
        this.startCount = count;
    }

    public boolean withdraw(int requested) {
        return withdrawStrategy.withdraw(this, requested);
    }

    public int getNominal() {
        return nominal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setNext(Cell next) {
        this.next = next;
    }

    public int getBalance() {
        return count * nominal;
    }

    public Cell getNext() {
        return next;
    }

    @Override
    public int compareTo(Cell o) {
        if (nominal > o.getNominal())
            return -1;
        if (nominal < o.getNominal())
            return 1;
        return 0;
    }

    @Override
    public Iterator<Cell> iterator() {
        return new Iterator<Cell>() {
            Cell current = Cell.this;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Cell next() {
                Cell before = current;
                current = current.next;
                return before;
            }
        };
    }

    @Override
    public void setStrategy(WithdrawStrategy strategy) {
        withdrawStrategy = strategy;
    }

    public void reset() {
        count = startCount;
    }

    public void empty() {
        count = 0;
    }
}
