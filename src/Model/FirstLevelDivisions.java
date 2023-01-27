package Model;

/**
 * <b>Class for creating the first-level-divisions model. Although it's not used yet it could also be used
 * to upgrade the apps functionality</b>
 */
public class FirstLevelDivisions {
    private String divisionName;
    private int divisionID, countryID;

    /**Constructor for Divisions
     * @param divisionID division id
     * @param divisionName division name
     * @param countryID country id
     */
    public FirstLevelDivisions(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    /**Getter for division name
     * @return division name
     */
    public String getDivisionName() {
        return divisionName;
    }
}
