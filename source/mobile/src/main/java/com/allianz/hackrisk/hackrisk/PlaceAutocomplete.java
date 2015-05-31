package com.allianz.hackrisk.hackrisk;

/**
 * Created by devsupport on 30/05/2015.
 */
/**
 * Holder for Places Geo Data Autocomplete API results.
 */
public class PlaceAutocomplete {

    public CharSequence placeId;
    public CharSequence description;

    PlaceAutocomplete(CharSequence placeId, CharSequence description) {
        this.placeId = placeId;
        this.description = description;
    }

    @Override
    public String toString() {
        return description.toString();
    }
}
