package Database;

import Helper.JDBC;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <b>A class built with helper functions for communication with the server for
 * user data</b>
 */
public class DBUsers {
    /**Checks the database to see if the input username and password
     * exist there and returns true or false
     * @param userName username
     * @param password password
     * @return bool
     * @throws SQLException sqlexception
     */
    public static boolean isValidPassword(String userName, String password) throws SQLException {

        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT Password FROM users WHERE User_Name ='" + userName + "'";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            if (result.getString("Password").equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**Takes a username and returns its id
     * @param username username
     * @return int
     * @throws SQLException sqlexception
     */
    public static int getID(String username) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM users";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
            if(result.getString("User_Name").equals(username)){
                return result.getInt(1);
            }
        }
        return 0;
    }

    /** Function to retrieve the UserName from a User ID
     * @param id int User_ID
     * @return String
     * @throws SQLException sqlexception
     */
    public static String getUserName(int id) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM users";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
            if(result.getInt("User_ID") == id){
                return result.getString(2);
            }
        }
        return null;
    }
}
