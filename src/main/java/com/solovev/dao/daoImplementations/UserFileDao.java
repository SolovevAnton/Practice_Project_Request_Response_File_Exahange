package com.solovev.dao.daoImplementations;

import com.solovev.dao.AbstractDAO;
import com.solovev.model.UserFile;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class UserFileDao extends AbstractDAO<UserFile> {
    public UserFileDao() {
        super(UserFile.class);
    }

    @Override
    public boolean add(UserFile userFiles) {
        super.add(userFiles);
        String serverFileName = serverFileNameConfiguration(userFiles);
        userFiles.setServerFileName(serverFileName);
        return super.update(userFiles);
    }
    private String serverFileNameConfiguration(UserFile userFile){
        String extension = userFile.getFileName().replaceAll(".*\\.","");
        return String.format("%d.%s",userFile.getId(),extension);
    }
    public Collection<UserFile> getByUser(long userId){
        Map<String, Object> params = Map.of("user", userId);
        return getObjectsByParam(params);
    }
    public Optional<UserFile> getByNameAndUser(long userId, String fileName){
        Map<String, Object> params = Map.of("user", userId,"fileName",fileName);
        return getObjectByParam(params);
    }
}
