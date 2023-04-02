package org.academiadecodigo.wordsgame.database;

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
    String inGameRootUser;
    String inGameRootPass;

    public DatabaseEnvData(String completeUrl, String url, String dbRoot, String dbRootPass, String dbName, String inGameRootUser, String inGameRootPass) {
        this.completeUrl = completeUrl;
        this.url = url;
        this.dbRoot = dbRoot;
        this.dbRootPass = dbRootPass;
        this.dbName = dbName;
        this.inGameRootUser = inGameRootUser;
        this.inGameRootPass = inGameRootPass;
    }
}
