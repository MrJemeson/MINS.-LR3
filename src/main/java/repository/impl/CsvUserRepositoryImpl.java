package repository.impl;

import csv.CsvIO;
import csv.CsvMapper;
import csv.UserCsvMapper;
import object.User;
import repository.UserRepository;
import storage.FileStorage;

import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CsvUserRepositoryImpl implements UserRepository {
    private final Path csvPath;
    private final FileStorage storage;
    private final CsvMapper<User> mapper;

    public CsvUserRepositoryImpl(Path csvPath, FileStorage storage) {
        this(csvPath, storage, new UserCsvMapper());
    }

    public CsvUserRepositoryImpl(Path csvPath, FileStorage storage, CsvMapper<User> mapper) {
        this.csvPath = csvPath;
        this.storage = storage;
        this.mapper = mapper;
    }

    private List<User> loadUsers(){
        return CsvIO.readAll(csvPath, storage, mapper);
    }

    @Override
    public Optional<User> findByName(String userName) {
        List<User> users = loadUsers();
        try {
            User user = users.stream().filter(x -> x.getUserName().equals(userName)).findFirst().orElseThrow();
            return Optional.of(user);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUserId(int userId) {
        List<User> users = loadUsers();
        try {
            User user = users.stream().filter(x -> x.getId() == userId).findFirst().orElseThrow();
            return Optional.of(user);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
