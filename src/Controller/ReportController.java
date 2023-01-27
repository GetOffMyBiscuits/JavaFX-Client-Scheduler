package Controller;

import Database.DBAppointments;
import Database.DBContacts;
import Database.DBReports;
import Helper.JDBC;
import Main.Main;
import Model.Appointment;
import Model.Reports;
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
import java.util.Date;
import java.util.ResourceBundle;

/**
 * <b>This class is the controller for the Reports page</b>
 */
public class ReportController implements Initializable {
    @FXML private Button back;
    @FXML private ComboBox <String> contactCB;
    @FXML private TableView <Reports> reportTable;
    @FXML private TableView <Reports> divisionTable;
    @FXML private TableColumn <Reports, Date> monthColumn;
    @FXML private TableColumn <Reports, Integer> totalAppColumn, totalCustomersColumn;
    @FXML private TableColumn <Reports, String> reportTypeColumn, divisionNameColumn;

    @FXML private TableView <Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Integer> idColumn, customerColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> startColumn, endColumn;
    @FXML private TableColumn<Appointment, String> titleColumn, typeColumn, descriptionColumn, locationColumn;
    public static ObservableList<Reports> olReports = FXCollections.observableArrayList();
    public static ObservableList<Reports> olDivisions = FXCollections.observableArrayList();

    /**Takes users back to the main page from the reports page
     * @throws IOException
     */
    @FXML public void backButton() throws IOException {
        Main.mainScreen(back);
    }

    /**Populates the tables with the correct data depending on the
     * customer selected in the combo-box. Includes two <b>lambdas</b>
     * used to simplify the code, overrides the start and end date columns
     * to reformat them appropriately
     * @throws SQLException
     */
    @FXML public void onContactSelected() throws SQLException {
        String contact_name = contactCB.getValue();
        int id = DBContacts.getID(contact_name);

        MainController.olAppointment.clear();

        try {
            DBAppointments.queryAppointmentsFilter(id);
            DBReports.queryReport();
            DBReports.queryDivisions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer_name"));

        appointmentTable.setItems(MainController.olAppointment);

        //Two overrides for formatting the Date and Time
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        //format start date column
        //lambda
        startColumn.setCellFactory(i -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : formatter.format(item));
            }
        });
        //format end date column
        //lamdba
        endColumn.setCellFactory(i -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : formatter.format(item));
            }
        });


    }
    /**Queries the database to populate customers into the combo box
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        olReports.clear();
        olDivisions.clear();

        try {
            DBReports.queryDivisions();
            DBReports.queryReport();
            Statement statement = JDBC.connection.createStatement();
            String sqlStatement = "SELECT distinct * FROM contacts";
            ResultSet result = statement.executeQuery(sqlStatement);
            while (result.next()) {
                contactCB.getItems().add(result.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        totalAppColumn.setCellValueFactory(new PropertyValueFactory<>("totalAppointments"));
        reportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        reportTable.setItems(olReports);

        divisionNameColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        totalCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("totalCustomers"));

        divisionTable.setItems(olDivisions);
    }
}
