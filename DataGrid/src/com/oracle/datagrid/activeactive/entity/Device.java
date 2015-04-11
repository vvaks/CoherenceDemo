package com.oracle.datagrid.activeactive.entity;


import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

public class Device implements PortableObject {
    private Integer deviceId;
    private String deviceType;
    private String deviceSerialNumber;
    private String deviceStatus;
    private String deviceMake;
    private String deviceModel;
    private String ipAddress;
    private String macAddress;
    private Customer customer;
    private String selected = "";
    
    public Device() {}

    public Integer getDeviceId() {
        return deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public String getDeviceMake() {
        return deviceMake;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }
    public Customer getCustomer() {
        return customer;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public void setDeviceMake(String deviceMake) {
        this.deviceMake = deviceMake;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void readExternal(PofReader pofReader) throws IOException {
        pofReader.registerIdentity(this);
        deviceId = pofReader.readInt(0);
        deviceType = pofReader.readString(1);
        deviceSerialNumber = pofReader.readString(2);
        deviceStatus = pofReader.readString(3);
        deviceMake = pofReader.readString(4);
        deviceModel = pofReader.readString(5);
        ipAddress = pofReader.readString(6);
        macAddress = pofReader.readString(7);        
        customer = (Customer) pofReader.readObject(8);
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        if(deviceId != null){
            pofWriter.writeInt (0, deviceId);
        }
        pofWriter.writeString (1, deviceType);
        pofWriter.writeString (2, deviceSerialNumber);
        pofWriter.writeString (3, deviceStatus);
        pofWriter.writeString (4, deviceMake);
        pofWriter.writeString (5, deviceModel);
        pofWriter.writeString (6, ipAddress);
        pofWriter.writeString (7, macAddress);
        pofWriter.writeObject (8, customer);
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getSelected() {
        return selected;
    }
}
