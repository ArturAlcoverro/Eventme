package com.androidprog2.eventme.Persistance.API;

import com.androidprog2.eventme.Persistance.UserDAO;
import com.androidprog2.eventme.business.User;

import java.util.ArrayList;

public class APIUserDAO implements UserDAO {

    @Override
    public void createUser(User user) {
        //APIConnector.getInstance().postApi();
    }

    @Override
    public void loginUser(User user) {
        APIConnector.getInstance().postApi();
    }

    @Override
    public ArrayList<User> getAllUsers() {
        APIConnector.getInstance().getApi();
        return null;
    }

    @Override
    public User searchById(int id) {
        APIConnector.getInstance().getApi();
        return null;
    }

    @Override
    public void changeUser(User user) {
        APIConnector.getInstance().putApi();
    }

    @Override
    public void deleteUser() {
        APIConnector.getInstance().deleteApi();
    }
}
