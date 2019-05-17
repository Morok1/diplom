package to_heroku.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import to_heroku.model.BugReport;
import to_heroku.service.Integrator;

import java.util.Set;

@RestController
public class ResultController {

    @Autowired
    private Integrator integrator;

    @PostMapping("/classreports")
    public Set<BugReport>  bugReports(){
        return null;
    }
}
