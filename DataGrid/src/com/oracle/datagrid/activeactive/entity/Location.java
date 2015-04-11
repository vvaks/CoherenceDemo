package com.oracle.datagrid.activeactive.entity;


import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

public class Location implements PortableObject {
    private Integer locationId;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Customer customer;
    
    public Location() {}

    public Integer getLocationId() {
        return locationId;
    }

    public String getAdress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }
    public Customer getCustomer() {
        return customer;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public void setAdress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void readExternal(PofReader pofReader) throws IOException {
        pofReader.registerIdentity(this);
        locationId = pofReader.readInt(0);
        address = pofReader.readString(1);
        city = pofReader.readString(2);
        state = pofReader.readString(3);
        postalCode = pofReader.readString(4);
        country = pofReader.readString(5);
        customer = (Customer) pofReader.readObject(6);   
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        if(locationId != null){
            pofWriter.writeInt(0, locationId);
        }
        pofWriter.writeString(1, address);
        pofWriter.writeString(2, city);
        pofWriter.writeString(3, state);
        pofWriter.writeString(4, postalCode);
        pofWriter.writeString(5, country);
        pofWriter.writeObject(6, customer);
    }
}