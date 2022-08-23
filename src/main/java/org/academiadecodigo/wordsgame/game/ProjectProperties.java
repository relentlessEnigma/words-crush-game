package org.academiadecodigo.wordsgame.game;

import java.util.HashMap;
import java.util.Map;

public class ProjectProperties {

    private Map<String, String> properties = new HashMap<>();
    private static ProjectProperties pp;

    private ProjectProperties() {
        setProperties();
    }

    public static ProjectProperties getInstance() {
        if(pp == null) {
            return pp = new ProjectProperties();
        }
        return pp;
    }

    private void setProperties(){
        properties.put("admin.name", "admin");
        properties.put("admin.password", "bullshit");
        properties.put("server.grid.rows.number", "7");
        properties.put("server.grid.score.0", "15");
        properties.put("server.grid.score.1", "50");
        properties.put("server.grid.score.2", "100");
        properties.put("server.grid.score.3", "150");
        properties.put("server.grid.score.4", "200");
        properties.put("server.grid.score.5", "300");
    }

    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }

}
