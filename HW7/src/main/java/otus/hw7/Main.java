package otus.hw7;

import otus.hw7.atm.ATM;
import otus.hw7.atm.Cell;
import otus.hw7.base.Event;
import otus.hw7.department.Department;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tully.
 */
public class Main {
    public static void main(String[] args) {
        List<Cell> cells = new ArrayList<>();

        cells.add(new Cell(1, 10));
        cells.add(new Cell(5, 10));
        cells.add(new Cell(10, 10));
        cells.add(new Cell(50, 10));
        cells.add(new Cell(100, 10));

        ATM atm = new ATM(cells);
        System.out.println("Initial balance: " + atm.getBalance());

        atm.withdraw(1578);
        System.out.println("Final balance: " + atm.getBalance());

        Department department = new Department();
        department.register(atm);
        department.register(new ATM(cells));

        System.out.println(department.getBalance());

        department.notify(Event.EMPTY);
        System.out.println(department.getBalance());

        department.notify(Event.RESET);
        System.out.println(department.getBalance());
    }
}
