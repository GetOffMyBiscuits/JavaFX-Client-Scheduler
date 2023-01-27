package Database;

import Helper.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <b>A class built with helper functions for communication with the server for
 * division data</b>
 */
public class DBDivisions {
    /**Takes a division name and returns its id
     * @param username Division name
     * @return int id
     * @throws SQLException sqlexception
     */
    public static int retrieveDivisionID(String username) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM first_level_divisions";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
            if(result.getString("Division").equals(username)){
                return result.getInt(1);
            }
        }
        return 0;
    }

    public static String retrieveDivisionName(int id) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM first_level_divisions";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
            if(result.getInt("Division_ID") == id){
                return result.getString(2);
            }
        }
        return null;
    }

    public static String retrieveCountry(int id) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct Country FROM first_level_divisions a " +
                "INNER JOIN countries b ON b.Country_ID = a.Country_ID " +
                "WHERE Division_ID = '" + id + "'";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
                return result.getString(1);
            }
        return null;
    }

    /**Takes a division name and returns a Country
     * @param name Division name
     * @return string
     * @throws SQLException sqlexception
     */
    public static String retrieveCountryDivision(String name) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct Country FROM first_level_divisions a " +
                "INNER JOIN countries b ON b.Country_ID = a.Country_ID " +
                "WHERE Division = '" + name + "'";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
            return result.getString(1);
        }
        return null;
    }
}
