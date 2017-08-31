package hw10.db_service;

import hw10.dataset.UserDataSet;

public interface DBService {
    void save(UserDataSet user);
    UserDataSet get(long id);
    UserDataSet get(String name);
}
