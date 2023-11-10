package com.solovev;

import com.solovev.model.User;

import java.util.List;

public class DataConstants {
    public static final List<User> USERS = List.of(
            new User("firstLog", "firstPass", "first"),
            new User("secondLog", "secondPass", "second"),
            new User("thirdLog", "thirdPass", "third"),
            new User("fourthLog", "fourthPass", "third")
    );
}
