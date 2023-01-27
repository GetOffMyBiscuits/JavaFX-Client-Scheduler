package Controller;

import Database.DBAppointments;
import Database.DBContacts;
import Database.DBCustomers;
import Database.DBUsers;
import Helper.Errors;
import Helper.JDBC;
import Main.Main;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <b>This class is the controller for adding and updating appointments</b>
 */
public class AppointmentsController implements Initializable {
    @FXML Label updateLabel;
    @FXML Button cancel;
    @FXML TextField appID, title, type, description, loc;
    @FXML DatePicker startDate, endDate;
    @FXML ComboBox <String> startTime, endTime, startTimeMinutes, endTimeMinutes, customerID, userID, contact;
    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();
    public static Appointment selectedAppointment;
    public static boolean update = false;

    /**Changes the text on a label
     * @param label the label from FXML
     */
    public void updateLabel(String label) { updateLabel.setText(label); }

    /**Checks to see if appointments from the same user overlap
     * @return true or false
     * @throws SQLException SqlException
     */
    public boolean appointmentsOverlap() throws SQLException {
        //Check to see if appointments owned by the same user overlap
        //is greater than 8am EST and less than 10pm EST
        int userid = DBUsers.getID(userID.getValue());
        int customerid = DBCustomers.getID(customerID.getValue());
        int appointmentid = selectedAppointment.getAppointmentID();
        LocalDate lStartDate = startDate.getValue();
        String hourStart = startTime.getValue();
        String minuteStart = startTimeMinutes.getValue();
        LocalDate lEndDate = endDate.getValue();
        String hourEnd = endTime.getValue();
        String minuteEnd = endTimeMinutes.getValue();

        if (!update){
            appointmentid = 0;
        }

        ZonedDateTime utcZdtStart = Appointment.convertToUTC(lStartDate, hourStart, minuteStart);
        ZonedDateTime utcZdtEnd = Appointment.convertToUTC(lEndDate, hourEnd, minuteEnd);
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String start = customFormatter.format(LocalDateTime.of(utcZdtStart.toLocalDate(), utcZdtStart.toLocalTime()));
        String end = customFormatter.format(LocalDateTime.of(utcZdtEnd.toLocalDate(), utcZdtEnd.toLocalTime()));
        LocalTime morning = LocalTime.of(11, 59);
        LocalTime night = LocalTime.of(2, 1);

        Statement statement = JDBC.connection.createStatement();
        String sqlStatement =
                "SELECT distinct * FROM appointments a INNER JOIN " +
                        "customers b ON b.Customer_ID = a.Customer_ID INNER JOIN " +
                        "users c ON a.User_ID = c.User_ID INNER JOIN " +
                        "contacts d ON a.Contact_ID = d.Contact_ID " +
                        "WHERE a.Appointment_ID NOT IN (" + appointmentid + ") " +
                        " AND (a.User_ID = " + userid +
                        " OR b.Customer_ID = " + customerid +
                        ") AND (a.Start BETWEEN '" + utcZdtStart + "' AND '" + utcZdtEnd +
                        "' OR a.End BETWEEN '" + utcZdtStart + "' AND '" + utcZdtEnd + "' " +
                        " OR '" + utcZdtStart + "' BETWEEN a.Start AND a.End" +
                        " OR '" + utcZdtEnd + "' BETWEEN a.Start AND a.End)";

        ResultSet result = statement.executeQuery(sqlStatement);
        //if the query finds a result then there is an error
        if (result.next()){
            System.out.println("CONFLICTING APPOINTMENTS");
            System.out.println(appointmentid);
            System.out.println(result.getString("Start") + " " + result.getString("End"));
            if (result.getInt("Customer_ID") == customerid) {
                Errors.inputValidation(
                        "Scheduling Conflict",
                        "This appointment overlaps with another appointment for Customer: " +
                                DBCustomers.getCustomerName(customerid),
                        "Please adjust the scheduled time for this appointment " +
                                "to ensure there are no conflicting appointment times");
            }else {
                Errors.inputValidation(
                        "Scheduling Conflict",
                        "This appointment overlaps with another appointment for User: " +
                                DBUsers.getUserName(userid),
                        "Please adjust the scheduled time for this appointment " +
                                "to ensure there are no conflicting appointment times");
            }
            return true;
        } else if (utcZdtStart.isAfter(utcZdtEnd)){
            System.out.println("DATES ARE CROSSED");
            Errors.inputValidation(
                    "Time Error",
                    "The end date cannot come before the start date: " + userid,
                    "Please adjust the time or date so that the start comes first ");
            return true;
        } else if  (!Time.valueOf(LocalTime.parse(start)).after(Time.valueOf(morning)) &&
                    !Time.valueOf(LocalTime.parse(start)).before(Time.valueOf(night)) ||
                    !Time.valueOf(LocalTime.parse(end)).after(Time.valueOf(morning)) &&
                    !Time.valueOf(LocalTime.parse(end)).before(Time.valueOf(night))){
            Errors.inputValidation(
                    "Error",
                    "Appointments can't be made outside of work hours!",
                    "Please adjust the time so that it's within 8am and 10pm EST!");
            return true;
        }
        return false;
    }

    /**Allows the form to pre-populate with the appointment info
     * to make it easier for users to update
     * @param selected the selected appointment
     */
    public void modifyText(Appointment selected){
        title.setText(selected.getTitle());
        type.setText(selected.getType());
        description.setText(selected.getDescription());
        loc.setText(selected.getLocation());
        startDate.setValue(selected.getStart().toLocalDate());
        endDate.setValue(selected.getEnd().toLocalDate());
        startTime.setValue(String.valueOf(selected.getStart().toLocalTime().getHour()));
        startTimeMinutes.setValue(String.valueOf(selected.getStart().toLocalTime().getMinute()));
        endTime.setValue(String.valueOf(selected.getEnd().toLocalTime().getHour()));
        endTimeMinutes.setValue(String.valueOf(selected.getEnd().toLocalTime().getMinute()));
        customerID.setValue(String.valueOf(selected.getCustomer_name()));
        userID.setValue(String.valueOf(selected.getUser_name()));
        contact.setValue(String.valueOf(selected.getContact_name()));
    }

    /**Cancel button, that takes users back to the main page
     * @throws IOException io
     */
    @FXML public void cancelButton() throws IOException {
        Main.mainScreen(cancel);
    }

    /**Handles clicking the save button and adding an appointment to the database, or updating
     * an appointment in the database
     * @throws SQLException sqlexception
     * @throws IOException ioexception
     */
    @FXML public void onSave() throws SQLException, IOException {
        int appAppID = DBAppointments.createID();
        String appTitle = title.getText();
        String appType = type.getText();
        String appDesc = description.getText();
        String appLoc = loc.getText();
        int appCustomer = DBCustomers.getID(customerID.getValue());
        int appUser = DBUsers.getID(userID.getValue());
        int appContact = DBContacts.getID(contact.getValue());

        Statement statement = JDBC.connection.createStatement();
        String sqlStatement;

        //Ensure all fields are filled in to save an appointment
        if (startDate.getValue() == null || startTime.getValue() == null || startTimeMinutes.getValue() == null ||
            endDate.getValue() == null || endTime.getValue() == null || endTimeMinutes.getValue() == null ||
            title.getText() == null || type.getText() == null || description.getText() == null || loc.getText() == null ||
            customerID.getValue() == null || userID.getValue() == null || contact.getValue() == null) {

            Errors.inputValidation(
                    "Error",
                    "The form is not completely filled in!",
                    "Please fill out all fields remember to use the calendar date picker to select " +
                         "a date AND use the Hour and Minute dropdown to set hours and minutes before saving");

        } else if (!appointmentsOverlap()) { //checks to see if appointments overlap
            LocalDate lStartDate = startDate.getValue();
            String hourStart = startTime.getValue();
            String minuteStart = startTimeMinutes.getValue();

            LocalDate lEndDate = endDate.getValue();
            String hourEnd = endTime.getValue();
            String minuteEnd = endTimeMinutes.getValue();

            ZonedDateTime locZdtStart = Appointment.convertToLocZdt(lStartDate, hourStart, minuteStart);
            ZonedDateTime utcZdtStart = Appointment.convertToUTC(lStartDate, hourStart, minuteStart);
            ZonedDateTime locZdtEnd = Appointment.convertToLocZdt(lEndDate, hourEnd, minuteEnd);
            ZonedDateTime utcZdtEnd = Appointment.convertToUTC(lEndDate, hourEnd, minuteEnd);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            // make it look good in 24-hour format sortable by yyyy-MM-dd HH:mm:ss  (we are going to ignore fractions beyond seconds
            DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            alert.setTitle("Time and Date");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Locale.getDefault().toString:" + Locale.getDefault().toString()
                            + "\n\n"
                            + "ZoneOffset.systemDefault:" + ZoneOffset.systemDefault()
                            + "\n\n"
                            + "Local Date and Time:" + customFormatter.format(locZdtStart)
                            + "\n\n"
                            + "UTC Date and Time:" + customFormatter.format(utcZdtStart)
            );

            alert.showAndWait();

            if (!update) {
                sqlStatement = "INSERT into appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                        "values" +
                        " ('" + appAppID +
                        "','" + appTitle +
                        "','" + appDesc +
                        "','" + appType +
                        "','" + appLoc +
                        "','" + LocalDateTime.of(utcZdtStart.toLocalDate(), utcZdtStart.toLocalTime()) +
                        "','" + LocalDateTime.of(utcZdtEnd.toLocalDate(), utcZdtEnd.toLocalTime()) +
                        "','" + appCustomer +
                        "','" + appUser +
                        "','" + appContact + "')";
            }
            else {
                sqlStatement = "UPDATE appointments SET Appointment_ID = '" + MainController.selectedID +
                        "', Title = '" + appTitle +
                        "', Description = '" + appDesc +
                        "', Type = '" + appType +
                        "', Location = '" + appLoc +
                        "', Start = '" + LocalDateTime.of(utcZdtStart.toLocalDate(), utcZdtStart.toLocalTime()) +
                        "', End = '" + LocalDateTime.of(utcZdtEnd.toLocalDate(), utcZdtEnd.toLocalTime()) +
                        "', Contact_ID = '" + appContact +
                        "', Customer_ID = '" + appCustomer +
                        "', User_ID = '" + appUser +
                        "' WHERE Appointment_ID = '" + MainController.selectedID + "'";
            }
            statement.executeUpdate(sqlStatement);
            Main.mainScreen(cancel);
        }
    }

    /**Queries the server to populate the combo-boxes
     * @param url url
     * @param resourceBundle rb
     */
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        if(update){
            modifyText(selectedAppointment);
        } try {
            Statement statement = JDBC.connection.createStatement();
            Statement statement2 = JDBC.connection.createStatement();
            Statement statement3 = JDBC.connection.createStatement();
            String sqlStatement = "SELECT distinct * FROM customers";
            String sqlStatement2 = "SELECT distinct * FROM users";
            String sqlStatement3 = "SELECT distinct * FROM contacts";
            ResultSet result = statement.executeQuery(sqlStatement);
            ResultSet result2 = statement2.executeQuery(sqlStatement2);
            ResultSet result3 = statement3.executeQuery(sqlStatement3);

            while (result.next()) {
                customerID.getItems().add(result.getString(2));
            }
            while (result2.next()){
                userID.getItems().add(result2.getString(2));
            }
            while (result3.next()){
                contact.getItems().add(result3.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        hours.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("00", "15", "30", "45");
        startTime.setItems(hours);
        startTimeMinutes.setItems(minutes);
        endTime.setItems(hours);
        endTimeMinutes.setItems(minutes);
    }
}
