package com.example.sql_database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppInfoDTO {

    private String appName;
    private String appVersion;
    private String appEnvironment;

    public static AppInfoDTO info(String appEnvironment) {
        AppInfoDTO appInfoDTO = new AppInfoDTO();
        appInfoDTO.setAppEnvironment(appEnvironment);
        if (appEnvironment.equals("dev")) {
            appInfoDTO.setAppName("hogwarts-school");
            appInfoDTO.setAppVersion("0.0.1");
        }
        return appInfoDTO;
    }
}
