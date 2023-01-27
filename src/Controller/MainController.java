package Controller;

import Database.DBAppointments;
import Database.DBCustomers;
import Helper.Errors;
import Helper.JDBC;
import Main.Main;
import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * <b>This class is the controller for the main screen and the main tableviews</b>
 */
public class MainController implements Initializable {
    @FXML private RadioButton thisWeek, thisMonth, allTime;
    @FXML private Button addAppointmentsButton, addCustomersButton,
                         modifyCustomersButton, modifyAppointmentsButton,
                         reports, logoutButton;
    @FXML private TableColumn <Appointment, String> appTitle, appType, appDescription, appLocation;
    @FXML private TableColumn <Appointment, LocalDateTime> appStart, appEnd;
    @FXML private TableColumn <Appointment, Integer> appID, appCustomerID, appUserID, appContact;
    @FXML private TableView <Appointment> appTable;
    public static ObservableList<Appointment> olAppointment = FXCollections.observableArrayList();

    @FXML private TableColumn <Customer, String>
            customerName, customerAddress, customerPhone, customerState, customerPostal;
    @FXML private TableColumn <Customer, Integer> customerID;
    @FXML private TableView <Customer> customersTable;
    public static ObservableList<Customer> olCustomer = FXCollections.observableArrayList();
    public static int selectedID;
    public static int selectedCustomerID;
    private static boolean loggedin = false;

    /**Manages clicking the logout button
     * @throws IOException
     */
    //logout lambda
//    @FXML private void onLogoutClicked() throws IOException {
//                Main.logout(logoutButton);
//                loggedin = false;
//    }

    /**Query to receive all of the appointment data without any filter
     * @throws SQLException
     */
    @FXML public void onAllTime() throws SQLException {
        thisWeek.setSelected(false);
        thisMonth.setSelected(false);
        allTime.setDisable(true);
        thisWeek.setDisable(false);
        thisMonth.setDisable(false);

        olAppointment.clear();
        DBAppointments.queryAppointments();
        appTable.setItems(olAppointment);
    }

    /**Query to receive the appointment data within the current month
     * @throws SQLException
     */
    @FXML public void onThisMonth() throws SQLException {
        allTime.setSelected(false);
        thisWeek.setSelected(false);
        thisMonth.setDisable(true);
        thisWeek.setDisable(false);
        allTime.setDisable(false);

        olAppointment.clear();
        DBAppointments.queryMonthlyAppointments();
        appTable.setItems(olAppointment);
    }

    /**Query to receive appointment data within the current week
     * @throws SQLException
     */
    @FXML public void onThisWeek() throws SQLException {
        allTime.setSelected(false);
        thisMonth.setSelected(false);
        thisWeek.setDisable(true);
        thisMonth.setDisable(false);
        allTime.setDisable(false);

        olAppointment.clear();
        DBAppointments.queryWeeklyAppointments();
        appTable.setItems(olAppointment);
    }

    /**Button that takes users to the Appointment add form
     * @throws IOException
     */
    @FXML public void addAppointment() throws IOException {
        AppointmentsController.update = false;
        Main.ChangeScene("addAppointment", addAppointmentsButton);
    }

    /**Button to take users to the add customer form
     * @throws IOException
     */
    @FXML public void addCustomer() throws IOException {
        Main.ChangeScene("addCustomer", addCustomersButton);
    }

    /**Button to take users to the modify appointments form
     * @throws IOException
     */
    @FXML public void modifyAppointments() throws IOException {
        if(appTable.getSelectionModel().isEmpty()){
            Errors.inputValidation(
                    "No Selection!",
                    null,
                    "Please select an appointment record to modify!");
        }else{
            AppointmentsController.update = true;
            selectedID = appTable.getSelectionModel().getSelectedItem().getAppointmentID();
            AppointmentsController.selectedAppointment = appTable.getSelectionModel().getSelectedItem();
            Main.ChangeScene("modifyAppointments", modifyAppointmentsButton);
        }
    }

    /**Button to take users to the modify customer form
     * @throws IOException
     */
    @FXML public void modifyCustomer() throws IOException {
        if(customersTable.getSelectionModel().isEmpty()){
            Errors.inputValidation(
                    "No Selection!",
                    null,
                    "Please select a customer record to modify!");
        } else {
            CustomerController.update = true;
            selectedCustomerID = customersTable.getSelectionModel().getSelectedItem().getCustomerID();
            CustomerController.selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
            Main.ChangeScene("modifyCustomer", modifyCustomersButton);
        }
    }

    /**Button that manages deleting appointments, It includes a SQL statement that deletes
     * the appointment from the MySQL server
     * @throws SQLException
     */
    @FXML public void onDeleteAppointments() throws SQLException {
        if (appTable.getSelectionModel().isEmpty()){
            Errors.inputValidation(
                    "No Selection",
                    null,
                    "Please select an appointment to delete");
        } else {
            selectedID = appTable.getSelectionModel().getSelectedItem().getAppointmentID();
            String selectedType = appTable.getSelectionModel().getSelectedItem().getType();
            LocalDateTime selectedDate = appTable.getSelectionModel().getSelectedItem().getStart();

            Statement statement = JDBC.connection.createStatement();
            String sqlStatement =
                    "DELETE FROM appointments WHERE Appointment_ID = " + selectedID;
            statement.executeUpdate(sqlStatement);
            olAppointment.clear();
            DBAppointments.queryAppointments();
            appTable.setItems(olAppointment);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Appointment!");
            alert.setHeaderText(null);
            alert.setContentText(
                    "This " + selectedType + " Appointment, scheduled for " +
                            selectedDate + " has been deleted!");
            alert.showAndWait();
        }
    }

    /**Button that manages deleting customers, It includes a SQL statement that deletes
     * the appointment from the MySQL server
     * @throws SQLException
     */
    @FXML public void onDeleteCustomers() throws SQLException {
        if (customersTable.getSelectionModel().isEmpty()){
            Errors.inputValidation(
                    "No Selection",
                    null,
                    "Please select a customer to delete");
        } else {
            selectedCustomerID = customersTable.getSelectionModel().getSelectedItem().getCustomerID();
            Statement statement = JDBC.connection.createStatement();
            String sqlStatement =
                    "DELETE FROM customers WHERE Customer_ID = " + selectedCustomerID;
            statement.executeUpdate(sqlStatement);
            olCustomer.clear();
            DBCustomers.queryCustomers();
            customersTable.setItems(olCustomer);
        }
    }

    /**Manages the button click to take users to the reports page
     * @throws IOException
     */
    @FXML public void reportsButton() throws IOException {
        Main.ChangeScene("report", reports);
    }

    /**SQL statement that queries the server to check if
     * there is an upcoming appointment
     * @throws SQLException
     */
    public void upcomingAppointments() throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement =
                "select Appointment_ID, Start from client_schedule.appointments " +
                "where current_timestamp() between Start - INTERVAL 15 MINUTE and Start " +
                "or current_timestamp() between Start and End";
        ResultSet result = statement.executeQuery(sqlStatement);

        if (result.next()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Appointment!");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Appointment " + result.getInt("Appointment_ID") + ", scheduled for " +
                            result.getTimestamp("Start") + " is coming up!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Upcoming Appointments!");
            alert.setHeaderText(null);
            alert.setContentText(
                    "There are currently no upcoming appointments!");
            alert.showAndWait();
        }
    }

    /** Initializes the tableviews on the main page
     * Includes two <b>lambdas</b> used to simplify the code, overrides the start and end date columns
     * to reformat them appropriately
     * Another <b>lambda</b> is used to simply set up the logout button
     * @param url url
     * @param resourceBundle the resource bundle for language localization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        olCustomer.clear();
        olAppointment.clear();

        try {
            if (!loggedin){
                upcomingAppointments();
                loggedin = true;
            }
            DBAppointments.queryAppointments();
            DBCustomers.queryCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        customerState.setCellValueFactory(new PropertyValueFactory<>("division_name"));

        customersTable.setItems(olCustomer);

        appID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        appContact.setCellValueFactory(new PropertyValueFactory<>("contact_name"));
        appCustomerID.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        appUserID.setCellValueFactory(new PropertyValueFactory<>("user_name"));

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        //lambda function
        appStart.setCellFactory(i -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : formatter.format(item));
            }
        });
        //lambda function
        appEnd.setCellFactory(i -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : formatter.format(item));
            }
        });

        appTable.setItems(olAppointment);

        //third lambda function
        logoutButton.setOnAction(e -> {
            try {
                Main.logout(logoutButton);
                loggedin = false;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
