package com.solovev;

import com.solovev.model.User;
import com.solovev.model.UserFiles;

import java.util.List;

public class DataConstants {
    public static final List<User> USERS = List.of(
            new User("firstLog", "firstPass", "first"),
            new User("secondLog", "secondPass", "second"),
            new User("thirdLog", "thirdPass", "third"),
            new User("fourthLog", "fourthPass", "third")
    );
    public static final List<UserFiles> FILES = List.of(
            new UserFiles("first.txt","1.txt",USERS.get(0)),
            new UserFiles("firstAgain.txt","2.txt",USERS.get(0)),
            new UserFiles("second.txt","3.txt",USERS.get(1)),
            new UserFiles("second.json","4.json",USERS.get(1)),
            new UserFiles("first.txt","5.txt",USERS.get(2))
    );
}
