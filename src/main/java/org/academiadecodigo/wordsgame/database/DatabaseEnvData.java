package org.academiadecodigo.wordsgame.database;

import lombok.Getter;
import lombok.Setter;
import org.academiadecodigo.wordsgame.entities.users.Role;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class DatabaseEnvData {
    String databaseSetupFilePath;
    String completeUrl;
    String url;
    String dbRoot;
    String dbRootPass;
    String dbName;
    String inGameRootUser;
    String inGameRootPass;
    String rootRole;
    String enumRoles;

    public DatabaseEnvData(String databaseSetupFilePath, String completeUrl, String url, String dbRoot, String dbRootPass, String dbName, String inGameRootUser, String inGameRootPass) {
        this.databaseSetupFilePath = databaseSetupFilePath;
        this.completeUrl = completeUrl;
        this.url = url;
        this.dbRoot = dbRoot;
        this.dbRootPass = dbRootPass;
        this.dbName = dbName;
        this.inGameRootUser = inGameRootUser;
        this.inGameRootPass = inGameRootPass;
        this.rootRole = Role.ROOT.name();
        this.enumRoles = this.getEnumRolesAsString();
    }

    private String getEnumRolesAsString() {
        return Stream.of(Role.values())
                .map(role -> "'" + role.name() + "'")
                .collect(Collectors.joining(", "));
    }
}
