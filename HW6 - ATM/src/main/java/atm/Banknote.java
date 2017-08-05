package atm;

import java.util.Random;

public class Banknote {
    private long id = new Random().nextLong();
    private Denomination denomination;

    public Banknote(Denomination denomination) {
        this.denomination = denomination;
    }

    public Banknote(long id, Denomination denomination) {
        this.id = id;
        this.denomination = denomination;
    }

    public long getId() {
        return id;
    }

    public Denomination getDenomination() {
        return denomination;
    }
}
