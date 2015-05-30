package com.allianz.hackrisk.hackrisk;

import java.io.Serializable;

/**
 * Created by devsupport on 30/05/2015.
 */
public class RiskObject implements Serializable {
    public String Header;
    public String Description;
    public String Location;
    public String Date;
    public Double Latitude;
    public Double Longitude;
    public String Category;
    public String StreetName;
}
