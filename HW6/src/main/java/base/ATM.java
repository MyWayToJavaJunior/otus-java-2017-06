package base;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import atm.Banknote;

public interface ATM {
    void put(Banknote[] banknotes);
    Banknote[] get(int amount) throws InvalidAmountException, InsufficientFundsException;
    long getRestInfo();
}
