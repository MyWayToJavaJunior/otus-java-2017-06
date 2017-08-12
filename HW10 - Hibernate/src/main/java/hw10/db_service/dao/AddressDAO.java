package hw10.db_service.dao;

import hw10.dataset.AddressDataSet;
import org.hibernate.Session;

public class AddressDAO {
    private final Session session;

    public AddressDAO(Session session) {
        this.session = session;
    }

    public void save(AddressDataSet addressDataSet) {
        session.save(addressDataSet);
    }

    public AddressDataSet get(long id) {
        return session.load(AddressDataSet.class, id);
    }
}
