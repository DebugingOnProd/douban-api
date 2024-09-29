package org.lhq.entity;

import lombok.Data;
import org.lhq.entity.enums.MovieRole;

@Data
public class CelebrityInfo {
    private String id;
    private String name;
    private MovieRole role;
    private String characterName;
    private String avatar;
    private String works;
    private String link;
}
