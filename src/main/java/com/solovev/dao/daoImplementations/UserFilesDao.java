package com.solovev.dao.daoImplementations;

import com.solovev.dao.AbstractDAO;
import com.solovev.dao.DAO;
import com.solovev.model.UserFiles;

public class UserFilesDao extends AbstractDAO<UserFiles> {
    public UserFilesDao() {
        super(UserFiles.class);
    }
}
