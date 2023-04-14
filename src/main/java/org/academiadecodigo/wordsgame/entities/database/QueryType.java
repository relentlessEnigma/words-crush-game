package org.academiadecodigo.wordsgame.entities.database;

import lombok.Getter;

@Getter
public enum QueryType {
    QUERY_DB("query-db"),
    QUERY_USERS("query-users"),
    QUERY_WORDS("query-words");

    String param;

    QueryType(String param) {
        this.param = param;
    }
}
