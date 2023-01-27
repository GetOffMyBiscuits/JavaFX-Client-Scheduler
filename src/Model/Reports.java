package Model;

import java.util.Date;

/**
 * <b>Class that creates the model for Reports</b>
 */
public class Reports {
    private String type, divisionName, month;
    private int totalCustomers, totalAppointments;

    /** Constructor for the total appointments report
     * @param type type
     * @param totalAppointments total appointments
     * @param month month
     */
    public Reports(String type, int totalAppointments, String month){
        this.type = type;
        this.totalAppointments = totalAppointments;
        this.month = month;
    }

    /**Constructor for the total customers report
     * @param divisionName division name
     * @param totalCustomers total customers
     */
    public Reports(String divisionName, int totalCustomers){
        this.divisionName = divisionName;
        this.totalCustomers = totalCustomers;
    }

    /**These are the getters for this model, this one is for type
     * @return type
     */
    public String getType(){ return type; }
    public String getDivisionName(){ return divisionName; }
    public int getTotalAppointments(){ return totalAppointments; }
    public int getTotalCustomers(){ return totalCustomers; }
    public String getMonth(){ return month; }

}
