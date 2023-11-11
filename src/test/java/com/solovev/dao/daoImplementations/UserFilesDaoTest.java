package com.solovev.dao.daoImplementations;

import com.solovev.DBSetUpAndTearDown;
import com.solovev.DataConstants;
import com.solovev.model.User;
import com.solovev.model.UserFiles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.persistence.Table;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class UserFilesDaoTest {
    @Test
    public void getAll(){
        assertEquals(FILES,userFilesDao.get());
    }
    @Test
    public void serverNameConfiguration(){
        int expectedId = FILES.size() + 1;
        UserFiles fileToAdd = new UserFiles("added.exe",USERS.get(0));
        UserFiles expectedOutcome = new UserFiles(expectedId,fileToAdd.getFileName(),expectedId + ".exe",USERS.get(0));

        assertFalse(userFilesDao.get().contains(fileToAdd));
        assertFalse(userFilesDao.get().contains(expectedOutcome));

        assertTrue(userFilesDao.add(fileToAdd));
        assertEquals(expectedOutcome,userFilesDao.get(expectedId).get());
        assertEquals(expectedOutcome,fileToAdd);
    }
    @Nested
    public class ConstraintsViolation{
        @Test
        public void successfullyAdded() {
        UserFiles fileToAdd = new UserFiles("added.exe",USERS.get(0));
        UserFiles otherToAdd = new UserFiles(fileToAdd.getFileName(),  USERS.get(USERS.size()-1));

        assumeFalse(userFilesDao.get().contains(fileToAdd));
        assertTrue(userFilesDao.add(fileToAdd));
        assertTrue(userFilesDao.get().contains(fileToAdd));

        assumeFalse(userFilesDao.get().contains(otherToAdd));
        assertTrue(userFilesDao.add(otherToAdd));
        assertTrue(userFilesDao.get().contains(otherToAdd));
    }

        @Test
        public void nonNullViolated() {
        UserFiles emptyUserFiles= new UserFiles();
        assertThrows(IllegalArgumentException.class, () -> userFilesDao.add(emptyUserFiles));
        assertTrue(checkTableDidntChange());
    }

        @Test
        public void constantViolations() {
        UserFiles existingFiles = FILES.get(0);
        UserFiles notUniqueFields = new UserFiles(existingFiles.getFileName(), existingFiles.getUser());

        assertTrue(userFilesDao.get().contains(existingFiles ));
        assertThrows(IllegalArgumentException.class, () -> userFilesDao.add(existingFiles));
        assertTrue(checkTableDidntChange());

        assertFalse(userFilesDao.get().contains(notUniqueFields));
        assertThrows(IllegalArgumentException.class, () -> userFilesDao.add(notUniqueFields));
        assertTrue(checkTableDidntChange());
    }
        private boolean checkTableDidntChange() {
        return userFilesDao.get().equals(FILES);
    }

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