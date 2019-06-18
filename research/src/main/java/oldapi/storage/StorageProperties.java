package oldapi.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oldApi/storage")
@Data
public class StorageProperties {
    private String location =
            "/Users/evgenij/Documents/Проекты/Мои/projects/tutorials/oldapi.testheroku/src/main/java/com/test/oldapi.heroku/oldapi.testheroku/root";

}
