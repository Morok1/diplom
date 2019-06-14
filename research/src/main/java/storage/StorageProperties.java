package storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
@Data
public class StorageProperties {
    private String location =
            "/Users/evgenij/Documents/Проекты/Мои/projects/tutorials/testheroku/src/main/java/com/test/heroku/testheroku/root";

}
