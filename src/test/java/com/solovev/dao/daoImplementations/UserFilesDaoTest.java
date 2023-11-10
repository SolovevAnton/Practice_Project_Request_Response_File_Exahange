package com.solovev.dao.daoImplementations;

import com.solovev.DBSetUpAndTearDown;
import com.solovev.DataConstants;
import com.solovev.model.User;
import com.solovev.model.UserFiles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.Table;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserFilesDaoTest {
    @Test
    public void getAll(){
        assertEquals(FILES,userFilesDao.get());
    }
    private static UserFilesDao userFilesDao;
    @BeforeEach
    public void setUp() throws SQLException, IOException, ClassNotFoundException {
        dbSetUpAndTearDown.dbFactoryAndTablesCreation();

        dbSetUpAndTearDown.setUpUsersTableValues(USERS);
        dbSetUpAndTearDown.setUpFilesTableValues(FILES);
        userFilesDao = new UserFilesDao();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dbSetUpAndTearDown.dbFactoryAndTablesTearDown();
    }
    private static final DBSetUpAndTearDown dbSetUpAndTearDown = new DBSetUpAndTearDown();
    private final List<User> USERS = DataConstants.USERS;
    private final List<UserFiles> FILES = DataConstants.FILES;
}