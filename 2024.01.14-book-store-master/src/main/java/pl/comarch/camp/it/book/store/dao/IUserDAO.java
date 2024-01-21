package pl.comarch.camp.it.book.store.dao;

import pl.comarch.camp.it.book.store.model.User;

import java.util.List;

public interface IUserDAO {
    User getById(int id);
    User getByLogin(String login);
    List<User> getAll();
    void save(User user);
    void delete(int id);
    void update(User user);
}
