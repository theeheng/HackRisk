package com.allianz.hackrisk.hackrisk;


import java.util.ArrayList;

public class CrimeApiResult {


    private String category;

    private String locationType;

    private Location location;

    private String context;

    private OutcomeStatus outcomeStatus;

    private String persistentId;

    private Integer id;

    private String locationSubtype;

    private String month;

    /**
     *
     * @return
     * The category
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The locationType
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     *
     * @param locationType
     * The location_type
     */
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    /**
     *
     * @return
     * The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The context
     */
    public String getContext() {
        return context;
    }

    /**
     *
     * @param context
     * The context
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     *
     * @return
     * The outcomeStatus
     */
    public OutcomeStatus getOutcomeStatus() {
        return outcomeStatus;
    }

    /**
     *
     * @param outcomeStatus
     * The outcome_status
     */
    public void setOutcomeStatus(OutcomeStatus outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }

    /**
     *
     * @return
     * The persistentId
     */
    public String getPersistentId() {
        return persistentId;
    }

    /**
     *
     * @param persistentId
     * The persistent_id
     */
    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The locationSubtype
     */
    public String getLocationSubtype() {
        return locationSubtype;
    }

    /**
     *
     * @param locationSubtype
     * The location_subtype
     */
    public void setLocationSubtype(String locationSubtype) {
        this.locationSubtype = locationSubtype;
    }

    /**
     *
     * @return
     * The month
     */
    public String getMonth() {
        return month;
    }

    /**
     *
     * @param month
     * The month
     */
    public void setMonth(String month) {
        this.month = month;
    }

}
