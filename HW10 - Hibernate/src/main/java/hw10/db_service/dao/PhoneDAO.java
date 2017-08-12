package hw10.db_service.dao;

import hw10.dataset.PhoneDataSet;
import org.hibernate.Session;

public class PhoneDAO {
    private final Session session;

    public PhoneDAO(Session session) {
        this.session = session;
    }

    public void save(PhoneDataSet phoneDataSet) {
        session.save(phoneDataSet);
    }

    public PhoneDataSet get(long id) {
        return session.load(PhoneDataSet.class, id);
    }
}
