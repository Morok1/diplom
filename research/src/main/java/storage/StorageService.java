package storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init() throws Exception;

    void store(MultipartFile file) throws EmptyStorageException;

    Stream<Path> loadAll() throws EmptyStorageException;

    Path load(String filename);

    Resource loadAsResource(String filename) throws Exception;

    void deleteAll();

}
