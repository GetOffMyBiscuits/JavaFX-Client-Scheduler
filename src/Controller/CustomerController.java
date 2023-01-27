package Controller;

import Database.*;
import Helper.Errors;
import Helper.JDBC;
import Main.Main;
import Model.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * <b>This class is the controller for adding and updating customers</b>
 */
public class CustomerController implements Initializable {
    @FXML private Label updateLabel;
    @FXML private Button cancel;
    @FXML private TextField nameTxt, addressTxt, phoneTxt, postalTxt;
    @FXML private ComboBox <String> countryCB, stateCB;
    public static boolean update = false;
    public static Customer selectedCustomer;

    /**Updates the label
     * @param label String
     */
    public void updateLabel(String label) { updateLabel.setText(label); }

    /**Pre-populates the form with the current customer data
     * to make it easier for users to update
     * @param selected customer data
     * @throws SQLException
     */
    public void modifyText(Customer selected) throws SQLException {
        nameTxt.setText(selected.getCustomerName());
        addressTxt.setText(selected.getAddress());
        phoneTxt.setText(selected.getPhone());
        postalTxt.setText(selected.getPostalCode());
        countryCB.setValue(DBDivisions.retrieveCountryDivision(selected.getDivision_name()));
        stateCB.setValue(selected.getDivision_name());
    }

    /**Gets Division or 'State' data based on whatever
     * Country is selected in the combo box
     * @throws SQLException
     */
    @FXML public void onCountrySelected() throws SQLException {
        String currentCountry = countryCB.getValue();
        int id = DBCountries.retrieveID(currentCountry);
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT distinct * FROM first_level_divisions WHERE Country_ID in ('" + id + "')  ";
        ResultSet result = statement.executeQuery(sqlStatement);
        stateCB.getItems().clear();

        while (result.next()){
            stateCB.getItems().add(result.getString(2));
        }
    }

    /**Returns user to the main page from the customer form
     * @throws IOException
     */
    @FXML public void cancelButton() throws IOException {
        Main.mainScreen(cancel);
    }

    /**Handles saving customers to the database, and updating customers
     * in the database
     * @throws SQLException
     * @throws IOException
     */
    @FXML public void onSave() throws SQLException, IOException {
        int id = DBCustomers.createID();
        String name = nameTxt.getText();
        String address = addressTxt.getText();
        String phone = phoneTxt.getText();
        String postal = postalTxt.getText();
        String country = countryCB.getValue();
        String state = stateCB.getValue();

        Statement statement = JDBC.connection.createStatement();
        String sqlStatement;
        if (nameTxt.getText() == null || addressTxt.getText() == null || phoneTxt.getText() == null ||
        postalTxt.getText() == null || countryCB.getValue() == null || stateCB.getValue() == null){
            Errors.inputValidation(
                    "Error",
                    "The form is not completely filled in!",
                    "Please fill out all fields remember to use the calendar date picker to select " +
                            "a date AND use the Hour and Minute dropdown to set hours and minutes before saving");
        }else {
            if (!update) {
                sqlStatement = "INSERT into customers (Customer_ID, Customer_Name, Address, Phone, Postal_Code, Division_ID)" +
                        "values" + "('" + id + "', '" + name + "','" + address + "','" + phone + "','" + postal +
                        "','" + DBDivisions.retrieveDivisionID(state) + "')";
            } else {
                sqlStatement = "UPDATE customers SET Customer_ID = '" + MainController.selectedCustomerID +
                        "', Customer_Name = '" + name +
                        "', Address = '" + address +
                        "', Phone = '" + phone +
                        "', Postal_Code = '" + postal +
                        "', Division_ID = '" + DBDivisions.retrieveDivisionID(state) +
                        "' WHERE Customer_ID = '" + MainController.selectedCustomerID + "'";
            }
            statement.executeUpdate(sqlStatement);
            Main.mainScreen(cancel);
        }
    }

    /**Populates the combo-boxes with the correct data
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(update){
            try {
                modifyText(selectedCustomer);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            String currentCountry = countryCB.getValue();
            int id = DBCountries.retrieveID(currentCountry);

            Statement statement = JDBC.connection.createStatement();
            Statement statement2 = JDBC.connection.createStatement();
            String sqlStatement = "SELECT distinct * FROM countries";
            String sqlStatement2 = "SELECT distinct * FROM first_level_divisions WHERE Country_ID = "+ id;
            ResultSet result = statement.executeQuery(sqlStatement);
            ResultSet result2 = statement2.executeQuery(sqlStatement2);

            while (result.next()) {
                countryCB.getItems().add(result.getString(2));
            }
            while (result2.next()){
                stateCB.getItems().add(result2.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
