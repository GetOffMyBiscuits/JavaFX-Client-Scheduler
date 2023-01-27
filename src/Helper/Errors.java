package Helper;

import javafx.scene.control.Alert;

/**
 * Class to hold error text functions
 */
public class Errors {
    /**This function takes title, header, and main text input to simply create error messages
     * @param title title
     * @param header header
     * @param main main
     */
    public static void inputValidation(String title, String header, String main){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(main);

        alert.showAndWait();
    }
}
