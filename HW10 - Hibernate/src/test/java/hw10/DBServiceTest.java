package hw10;

import hw10.dataset.AddressDataSet;
import hw10.dataset.PhoneDataSet;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;
import hw10.db_service.DBServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class DBServiceTest {
    DBService dbService;

    @Before
    public void setup() {
        dbService = new DBServiceImpl();
    }

    @Test
    public void test() {
        UserDataSet user = new UserDataSet("Name1", "pass", 1, new AddressDataSet("Street1"),
                Arrays.asList(new PhoneDataSet("+70001112201"), new PhoneDataSet("+70001112202")));
        dbService.save(user);

        UserDataSet actual = dbService.get(1);
        Assert.assertEquals(user.getId(), actual.getId());
        Assert.assertEquals(user.getName(), actual.getName());
        Assert.assertEquals(user.getAddress().getStreet(), actual.getAddress().getStreet());
    }
}
