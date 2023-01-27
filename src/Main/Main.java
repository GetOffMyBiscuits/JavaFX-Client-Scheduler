package Main;

import Controller.AppointmentsController;
import Controller.CustomerController;
import Controller.ReportController;
import Helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * <b>This class is for running the application and setting up the window</b>
 */
public class Main extends Application {

    /**Sets up the window and its dimensions, as well as link to the FXML file
     * @param stage stage
     * @throws IOException ioexception
     */
    @Override public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../View/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 356, 370);
        stage.setTitle("Appointment Scheduler");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    /**Launches the application and also sets up our JDBC database connection to our local MySQL server
     * @param args args
     */
    public static void main(String[] args) {
        //Locale.setDefault(new Locale("fr"));
        JDBC.openConnection();
        launch();
    }

    /**Function to switch from the login screen to the main screen
     * @param button requires a button reference
     * @throws IOException ioexception
     */
    public static void mainScreen (Button button) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader mainLoad = new FXMLLoader(Main.class.getResource("../View/Main.fxml"));
        Pane mainPane = mainLoad.load();
        stage.setScene(new Scene(mainPane));
    }

    /**Takes us back to the login screen and sets logged-in to false
     * @param button Button reference
     * @throws IOException ioexception
     */
    public static void logout (Button button) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader mainLoad = new FXMLLoader(Main.class.getResource("../View/Login.fxml"));
        Pane mainPane = mainLoad.load();
        stage.setScene(new Scene(mainPane));
    }

    /**This is a switch statement to handle switching between different scenes:
     * Appointments, Customers and Reports
     * @param form the scene name
     * @param button reference
     * @throws IOException ioexception
     */
    public static void ChangeScene(String form, Button button) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();

        //part
        FXMLLoader appLoad =  new FXMLLoader(Main.class.getResource("../View/Appointments.fxml"));
        Pane appPane = appLoad.load();
        AppointmentsController appC = appLoad.getController();

        //product
        FXMLLoader customerLoad =  new FXMLLoader(Main.class.getResource("../View/Customer.fxml"));
        Pane customerPane = customerLoad.load();
        CustomerController customerC = customerLoad.getController();

        FXMLLoader reportLoad =  new FXMLLoader(Main.class.getResource("../View/Reports.fxml"));
        Pane reportPane = reportLoad.load();
        ReportController reportC = reportLoad.getController();

        switch (form) {
            case "addCustomer" -> {
                customerC.updateLabel("Add Customer");
                //CustomerController.modify = false;
                stage.setScene(new Scene(customerPane));
            }
            case "addAppointment" -> {
                //appC.modify = false;
                appC.updateLabel("Add Appointment");
                stage.setScene(new Scene(appPane));
            }
            case "modifyCustomer" -> {
                customerC.updateLabel("Modify Customer");
                //CustomerController.modify = false;
                stage.setScene(new Scene(customerPane));
            }
            case "modifyAppointments" -> {
                //appC.modify = false;
                appC.updateLabel("Modify Appointment");
                stage.setScene(new Scene(appPane));
            }
            case "report" -> {
                stage.setScene(new Scene(reportPane));
            }
        }
    }

}