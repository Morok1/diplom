package to_heroku.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MainValidator  implements Validator{

    @Override
    public void validate(Class<?> clazz, Set<BugReport> bugReports) {

    }
}
