package service;

import object.User;


public interface UserService {
    User getUserById(int userId);
    User getUserByName(String userName);
}
