package hw10.db_service.dao;

import hw10.dataset.UserDataSet;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

    public UserDataSet get(String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        Root<UserDataSet> from = criteria.from(UserDataSet.class);
        criteria.where(builder.equal(from.get("name"), name));
        Query<UserDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
    }
}
