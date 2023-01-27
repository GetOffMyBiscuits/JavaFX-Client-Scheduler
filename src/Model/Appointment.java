package Model;

import java.time.*;

/**
 * <b>Class for creating the appointment model</b>
 */
public class Appointment {
    private int appointmentID, customerID, contactID, userID;
    private String title, description, location, type, customer,
            user, contact, customer_name, contact_name, user_name;
    private LocalDateTime start, end;

    /**Constructor for appointments
     * @param appointmentID int appointment id
     * @param title string title
     * @param description string description
     * @param location string location
     * @param type string type
     * @param start local date time start
     * @param end local date time end
     * @param customerID int customer id
     * @param contactID int contact id
     * @param userID int user id
     */
    public Appointment(int appointmentID, String title, String description, String location, String type,
                       LocalDateTime start, LocalDateTime end,
                       int customerID, int contactID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.contactID = contactID;
        this.userID = userID;
    }

    /**Constructor for appointments with zoneddatetimes instead of localdatetimes and
     * names instead of id's for customer, contact, and user
     * @param appointmentID int id
     * @param title string title
     * @param description string description
     * @param location string location
     * @param type string type
     * @param start zoneddatetime start
     * @param end zoneddatetime end
     * @param customer_name string customer name
     * @param contact_name string contact name
     * @param user_name string user name
     */
    public Appointment(int appointmentID, String title, String description, String location, String type,
                       ZonedDateTime start, ZonedDateTime end,
                       String customer_name, String contact_name, String user_name) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = LocalDateTime.from(start);
        this.end = LocalDateTime.from(end);
        this.customer_name = customer_name;
        this.contact_name = contact_name;
        this.user_name = user_name;
    }

    /**
     * Getters for the appointment class
     */
    public String getDescription(){ return description; }
    public String getCustomer_name(){ return customer_name; }
    public String getContact_name(){ return contact_name; }
    public String getUser_name(){ return user_name; }
    public int getAppointmentID(){ return appointmentID; }
    public String getTitle(){ return title; }
    public String getLocation(){ return location; }
    public String getType(){ return type; }
    public LocalDateTime getStart(){ return start; }
    public LocalDateTime getEnd(){ return end; }
    public String getUser(){ return user; }
    public String getCustomer(){ return customer; }
    public int getCustomerID(){ return customerID; }
    public String getContact(){ return contact; }
    public int getContactID(){ return contactID; }
    public int getUserID(){ return userID; }

    /**
     * Setters for the appointment class
     */
    //Setters
    public void setLocation(String location){ this.location = location; }
    public void setType(String type){ this.type = type; }
    public void setStart(LocalDateTime start){ this.start = start;}
    public void setCustomer(String customer){ this.customer = customer;}
    public void setAppointmentID(int appointmentID){ this.appointmentID = appointmentID; }
    public void setTitle(String title){ this.title = title; }
    public void setEnd(LocalDateTime end){ this.end = end;}
    public void setUser(String user){ this.user = user; }
    public void setContact(String contact){ this.contact = contact; }

    /** handles conversion of localdate time to a local zoneddatetime
     * and also combines hr and minutes with date
     * @param ldt localdatetime
     * @param hr hour
     * @param min minute
     * @return returns a zoned date time
     */
    public static ZonedDateTime convertToLocZdt(LocalDate ldt, String hr, String min){
        // obtain the LocalDateTime
        LocalDateTime ldtStart = LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), Integer.parseInt(hr), Integer.parseInt(min));
        // obtain the ZonedDateTime version of LocalDateTime
        return ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
    }

    /**Converts a LocalDate, Hour string, and Minute string to UTC ZonedDateTime
     * @param ldt LocalDateTime
     * @param hr hour
     * @param min minute
     * @return ZonedDateTime
     */
    public static ZonedDateTime convertToUTC(LocalDate ldt, String hr, String min){
        // obtain the LocalDateTime
        LocalDateTime ldtStart = LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), Integer.parseInt(hr), Integer.parseInt(min));
        // obtain the ZonedDateTime version of LocalDateTime
        ZonedDateTime locZdtStart = ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
        // obtain the UTC ZonedDateTime of the ZonedDateTime version of LocalDateTime
        return locZdtStart.withZoneSameInstant(ZoneOffset.UTC);
    }
}
