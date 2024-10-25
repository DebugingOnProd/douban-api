package org.lhq.entity.enums;

public enum MovieRole {
    ACTOR("主演"),
    DIRECTOR("导演"),
    CAST("演员"),
    VOICE("配音"),
    WRITER("编剧"),
    PRODUCER("制片人"),
    EDITOR("剪辑"),
    COSTUME("服装"),
    OTHER("其他");

    private final String name;

    MovieRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
