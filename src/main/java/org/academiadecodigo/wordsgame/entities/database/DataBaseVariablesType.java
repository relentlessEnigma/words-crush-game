package org.academiadecodigo.wordsgame.entities.database;

import org.academiadecodigo.wordsgame.database.DatabaseEnvData;
import java.util.function.Function;

public enum DataBaseVariablesType {
    COMPLETE_URL(DatabaseEnvData::getCompleteUrl),
    URL(DatabaseEnvData::getUrl),
    DB_ROOT(DatabaseEnvData::getDbRoot),
    DB_ROOT_PASS(DatabaseEnvData::getDbRootPass),
    DB_NAME(DatabaseEnvData::getDbName),
    DB_IN_GAME_ROOT_PASS(DatabaseEnvData::getInGameRootPass),
    DB_IN_GAME_ROOT_USER(DatabaseEnvData::getInGameRootUser),
    DB_IN_GAME_ROOT_ROLE(DatabaseEnvData::getRootRole),
    AVAILABLE_ROLES(DatabaseEnvData::getEnumRoles);

    private final Function<DatabaseEnvData, String> fieldAccessor;

    DataBaseVariablesType(Function<DatabaseEnvData, String> fieldAccessor) {
        this.fieldAccessor = fieldAccessor;
    }

    public String getFieldValue(DatabaseEnvData dbEnvData) {
        return fieldAccessor.apply(dbEnvData);
    }
}