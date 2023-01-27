package Database;

import Controller.MainController;
import Helper.JDBC;
import Model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <b>A class built with helper functions for communication with the server for
 * customer data</b>
 */
public class DBCustomers {

    /**Gets all customer and division data via inner join
     * @throws SQLException sqlexception
     */
    public static void queryCustomers() throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM customers a " +
                "INNER JOIN first_level_divisions b ON a.Division_ID = b.Division_ID";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            MainController.olCustomer.add(new Customer
                    (result.getInt("Customer_ID"),
                    result.getString("Customer_Name"),
                    result.getString("Address"),
                    result.getString("Postal_Code"),
                    result.getString("Phone"),
                    result.getString("Division")));
        }
    }

    /**Takes a customer name and returns its id
     * @param username Customer name
     * @return int id
     * @throws SQLException sqlexception
     */
    public static int getID(String username) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM customers";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
            if(result.getString("Customer_Name").equals(username)){
                return result.getInt(1);
            }
        }
        return 0;
    }

    /** Function to retrieve the Customer Name from a Customer ID
     * @param id int Customer_ID
     * @return String Customer
     * @throws SQLException sqlexception
     */
    public static String getCustomerName(int id) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM customers";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()){
            if(result.getInt("Customer_ID") == id){
                return result.getString(2);
            }
        }
        return null;
    }

    /**an id number, generated to be unique every time
     * @return an id number
     * @throws SQLException sqlexception
     */
    public static int createID() throws SQLException {
        int id = 0;
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement =
                "SELECT distinct * FROM customers";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            int highestID = result.getInt("Customer_ID");
            System.out.println(highestID);
            if(result.getInt("Customer_ID") >= highestID){
                id = highestID + 1;
            }
        }
        return id;
    }
}


