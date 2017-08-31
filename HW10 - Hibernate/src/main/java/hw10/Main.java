package hw10;

import hw10.dataset.AddressDataSet;
import hw10.dataset.PhoneDataSet;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;
import hw10.db_service.DBServiceImpl;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DBService dbService = new DBServiceImpl();

        UserDataSet admin = new UserDataSet("Admin", "secret", 1, new AddressDataSet("Street"),
                Arrays.asList(new PhoneDataSet("+70001112200")));
        admin.setRole("ADMIN");
        dbService.save(admin);

        dbService.save(new UserDataSet("Name1", "pass1", 1, new AddressDataSet("Street1"),
                Arrays.asList(new PhoneDataSet("+70001112201"), new PhoneDataSet("+70001112202"))));

        dbService.save(new UserDataSet("Name2", "pass1",1, new AddressDataSet("Street2"),
                Arrays.asList(new PhoneDataSet("+70001112203"))));

        UserDataSet user1 = dbService.get(1);
        UserDataSet user2 = dbService.get(2);
    }
}
