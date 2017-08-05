package otus.hw7.department;

import otus.hw7.atm.ATM;
import otus.hw7.base.Event;
import otus.hw7.base.WithdrawStrategy;
import otus.hw7.base.Withdrawable;

import java.util.ArrayList;
import java.util.List;

public class Department implements Withdrawable {
    private List<ATM> atms = new ArrayList<>();

    public void register(ATM atm) {
        atms.add(atm);
    }

    public void unregister(ATM atm) {
        atms.remove(atm);
    }

    public int getBalance() {
        return atms.stream().mapToInt(ATM::getBalance).sum();
    }

    public void notify(Event event) {
        atms.forEach(atm -> atm.notify(event));
    }

    @Override
    public void setStrategy(WithdrawStrategy strategy) {
        atms.forEach(atm -> atm.setStrategy(strategy));
    }
}
