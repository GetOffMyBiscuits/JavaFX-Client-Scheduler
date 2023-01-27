package Database;

import Helper.JDBC;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class built with helper functions for communication with the server for
 * Contacts
 */
public class DBContacts {
    /**Input a contact username and receive its id
     * @param username takes a contact username
     * @return int id
     * @throws SQLException sqlexception
     */
    public static int getID(String username) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM contacts";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
            if(result.getString("Contact_Name").equals(username)){
                return result.getInt(1);
            }
        }
        return 0;
    }
}
