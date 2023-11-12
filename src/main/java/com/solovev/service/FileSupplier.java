package com.solovev.service;

import com.solovev.dao.daoImplementations.UserFileDao;
import com.solovev.model.UserFile;
import javassist.bytecode.ByteArray;
import org.apache.commons.fileupload.FileUploadException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class FileSupplier {
    private static final String NO_SUCH_FILE_EXISTS = "File: %s not exist for user ID: %d";
    private final File directoryToSearch;
    private final AtomicReference<UserFileDao> userFileDao = new AtomicReference<>(new UserFileDao());

    public FileSupplier(File directoryToSearch) {
        this.directoryToSearch = directoryToSearch;
    }
    public byte[] getFile(long userId) throws IOException {
        ByteArrayOutputStream filesContent = new ByteArrayOutputStream();
        Collection<UserFile> foundFilesForUser = userFileDao.get().getByUser(userId);
        for(UserFile userFile : foundFilesForUser){
            filesContent.write(getFileBytes(userFile));
        }
        return filesContent.toByteArray();
    }
    public byte[] getFile(long userId,String fileName) throws IOException {
        Optional<UserFile> foundUserFile = userFileDao.get().getByNameAndUser(userId,fileName);
        Supplier<FileNotFoundException> noFileExist = () -> new FileNotFoundException(String.format(NO_SUCH_FILE_EXISTS,fileName,userId));
        UserFile userFile = foundUserFile.orElseThrow(noFileExist);
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
