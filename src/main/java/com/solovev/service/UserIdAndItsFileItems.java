package com.solovev.service;

import com.solovev.dao.daoImplementations.UserDao;
import com.solovev.dao.daoImplementations.UserFileDao;
import com.solovev.model.User;
import com.solovev.model.UserFile;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class UserIdAndItsFileItems {
    private static final String USER_ID_PARAMETER_NAME = "userId";
    private static final String NO_USER_ID_MSG = "No user id provided";
    private static final String NO_USER_WITH_THIS_ID_MSG = "No user with this ID found";
    private static final String NO_FILE_PROVIDED = "No files were provided";
    private final AtomicReference<UserFileDao> fileDao = new AtomicReference<>(new UserFileDao());
    private final AtomicReference<UserDao> userDao = new AtomicReference<>(new UserDao());
    private Long userId;
    private final List<FileItem> fileItems = new ArrayList<>();

    public UserIdAndItsFileItems(List<FileItem> items) throws NumberFormatException {
        for (FileItem item : items) {
            if (item.isFormField()) {
                String itemText = item.getString();
                if (USER_ID_PARAMETER_NAME.equals(item.getFieldName())) {
                    setUserId(itemText);
                }
            } else {
                fileItems.add(item);
            }
        }
    }

    /**
     *
     * @param pathToContentDirectory directory to save files to
     * @return message about all saved files
     * @throws Exception
     */
    public String saveAllUserFiles(File pathToContentDirectory) throws Exception {
        validateSaveConditionsOrThrow();
        StringBuilder message = new StringBuilder();
        for (FileItem item : fileItems) {
            UserFile userFile = createUserFileEntry(userId, item);
            fileDao.get().add(userFile);
            File fileToSave = new File(pathToContentDirectory, userFile.getServerFileName());
            item.write(fileToSave);

            message.append(String.format("File named: %s was saved for user ID: %d\n",userFile.getFileName(),userFile.getUser().getId()));
        }
        return message.toString();
    }

    private void validateSaveConditionsOrThrow() {
        if (isUserIdNull()) {
            throw new IllegalArgumentException(NO_USER_ID_MSG);
        }
        if(fileItems.isEmpty()){
            throw new IllegalArgumentException(NO_FILE_PROVIDED);
        }
    }

    private UserFile createUserFileEntry(Long userId, FileItem item) throws IllegalArgumentException {
        Optional<User> foundUser = userDao.get().get(userId);
        return new UserFile(item.getName(), foundUser.orElseThrow(() -> new IllegalArgumentException(NO_USER_WITH_THIS_ID_MSG)));
    }

    public boolean isUserIdNull() {
        return userId == null;
    }

    public void setUserId(String userId) throws NumberFormatException {
        this.userId = Long.parseLong(userId);
    }
}
