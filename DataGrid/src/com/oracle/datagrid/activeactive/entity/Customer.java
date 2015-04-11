package com.oracle.datagrid.activeactive.entity;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

import java.util.List;

public class Customer implements PortableObject{
    private Integer customerId;
    private Name customerName;
    private String customerPhone;
    private Integer customerAccountNumber;
    private Location customerLocation;
    private List<Device> customerDevices;
    private List<Service> customerServices;
    private Boolean dbmsPersist = true;

    public Customer() {}

    public Integer getCustomerId() throws IOException{
        return customerId;
    }
    
    public Name getCustomerName() {
        return customerName;
    }
    
    public String getCustomerFirstName() {
        return customerName.getFirstName();
    }
    public String getCustomerLastName() {
        return customerName.getLastName();
    }
    public String getCustomerPhone() {
        return customerPhone;
    }
    public Integer getCustomerAccountNumber() {
        return customerAccountNumber;
    }
    public Location getCustomerLocation() {
        return customerLocation;
    }
    public List<Device> getCustomerDevices() {
        return customerDevices;
    }
    public List<Service> getCustomerServices() {
        return customerServices;
    }
    public Boolean getDbmsPersist(){
        return dbmsPersist;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public void setCustomerName(Name customerName) {
        this.customerName = customerName;
    }
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    public void setCustomerAccountNumber(Integer customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }
    public void setCustomerLocation(Location customerLocation) {
        this.customerLocation = customerLocation;
    }
    public void setCustomerDevices(List<Device> customerDevices) {
        this.customerDevices = (List<Device>)customerDevices;
    }
    public void setCustomerServices(List<Service> customerServices) {
        this.customerServices = (List<Service>)customerServices;
    }
    public void setDbmsPersist (Boolean dbmsPersist){
        this.dbmsPersist = dbmsPersist;
    }
    
    public void readExternal(PofReader pofReader) throws IOException {
        pofReader.registerIdentity(this);
        customerId = pofReader.readInt(0);
        customerName = (Name) pofReader.readObject(1);
        customerPhone = pofReader.readString(2);
        customerAccountNumber = pofReader.readInt(3);
        customerLocation = (Location) pofReader.readObject(4);
        customerDevices = (List<Device>) pofReader.readObject(5);
        customerServices = (List<Service>) pofReader.readObject(6);
        dbmsPersist = pofReader.readBoolean(7);
    }
    
    public void writeExternal(PofWriter pofWriter) throws IOException {
        if(customerId != null){
            pofWriter.writeInt(0, customerId);
        }
        pofWriter.writeObject(1, customerName);
        pofWriter.writeString(2, customerPhone);
        pofWriter.writeInt(3, customerAccountNumber);
        pofWriter.writeObject(4, customerLocation);
        pofWriter.writeObject(5, customerDevices);
        pofWriter.writeObject(6, customerServices);
        pofWriter.writeObject(7, dbmsPersist);
    }
}
