package com.crowdo.p2pconnect.model.core;


import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 21/3/17.
 */
public class Member {

    @Json(name = "id")
    private long id;

    @Json(name = "email")
    private String email;

    @Json(name = "name")
    private String name;

    @Json(name = "phone_number")
    private String phoneNumber;

    @Json(name = "nationality")
    private String nationality;

    @Json(name = "date_of_birth")
    private String dateOfBirth;

    @Json(name = "address1")
    private String address1;

    @Json(name = "address2")
    private String address2;

    @Json(name = "city")
    private String city;

    @Json(name = "postal_code")
    private String postalCode;

    @Json(name = "country_of_residence")
    private String countryOfResidence;

    @Json(name = "locale_preference")
    private String localePreference;

    @Json(name = "session_country")
    private String sessionCountry;

    @Json(name = "is_banned")
    private boolean isBanned;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getLocalePreference() {
        return localePreference;
    }

    public void setLocalePreference(String localePreference) {
        this.localePreference = localePreference;
    }

    public String getSessionCountry() {
        return sessionCountry;
    }

    public void setSessionCountry(String sessionCountry) {
        this.sessionCountry = sessionCountry;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
