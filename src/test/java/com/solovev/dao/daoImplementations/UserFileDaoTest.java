package com.solovev.dao.daoImplementations;

import com.solovev.DBSetUpAndTearDown;
import com.solovev.DataConstants;
import com.solovev.model.User;
import com.solovev.model.UserFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class UserFileDaoTest {
    @Test
    public void serverNameConfiguration(){
        int expectedId = FILES.size() + 1;
        UserFile fileToAdd = new UserFile("added.exe",USERS.get(0));
        UserFile expectedOutcome = new UserFile(expectedId,fileToAdd.getFileName(),expectedId + ".exe",USERS.get(0));

        assertFalse(userFileDao.get().contains(fileToAdd));
        assertFalse(userFileDao.get().contains(expectedOutcome));

        assertTrue(userFileDao.add(fileToAdd));
        assertEquals(expectedOutcome, userFileDao.get(expectedId).get());
        assertEquals(expectedOutcome,fileToAdd);
    }
    @Test
    public void getAll(){
        assertEquals(FILES, userFileDao.get());
    }
    @Test
    void getFilesByUserId() {
        User firstUser = USERS.get(0);
        User lastUser = USERS.get(USERS.size()-1);
        long firstUserId = firstUser.getId();
        long lastUserId = lastUser.getId();
        long nonExistentUser = firstUserId - 1;
        Collection<UserFile> firstUserCards = FILES.stream().filter(userFile -> userFile.getUser().equals(firstUser)).toList();
        Collection<UserFile> lastUserCards = FILES.stream().filter(userFile -> userFile.getUser().equals(lastUser)).toList();

        assertEquals(firstUserCards, userFileDao.getByUser(firstUserId));
        assertEquals(lastUserCards, userFileDao.getByUser(lastUserId));
        assertEquals(List.of(), userFileDao.getByUser(nonExistentUser));
    }
    @Test
    public void getFileByUserAndNameSuccess() {
        UserFile fileToFind = FILES.get(1);
        long userId = fileToFind.getUser().getId();
        String fileName = fileToFind.getFileName();

        assertEquals(fileToFind, userFileDao.getByNameAndUser(userId,fileName).get());
    }

    @Test
    public void getFileByUserAndNameNotFound() {
        UserFile fileToFind = FILES.get(1);
        long userId = fileToFind.getUser().getId();
        String fileName = fileToFind.getFileName();
        long nonExistentId = USERS.size() + 1;
        String nonExistentFileName = fileName + " corrupted";

        assertEquals(Optional.empty(), userFileDao.getByNameAndUser(userId, nonExistentFileName));
        assertEquals(Optional.empty(), userFileDao.getByNameAndUser(nonExistentId, fileName));
        assertEquals(Optional.empty(), userFileDao.getByNameAndUser(nonExistentId, nonExistentFileName));

    }

    @Nested
    public class ConstraintsViolation{
        @Test
        public void successfullyAdded() {
        UserFile fileToAdd = new UserFile("added.exe",USERS.get(0));
        UserFile otherToAdd = new UserFile(fileToAdd.getFileName(),  USERS.get(USERS.size()-1));

        assumeFalse(userFileDao.get().contains(fileToAdd));
        assertTrue(userFileDao.add(fileToAdd));
        assertTrue(userFileDao.get().contains(fileToAdd));

        assumeFalse(userFileDao.get().contains(otherToAdd));
        assertTrue(userFileDao.add(otherToAdd));
        assertTrue(userFileDao.get().contains(otherToAdd));
    }

        @Test
        public void nonNullViolated() {
        UserFile emptyUserFiles= new UserFile();
        assertThrows(IllegalArgumentException.class, () -> userFileDao.add(emptyUserFiles));
        assertTrue(checkTableDidntChange());
    }

        @Test
        public void constantViolations() {
        UserFile existingFiles = FILES.get(0);
        UserFile notUniqueFields = new UserFile(existingFiles.getFileName(), existingFiles.getUser());

        assertTrue(userFileDao.get().contains(existingFiles ));
        assertThrows(IllegalArgumentException.class, () -> userFileDao.add(existingFiles));
        assertTrue(checkTableDidntChange());

        assertFalse(userFileDao.get().contains(notUniqueFields));
        assertThrows(IllegalArgumentException.class, () -> userFileDao.add(notUniqueFields));
        assertTrue(checkTableDidntChange());
    }
        private boolean checkTableDidntChange() {
        return userFileDao.get().equals(FILES);
    }

    }
    private static UserFileDao userFileDao;
    @BeforeEach
    public void setUp() throws SQLException, IOException, ClassNotFoundException {
        dbSetUpAndTearDown.dbFactoryAndTablesCreation();

        dbSetUpAndTearDown.setUpUsersTableValues(USERS);
        dbSetUpAndTearDown.setUpFilesTableValues(FILES);
        userFileDao = new UserFileDao();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dbSetUpAndTearDown.dbFactoryAndTablesTearDown();
    }
    private static final DBSetUpAndTearDown dbSetUpAndTearDown = new DBSetUpAndTearDown();
    private final List<User> USERS = DataConstants.USERS;
    private final List<UserFile> FILES = DataConstants.FILES;
}