package atm;

public enum Denomination {
    // sorted by value in desc
    _5000 (5000),
    _1000 (1000),
    _500 (500),
    _100 (100),
    _50 (50);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Denomination getByValue(int value) {
        for (Denomination d : Denomination.values()) {
            if (d.getValue() == value) return d;
        }
        return null;
    }
}
