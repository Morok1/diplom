package com.test.heroku.testheroku;

import com.test.heroku.testheroku.example0.GeneralUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicValidationController {
    @GetMapping("/basicvalidation")
    public BasicValidationDTO basicValidation() {
        Class clazz = Example0.class;

        boolean isAnnotation = GeneralUtil.isAnnotation.test(clazz);
        boolean isEnum = GeneralUtil.isEnum.test(clazz);
        boolean isInterface = GeneralUtil.isInterface.test(clazz);
        boolean isAbstract = GeneralUtil.isAbstract.test(clazz);

        BasicValidationDTO dto =
                new BasicValidationDTO(isAnnotation, isEnum, isInterface, isAbstract);

        return dto;
    }

    @Data
    @AllArgsConstructor
    public class BasicValidationDTO {
        private boolean isAnnotation;
        private boolean isEnum;
        private boolean isInterface;
        private boolean isAbstract;
    }

    public class Example0{

    }

}
