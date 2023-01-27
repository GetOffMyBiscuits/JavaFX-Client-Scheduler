package Database;

import Controller.AppointmentsController;
import Controller.MainController;
import Helper.JDBC;
import Model.Appointment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.*;

/**
 * <b>A class built with helper functions for communication with the server for
 * appointment data</b>
 */
public class DBAppointments {
    /**Get all appointments filtered by selected contact
     * @param id takes a selected id
     * @throws SQLException sqlexception
     */
    public static void queryAppointmentsFilter(int id) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement =
                "SELECT distinct * FROM appointments a INNER JOIN " +
                "customers b ON b.Customer_ID = a.Customer_ID INNER JOIN " +
                "first_level_divisions c ON b.Division_ID = c.Division_ID INNER JOIN " +
                "users d ON a.User_ID = d.User_ID INNER JOIN " +
                "contacts e ON e.Contact_ID = a.Contact_ID " +
                "WHERE a.Contact_ID = " + id;
                ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            Timestamp start = result.getTimestamp("Start"); // utc is offset 0; no offset required
            ZonedDateTime startTime = start.toLocalDateTime().atZone(ZoneId.systemDefault());
            Timestamp end = result.getTimestamp("End"); // utc is offset 0; no offset required
            ZonedDateTime endTime = end.toLocalDateTime().atZone(ZoneId.systemDefault());

            int appointmentid = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            String customername = result.getString("Customer_Name");
            String username = result.getString("User_Name");
            String contactname = result.getString("Contact_Name");

            MainController.olAppointment.add((new Appointment(
                    appointmentid,
                    title,
                    description,
                    location,
                    type,
                    startTime,
                    endTime,
                    customername,
                    username,
                    contactname)));
        }
    }

    /**Gets all appointments from the database
     * @throws SQLException sqlexception
     */
    public static void queryAppointments() throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM appointments a INNER JOIN " +
                "customers b ON b.Customer_ID = a.Customer_ID INNER JOIN " +
                "users c ON a.User_ID = c.User_ID INNER JOIN " +
                "contacts d ON a.Contact_ID = d.Contact_ID";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            int appointmentid = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            String customername = result.getString("Customer_Name");
            String username = result.getString("User_Name");
            String contactname = result.getString("Contact_Name");

            Timestamp start = result.getTimestamp("Start"); // utc is offset 0; no offset required
            Timestamp end = result.getTimestamp("End"); // utc is offset 0; no offset required
            ZonedDateTime startTime = start.toLocalDateTime().atZone(ZoneId.systemDefault());
            ZonedDateTime endTime = end.toLocalDateTime().atZone(ZoneId.systemDefault());

            MainController.olAppointment.add((new Appointment(
                    appointmentid,
                    title,
                    description,
                    location,
                    type,
                    startTime,
                    endTime,
                    customername,
                    contactname,
                    username)));
        }
    }

    /**Gets weekly appointments from the database
     * @throws SQLException sqlexception
     */
    public static void queryWeeklyAppointments() throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement =
                "SELECT distinct * FROM appointments a INNER JOIN " +
                        "customers b ON b.Customer_ID = a.Customer_ID INNER JOIN " +
                        "users c ON a.User_ID = c.User_ID INNER JOIN " +
                        "contacts d ON a.Contact_ID = d.Contact_ID WHERE YEARWEEK(start) = YEARWEEK(NOW())";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            int appointmentid = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            String customername = result.getString("Customer_Name");
            String username = result.getString("User_Name");
            String contactname = result.getString("Contact_Name");

            Timestamp start = result.getTimestamp("Start"); // utc is offset 0; no offset required
            Timestamp end = result.getTimestamp("End"); // utc is offset 0; no offset required
            ZonedDateTime startTime = start.toLocalDateTime().atZone(ZoneId.systemDefault());
            ZonedDateTime endTime = end.toLocalDateTime().atZone(ZoneId.systemDefault());

            MainController.olAppointment.add((new Appointment(
                    appointmentid,
                    title,
                    description,
                    location,
                    type,
                    startTime,
                    endTime,
                    customername,
                    contactname,
                    username)));
        }

    }

    /**Gets monthly appointments from the database
     * @throws SQLException sqlexception
     */
    public static void queryMonthlyAppointments() throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement =
                "SELECT distinct * FROM appointments a INNER JOIN " +
                        "customers b ON b.Customer_ID = a.Customer_ID INNER JOIN " +
                        "users c ON a.User_ID = c.User_ID INNER JOIN " +
                        "contacts d ON a.Contact_ID = d.Contact_ID WHERE MONTH(start) = MONTH(NOW()) AND YEAR(start) = YEAR(NOW())";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            int appointmentid = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            String customername = result.getString("Customer_Name");
            String username = result.getString("User_Name");
            String contactname = result.getString("Contact_Name");

            Timestamp start = result.getTimestamp("Start"); // utc is offset 0; no offset required
            Timestamp end = result.getTimestamp("End"); // utc is offset 0; no offset required
            ZonedDateTime startTime = start.toLocalDateTime().atZone(ZoneId.systemDefault());
            ZonedDateTime endTime = end.toLocalDateTime().atZone(ZoneId.systemDefault());

            MainController.olAppointment.add((new Appointment(
                    appointmentid,
                    title,
                    description,
                    location,
                    type,
                    startTime,
                    endTime,
                    customername,
                    contactname,
                    username)));
        }
    }

    /**auto generates an auto id, it also ensures that it's not already a number in the database
     * @return id
     * @throws SQLException sqlexception
     */
    public static int createID() throws SQLException {
        int id = 0;
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement =
                "SELECT distinct * FROM appointments";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            int highestID = result.getInt("Appointment_ID");
            System.out.println(highestID);
            if(result.getInt("Appointment_ID") >= highestID){
                id = highestID + 1;
            }

        }

        return id;
    }
}
