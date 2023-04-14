package org.academiadecodigo.wordsgame.database;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;
import org.academiadecodigo.wordsgame.entities.database.*;

@Getter
@Setter
public class Database {

    private static volatile Database instance;
    private Properties props;
    private Connection connection;
    private String env;
    private DatabaseEnvData dataBaseData;

    private Database() {
        this.props = new Properties();
        env = ResourceBundle.getBundle("application").getString("env");
    }

    /**
     * Returns the single instance of the Database class, creating it if necessary.
     * This method uses double-checked locking to ensure that only one instance of
     * the class is created in a multiThreaded environment.
     *
     * @return the single instance of the Database class
     * @throws SQLException if an error occurs while creating the database connection
     */
    public static Database getInstance() throws SQLException {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    /**
     * Starts the database connection using the current environment file.
     *
     */
    public void startDb() {
        dataBaseData = setVarsFromCurrentEnvFile();
        connect();
    }

    /**
     * Loads the properties from the environment file and returns the corresponding
     * `DatabaseEnvData` object.
     *
     * @return the `DatabaseEnvData` object containing the database connection information
     */
    public DatabaseEnvData setVarsFromCurrentEnvFile() {
        return new DatabaseEnvData(
            getFileVariable("db.databaseSetupFilePath"),
            getFileVariable("db.completeUrl"),
            getFileVariable("db.url"),
            getFileVariable("db.username"),
            getFileVariable("db.password"),
            getFileVariable("db.name"),
            getFileVariable("db.inGameRootUser"),
            getFileVariable("db.inGameRootPass")
        );
    }

    private String getFileVariable(String variable) {
        return ResourceBundle.getBundle("application-" + env).getString(variable);
    }

    /**
     * Connects to the database using the connection information stored in the
     * `dataBaseData` field.
     *
     */
    private void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    //If already exists
                    connection = DriverManager
                            .getConnection(dataBaseData.completeUrl, dataBaseData.dbRoot, dataBaseData.dbRootPass);
                } catch (SQLException e) {
                    //If table or db do not exist yet
                    connection = DriverManager
                            .getConnection(dataBaseData.url, dataBaseData.dbRoot, dataBaseData.dbRootPass);
                    setupDbStructure();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets up the `users` table in the database if it does not already exist.
     *
     */
    public void setupDbStructure() {
        Arrays.stream(QueryType.values())
                .filter(query -> query != QueryType.QUERY_WORDS) // Exclude QUERY_WORDS enum value
                .forEach(this::setupDBStaticStructure);
    }

    private void setupDBStaticStructure(QueryType queryType) {
        // Read the XML file
        File xmlFile = new File(dataBaseData.databaseSetupFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            doc = dbFactory.newDocumentBuilder().parse(xmlFile);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        // Get the root element
        Element root = doc.getDocumentElement();

        // Get the list of queries
        NodeList queryList = root.getElementsByTagName(queryType.getParam());

        // Iterate through each query
        for (int i = 0; i < queryList.getLength(); i++) {
            Element queryElement = (Element) queryList.item(i);

            // Get the SQL query and its parameters from the XML file
            String sql = queryElement.getElementsByTagName("sql").item(0).getTextContent();
            NodeList paramList = queryElement.getElementsByTagName("param");

            // Prepare the parameters for the SQL query
            String[] params = new String[paramList.getLength()];
            for (int j = 0; j < paramList.getLength(); j++) {
                Element paramElement = (Element) paramList.item(j);
                String paramName = paramElement.getTextContent(); // dbName
                DataBaseVariablesType variableType = DataBaseVariablesType.valueOf(paramName);
                params[j] = variableType.getFieldValue(dataBaseData);
            }

            // Format the SQL query with the parameters
            String formattedSql = String.format(sql, (Object[]) params);

            // Execute the SQL query
            executeUpdate(formattedSql);
        }
    }

    /**
     * Executes a SQL query and returns the result set.
     *
     * @param query the SQL query to execute
     * @return the result set returned by the query, or null if an error occurs
     */
    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Unable to execute query: " + query);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a SQL update statement and returns the number of rows affected.
     *
     * @param query the SQL update statement to execute
     * @return the number of rows affected by the update, or -1 if an error occurs
     */
    public int executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Unable to execute update: " + query);
            e.printStackTrace();
            return -1;
        }
    }

    public void dropTable() throws SQLException {
        Statement queryStatement = connection.createStatement();
        queryStatement.executeUpdate(
                "DROP DATABASE IF EXISTS " + dataBaseData.dbName + ";"
        );
    }

    /**
     * Closes the database connection.
     */
    private void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to close database connection.");
            e.printStackTrace();
        }
    }

    /**
     * Deletes current instance of DataBase
     */
    public void closeInstance() {
        close();
        instance = null;
    }


}
