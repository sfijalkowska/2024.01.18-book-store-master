package pl.comarch.camp.it.book.store.dao.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.comarch.camp.it.book.store.dao.IUserDAO;
import pl.comarch.camp.it.book.store.exceptions.UserAlreadyExistException;
import pl.comarch.camp.it.book.store.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class UserRepository implements IUserDAO {

    private final List<User> users = new ArrayList<>();

    private final UserIdSequence userIdSequence;

    public UserRepository(UserIdSequence userIdSequence) {
        this.users.add(new User(userIdSequence.getId(), "admin", "0192023a7bbd73250516f069df18b500",
                "Pan", "Administrator", "ADMIN"));
        this.users.add(new User(userIdSequence.getId(), "janusz", "1e6f2ac43951a6721d3d26a379cc7f8b",
                "Janusz", "Kowalski", "USER"));
        this.userIdSequence = userIdSequence;
    }

    @Override
    public User getById(int id) {
        for(User user : this.users) {
            if(user.getId() == id) {
                return user.clone();
            }
        }

        return null;
    }

    @Override
    public User getByLogin(String login) {
        for(User user : this.users) {
            if(user.getLogin().equals(login)) {
                return user.clone();
            }
        }

        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> result = new ArrayList<>();
        for(User user : this.users) {
            result.add(user.clone());
        }
        return result;
    }

    @Override
    public void save(User user) {
        user.setId(this.userIdSequence.getId());
        User userByLogin = this.getByLogin(user.getLogin());
        if(userByLogin == null) {
            this.users.add(user);
        } else {
            throw new UserAlreadyExistException("User with login: " + user.getLogin() + " already exist");
        }
    }

    @Override
    public void delete(int id) {
        Iterator<User> iterator = this.users.iterator();
        while(iterator.hasNext()) {
            User user = iterator.next();
            if(user.getId() == id) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void update(User user) {
        User userFromDb = this.getById(user.getId());
        if(userFromDb == null) {
            return;
        }
        userFromDb.setName(user.getName());
        userFromDb.setSurname(user.getSurname());
        userFromDb.setLogin(user.getLogin());
        userFromDb.setPassword(user.getPassword());
        userFromDb.setRole(user.getRole());
    }
}
