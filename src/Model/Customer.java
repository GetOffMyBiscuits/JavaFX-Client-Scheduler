package Model;

import Database.DBDivisions;

import java.sql.SQLException;

/**
 * <b>Class that creates the customer model</b>
 */
public class Customer {
    private int customerID, divisionID;
    private String customerName, address, postalCode, phone, division_name;

    /**Constructor for customers
     * @param customerID customer id
     * @param customerName customer name
     * @param address address
     * @param postalCode postal code
     * @param phone phone
     * @param divisionID division id
     */
    public Customer(int customerID,
                    String customerName,
                    String address,
                    String postalCode,
                    String phone,
                    int divisionID){

        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    /**Constructor for customer that captures division names, customer names, and contact name
     * instead of id's
     * @param customerID customer id
     * @param customerName customer name
     * @param address address
     * @param postalCode postal code
     * @param phone phone
     * @param division_name division_name
     */
    public Customer(int customerID,
                    String customerName,
                    String address,
                    String postalCode,
                    String phone,
                    String division_name){

        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division_name = division_name;
    }


    /**
     * GETTERS for the customer class
     */
    public int getDivisionID(){ return divisionID; }
    public int getCustomerID(){ return customerID; }
    public String getCustomerName(){ return customerName; }
    public String getAddress(){ return address; }
    public String getPostalCode(){ return postalCode; }
    public String getPhone(){ return phone; }
    public String getDivision_name(){ return division_name; }


    /**
     * Setters for the customer class
     */
    public void setCustomerID(int customerID){ this.customerID = customerID; }
}
