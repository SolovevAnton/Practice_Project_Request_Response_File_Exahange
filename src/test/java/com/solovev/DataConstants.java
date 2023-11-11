package com.solovev;

import com.solovev.model.User;
import com.solovev.model.UserFile;

import java.util.List;

public class DataConstants {
    public static final List<User> USERS = List.of(
            new User("firstLog", "firstPass", "first"),
            new User("secondLog", "secondPass", "second"),
            new User("thirdLog", "thirdPass", "third"),
            new User("fourthLog", "fourthPass", "third")
    );
    public static final List<UserFile> FILES = List.of(
            new UserFile("first.txt","1.txt",USERS.get(0)),
            new UserFile("firstAgain.txt","2.txt",USERS.get(0)),
            new UserFile("second.txt","3.txt",USERS.get(1)),
            new UserFile("second.json","4.json",USERS.get(1)),
            new UserFile("first.txt","5.txt",USERS.get(2))
    );
}
