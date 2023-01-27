package Model;

/**
 * Class for creating the Country model, can be upgraded in the future
 */
public class Country {
    private final int countryID;
    private final String countryName;

    /**Country constructor
     * @param countryID int country id
     * @param countryName string country name
     */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }


    /**
     * Getters for the Country class
     */
    public int getCountryId() {
        return countryID;
    }
    public String getCountryName() {
        return countryName;
    }
}
