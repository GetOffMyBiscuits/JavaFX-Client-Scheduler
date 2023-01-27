package Controller;

import Database.DBUsers;
import Helper.Errors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Main.Main;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <b>This class is the controller for the login screen</b>
 */
public class LoginController implements Initializable {
    @FXML private Button loginButton;
    @FXML private TextField userIDTxt, passwordTxt;
    @FXML private Label timeLabel, timeZoneLabel, loginLabel, userLabel, passwordLabel;
    private String loginFailTitle, loginFailHeader, loginFailMain;

    /**Checks to see if the user is valid and moves to the main screen. It outputs an error if not
     * @throws IOException
     * @throws SQLException
     */
    @FXML public void onLogin() throws IOException, SQLException {
        String userID = userIDTxt.getText();
        String password = passwordTxt.getText();
        if(DBUsers.isValidPassword(userID, password)){
            Main.mainScreen(loginButton);
        } else {
            Errors.inputValidation(
                    loginFailTitle,
                    loginFailHeader,
                    loginFailMain);
        }
        logLoginAttempts(DBUsers.isValidPassword(userID, password));
    }

    /**Logs the login attempts and writes them to a file in the root folder
     * @param success checks to see if the username and password are valid
     * @throws IOException
     */
    private void logLoginAttempts(boolean success) throws IOException {
        File f = new File("login_activity.txt");
        ZonedDateTime time = ZonedDateTime.now();
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(f, true));
        printWriter.append("\n").append(time.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        printWriter.append("\nUSER: '").append(userIDTxt.getText()).append("' attempted a login");
        printWriter.append("\n" + "Login successful: ").append(String.valueOf(success));
        printWriter.append("\n");
        printWriter.close();
    }

    /**Used to get the resource bundle for the different language packs and
     *set up the label strings to link to those packs
     * @param url
     * @param resourceBundle resource bundle of languages, fr and en
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeLabel.setText(timeLabel.getText() + " " + ZoneId.systemDefault());
        ResourceBundle rb = ResourceBundle.getBundle("login", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")){
            loginLabel.setText(rb.getString("loginLabel"));
            userLabel.setText(rb.getString("username"));
            passwordLabel.setText(rb.getString("password"));
            timeLabel.setText(rb.getString("TimeZone"));
            loginButton.setText(rb.getString("login"));
            timeZoneLabel.setText(String.valueOf(ZoneId.systemDefault()));
            loginFailTitle = rb.getString("Title");
            loginFailHeader = rb.getString("Header");
            loginFailMain = rb.getString("Main");
        }
    }
}