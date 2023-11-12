package com.solovev.servlet;

import com.solovev.DBSetUpAndTearDown;
import com.solovev.DataConstants;
import com.solovev.dao.daoImplementations.UserFileDao;
import com.solovev.model.User;
import com.solovev.model.UserFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class FileServletTest {
    @Nested
    public class DoGetTests {
        @Test
        public void correctGet(){ fail();}
    }
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @BeforeEach
    public void setUp() throws SQLException, IOException, ClassNotFoundException {
        dbSetUpAndTearDown.dbFactoryAndTablesCreation();

        dbSetUpAndTearDown.setUpUsersTableValues(USERS);
        dbSetUpAndTearDown.setUpFilesTableValues(FILES);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dbSetUpAndTearDown.dbFactoryAndTablesTearDown();
    }
    private static final DBSetUpAndTearDown dbSetUpAndTearDown = new DBSetUpAndTearDown();
    private final List<User> USERS = DataConstants.USERS;
    private final List<UserFile> FILES = DataConstants.FILES;
}