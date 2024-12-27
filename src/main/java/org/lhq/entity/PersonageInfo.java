package org.lhq.entity;


import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonageInfo {
    private String name;
    private LocalDate birthday;
    private String gender;
    private String occupation;
    private String imdbId;
    private String avatar;
    private String url;
    private String birthPlace;
    private String summary;
}
