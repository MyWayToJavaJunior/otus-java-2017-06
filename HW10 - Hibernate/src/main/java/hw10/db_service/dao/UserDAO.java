package hw10.db_service.dao;

import hw10.dataset.UserDataSet;
import org.hibernate.Session;

public class UserDAO {
    private final Session session;

    public UserDAO(Session session) {
        this.session = session;
    }

    public void save(UserDataSet userDataSet) {
        session.save(userDataSet);
    }

    public UserDataSet get(long id) {
        return session.load(UserDataSet.class, id);
    }
}
