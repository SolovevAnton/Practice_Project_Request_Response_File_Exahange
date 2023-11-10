package com.solovev;

import com.solovev.dao.SessionFactorySingleton;
import com.solovev.model.User;
import com.solovev.model.UserFiles;
import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.Collection;

/**
 * Class to be used by all test methods to set up and tear down DB
 */
@Getter
public class DBSetUpAndTearDown {
    private static final String loadResourcePath = "src/test/resources/hibernatemysql.cfg.xml";
    @Getter(AccessLevel.NONE)
    private Connection connection;
    private final String USERS_TABLE_NAME = User.class.getAnnotation(Table.class).name();
    private final String FILES_TABLE_NAME = UserFiles.class.getAnnotation(Table.class).name();


    /**
     * Creates factory for DB in hibernate, create all tables if necessary
     * IMPORTANT: in test folder for resources must present hibernatemysql file for tested db, otherwise ioException will be thrown
     */
    public void dbFactoryAndTablesCreation() throws IOException, ClassNotFoundException, SQLException {
        //assert the file is presented
        File neededResourceName = new File(loadResourcePath);
        if (!Files.exists(neededResourceName.toPath())) {
            throw new IOException("configuration file: " + neededResourceName + " not found");
        }

        //creates factory and tables
        //cleans factory instance
        SessionFactorySingleton.closeAndDeleteInstance();
        SessionFactorySingleton.getInstance(neededResourceName);

        Class.forName("com.mysql.cj.jdbc.Driver");
        openConnection();
    }

    private void openConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/test_db";
        String username = "root";
        String password = "root";

        connection = DriverManager.getConnection(jdbcUrl, username, password);
    }

    /**
     * Add all users from collection to table
     */
    public void setUpUsersTableValues(Collection<User> users) throws SQLException {
        long currentMaxId = getLastId(USERS_TABLE_NAME);
        String SQL = "INSERT INTO " + USERS_TABLE_NAME + "(login,name,password) values(?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            for (User user : users) {
                statement.setString(1, user.getLogin());
                statement.setString(2, user.getName());
                statement.setString(3, user.getPassword());

                statement.addBatch();
                user.setId(++currentMaxId);
            }
            statement.executeBatch();
        }
    }
    private long getLastId(String tableName) throws SQLException {
        String SQLgetMaxId = "SELECT MAX(id) FROM " + tableName;
        ResultSet resultSet =  connection.createStatement().executeQuery(SQLgetMaxId);

        return resultSet.next() ? resultSet.getLong(1) : 0;
    }
    public void setUpFilesTableValues(Collection<UserFiles> files) throws SQLException {
        long currentMaxId = getLastId(FILES_TABLE_NAME);
        String SQL = "INSERT INTO " + FILES_TABLE_NAME + "(fileName,serverFileName,user_id) values(?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            for (UserFiles file : files) {
                statement.setString(1, file.getFileName());
                statement.setString(2, file.getServerFileName());
                statement.setLong(3, file.getUser().getId());

                statement.addBatch();
                file.setId(++currentMaxId);
            }
            statement.executeBatch();
        }
    }
    public void clearTable(String tableName) throws SQLException {
        String sqlDelete = "DELETE FROM " + tableName;
        executeStatement(sqlDelete);
    }

    public void dbFactoryAndTablesTearDown() throws SQLException {
        dropAllTables();
        closeConnection();
        SessionFactorySingleton.closeAndDeleteInstance();
    }

    private void dropAllTables() throws SQLException {
        String[] tableNames = {FILES_TABLE_NAME,USERS_TABLE_NAME}; //order is important!
        String dropTableSQL = "DROP TABLE IF EXISTS ";

        for (String tableName : tableNames) {
            executeStatement(dropTableSQL + tableName);
        }
    }

    private void executeStatement(String sqlQuery) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.executeUpdate();
        statement.close();
    }

    public void closeConnection() {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
    }
}
