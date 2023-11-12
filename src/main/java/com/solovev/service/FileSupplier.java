package com.solovev.service;

import com.solovev.dao.daoImplementations.UserFileDao;
import com.solovev.model.UserFile;
import org.apache.commons.fileupload.FileUploadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class FileSupplier {
    private final File directoryToSearch;
    private final AtomicReference<UserFileDao> userFileDao = new AtomicReference<>(new UserFileDao());

    public FileSupplier(File directoryToSearch) {
        this.directoryToSearch = directoryToSearch;
    }
    public byte[] getFile(long userId,String fileName) throws IOException {
        Optional<UserFile> foundUserFile = userFileDao.get().getByNameAndUser(userId,fileName);
        UserFile userFile = foundUserFile.orElseThrow(FileNotFoundException :: new);
        return getFileBytes(userFile);
    }
    private byte[] getFileBytes(UserFile userFile) throws IOException {
        Path gotFile = getFile(userFile);
        return Files.readAllBytes(gotFile);
    }
    private Path getFile(UserFile userFile) throws FileNotFoundException {
        Path resultFile = Path.of(directoryToSearch.toString(),userFile.getServerFileName());
        if(Files.notExists(resultFile)){
            throw new FileNotFoundException();
        }
        return resultFile;
    }
}
