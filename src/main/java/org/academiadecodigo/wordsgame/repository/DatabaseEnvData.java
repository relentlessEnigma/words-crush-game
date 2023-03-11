package org.academiadecodigo.wordsgame.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseEnvData {
    String completeUrl;
    String url;
    String dbRoot;
    String dbRootPass;
    String dbName;
    String gameRoot;
    String gameRootPass;

    public DatabaseEnvData(String completeUrl, String url, String dbRoot, String dbRootPass, String dbName, String gameRoot, String gameRootPass) {
        this.completeUrl = completeUrl;
        this.url = url;
        this.dbRoot = dbRoot;
        this.dbRootPass = dbRootPass;
        this.dbName = dbName;
        this.gameRoot = gameRoot;
        this.gameRootPass = gameRootPass;
    }
}
