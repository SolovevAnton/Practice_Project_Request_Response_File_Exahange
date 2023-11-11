package com.solovev.dao.daoImplementations;

import com.solovev.dao.AbstractDAO;
import com.solovev.dao.DAO;
import com.solovev.model.UserFiles;

public class UserFilesDao extends AbstractDAO<UserFiles> {
    public UserFilesDao() {
        super(UserFiles.class);
    }

    @Override
    public boolean add(UserFiles userFiles) {
        super.add(userFiles);
        String serverFileName = serverFileNameConfiguration(userFiles);
        userFiles.setServerFileName(serverFileName);
        return super.update(userFiles);
    }
    private String serverFileNameConfiguration(UserFiles userFile){
        String extension = userFile.getFileName().replaceAll(".*\\.","");
        return String.format("%d.%s",userFile.getId(),extension);
    }
}
