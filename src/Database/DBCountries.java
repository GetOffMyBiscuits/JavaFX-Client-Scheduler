package Database;

import Helper.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class built with helper functions for communication with the server for
 * Country data
 */
public class DBCountries {
    /** Input a country name and receive its id
     * @param username takes a country name
     * @return int id
     * @throws SQLException sqlexception
     */
    public static int retrieveID(String username) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM countries";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
            if(result.getString("Country").equals(username)){
                return result.getInt(1);
            }
        }
        return 0;
    }
}
