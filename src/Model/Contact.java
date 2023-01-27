package Model;

/**
 * <b>Class for creating the contact model, can be upgraded in the future</b>
 */
public class Contact {
    private int contactID;
    private String contactName, email;


    /**Contact constructor
     * @param contactName string contact name
     * @param email string email
     */
    public Contact(String contactName, String email) {
        this.contactName = contactName;
        this.email = email;
    }

    /**Constructor for contact, includes id
     * @param contactID int contact id
     * @param contactName string contact name
     * @param email string email
     */
    public Contact(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * Getters for the contact class
     */
    public int getContactID() {
        return contactID;
    }
    public String getContactName() {
        return contactName;
    }
}
