package com.allianz.hackrisk.hackrisk;


public class Location {

    private String latitude;

    private Street street;

    private String longitude;

    /**
     *
     * @return
     * The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The street
     */
    public Street getStreet() {
        return street;
    }

    /**
     *
     * @param street
     * The street
     */
    public void setStreet(Street street) {
        this.street = street;
    }

    /**
     *
     * @return
     * The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}