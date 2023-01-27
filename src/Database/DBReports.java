package Database;

import Controller.ReportController;
import Helper.JDBC;
import Model.Reports;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <b>A class built with helper functions for communication with the server for
 * Reports</b>
 */
public class DBReports {
    /**Query to create report of total appointments by month and type
     * @throws SQLException sqlexception
     */
    public static void queryReport() throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement =
        "SELECT distinct  MONTHNAME(STR_TO_DATE(MONTH(start), '%m')) as 'Month', " +
        "count(Appointment_ID) as 'Total_Appointments', Type " +
        "FROM appointments a " +
        "INNER JOIN customers b ON b.Customer_ID = a.Customer_ID " +
        "INNER JOIN first_level_divisions c ON b.Division_ID = c.Division_ID " +
        "GROUP BY Month, Type";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            ReportController.olReports.add(new Reports
                    (result.getString("Type"),
                    result.getInt("Total_Appointments"),
                    result.getString("Month")));
        }
    }

    /**Creates a report of total customers by division or 'state'
     * @throws SQLException sqlexception
     */
    public static void queryDivisions() throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement =
                "SELECT distinct Division, count(a.Customer_ID) as 'Total_Customers' FROM customers a " +
                "INNER JOIN first_level_divisions b ON a.Division_ID = b.Division_ID Group by Division";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            ReportController.olDivisions.add(new Reports
                    (result.getString("Division"),
                     result.getInt("Total_Customers")));
        }
    }
}
