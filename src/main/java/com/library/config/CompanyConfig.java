package com.library.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CompanyConfig {

    @Value("${company.name}")
    private String companyName;

    @Value("${company.goal}")
    private String companyGoal;

    @Value("${company.preview}")
    private String companyPreview;
}
