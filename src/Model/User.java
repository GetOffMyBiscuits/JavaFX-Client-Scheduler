package Model;

/**
 * <b>Class creates the model for users, it is not used here but would be useful
 * for creating additional functionality to the application, such as user management</b>
 */
public class User {
    private int userID;
    private String userName, password;

    /** Constructor for user
     * @param userID userid
     * @param userName username
     * @param password password
     */
    public User(int userID, String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

}
