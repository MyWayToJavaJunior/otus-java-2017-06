import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CSVReader reader = new CSVReader(new FileReader("pom.xml"));

        System.out.println("Hello world");
    }
}
