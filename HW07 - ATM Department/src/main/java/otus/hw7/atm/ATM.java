package otus.hw7.atm;

import otus.hw7.base.Event;
import otus.hw7.base.ATMObserver;
import otus.hw7.base.WithdrawStrategy;
import otus.hw7.base.Withdrawable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tully.
 */
public class ATM implements ATMObserver, Withdrawable {
    private final Cell first;

    public ATM(List<Cell> cells) {
        Collections.sort(cells);
        first = cells.get(0);
        linkCells(cells);
    }

    public boolean withdraw(int requested) {
        return first.withdraw(requested);
    }

    public int getBalance() {
        Iterator<Cell> iterator = first.iterator();
        int balance = 0;
        while (iterator.hasNext()) {
            balance += iterator.next().getBalance();
        }
        return balance;
    }

    private void linkCells(List<Cell> cells) {
        Iterator<Cell> iterator = cells.iterator();
        Cell cellA = iterator.next();
        while (iterator.hasNext()) {
            Cell cellB = iterator.next();
            cellA.setNext(cellB);
            cellA = cellB;
        }
    }

    @Override
    public void notify(Event event) {
        Iterator<Cell> iterator = first.iterator();
        switch (event) {
            case RESET:
                while (iterator.hasNext()) iterator.next().reset();
                break;
            case EMPTY:
                while (iterator.hasNext()) iterator.next().empty();
                break;
        }
    }

    @Override
    public void setStrategy(WithdrawStrategy strategy) {
        Iterator<Cell> iterator = first.iterator();
        while (iterator.hasNext()) iterator.next().setStrategy(strategy);
    }
}
