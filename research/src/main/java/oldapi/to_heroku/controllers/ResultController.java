package oldapi.to_heroku.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import oldapi.to_heroku.model.BugReport;

import java.util.Set;

@RestController
public class ResultController {


    @PostMapping("/classreports")
    public Set<BugReport>  bugReports(){
        return null;
    }
}
