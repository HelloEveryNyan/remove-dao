package notebook.model.repository.impl;

import notebook.model.User;
import notebook.model.repository.GBRepository;
import notebook.util.DBConnector;
import notebook.util.mapper.impl.UserMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements GBRepository {
    private final UserMapper mapper = new UserMapper();

    @Override
    public List<User> findAll() {
        List<String> lines = readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = users.stream().mapToLong(User::getId).max().orElse(0L);
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return findAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> update(Long userId, User update) {
        List<User> users = findAll();
        Optional<User> optionalUser = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst();

        optionalUser.ifPresent(editUser -> {
            editUser.setFirstName(update.getFirstName());
            editUser.setLastName(update.getLastName());
            editUser.setPhone(update.getPhone());
        });

        write(users);
        return optionalUser.map(user -> update);
    }

    @Override
    public boolean delete(Long id) {
        List<User> users = findAll();
        Optional<User> userToDelete = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (userToDelete.isPresent()) {
            users.remove(userToDelete.get());
            write(users);
            return true;
        } else {
            return false;
        }
    }

    private List<String> readAll() {
        return DBConnector.readFile();
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(mapper.toInput(u));
        }
        DBConnector.saveFile(lines);
    }
}

