package hw9.service;

import hw9.dataset.User;

import java.util.List;

public interface DBService {
    void save(User user);
    User get(long id);
}
