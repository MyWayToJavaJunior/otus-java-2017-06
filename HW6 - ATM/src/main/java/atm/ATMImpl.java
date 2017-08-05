package atm;

import base.ATM;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

import java.util.*;

public class ATMImpl implements ATM {
    private final Map<Denomination, Queue<Banknote>> banknoteDenominations;
    private long balance = 0;

    public ATMImpl() {
        this.banknoteDenominations = new HashMap<>();
        for (Denomination d : Denomination.values()) {
            banknoteDenominations.put(d, new ArrayDeque<>());
        }
    }

    public void put(Banknote[] banknotes) {
        for (Banknote b : banknotes) {
            banknoteDenominations.get(b.getDenomination()).add(b);
            balance += b.getDenomination().getValue();
        }
    }

    public Banknote[] get(int amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount > balance) throw new InsufficientFundsException();
        if (!checkPossibilityOfRelease(amount)) throw new InvalidAmountException();

        List<Banknote> result = new ArrayList<>();
        for (Denomination d : Denomination.values()) {
            while(true) {
                if ((amount - d.getValue()) < 0 || banknoteDenominations.get(d).size() == 0) break;
                amount -= d.getValue();
                result.add(banknoteDenominations.get(d).poll());
            }
            if (amount == 0) break;
        }

        balance -= amount;
        return result.toArray(new Banknote[result.size()]);
    }

    public long getRestInfo() {
        return balance;
    }

    private boolean checkPossibilityOfRelease(int amount) {
        Map<Denomination, Integer> valuesMap = new HashMap<>();
        for (Denomination d : Denomination.values()) valuesMap.put(d, banknoteDenominations.get(d).size());

        for (Denomination d : Denomination.values()) {
            while(true) {
                if ((amount - d.getValue()) < 0 || valuesMap.get(d) == 0) break;
                amount -= d.getValue();
                valuesMap.put(d, valuesMap.get(d) - 1);
            }
            if (amount == 0) return true;
        }
        return false;
    }
}
