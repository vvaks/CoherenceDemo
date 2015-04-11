package com.oracle.datagrid.activeactive.entity;


import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

import java.util.List;


public class Service implements PortableObject {
    private Integer serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceLOB;
    private String serviceRate;
    private String serviceCode;
    private List<Customer> customers;
    private String selected = "";

    public Service() {}

    public Integer getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getServiceLOB() {
        return serviceLOB;
    }

    public String getServiceRate() {
        return serviceRate;
    }

    public String getServiceCode() {
        return serviceCode;
    }
    
    public List<Customer> getCustomers(){
        return customers;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public void setServiceLOB(String serviceLOB) {
        this.serviceLOB = serviceLOB;
    }

    public void setServiceRate(String serviceRate) {
        this.serviceRate = serviceRate;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
    
    public void setCustomers(List<Customer> customers){
        this.customers = customers;
    }
    
    public void readExternal(PofReader pofReader) throws IOException {
        pofReader.registerIdentity(this);
        serviceId = pofReader.readInt(0);
        serviceName = pofReader.readString(1);
        serviceDescription = pofReader.readString(2);
        serviceLOB = pofReader.readString(3);
        serviceRate = pofReader.readString(4);
        serviceCode = pofReader.readString(5);
        customers = (List<Customer>) pofReader.readObject(6);
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        if(serviceId != null){
            pofWriter.writeInt (0, serviceId);
        }
        pofWriter.writeString (1, serviceName);
        pofWriter.writeString (2, serviceDescription);
        pofWriter.writeString (3, serviceLOB);
        pofWriter.writeString (4, serviceRate);
        pofWriter.writeString (5, serviceCode);
        pofWriter.writeObject (6, customers);
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getSelected() {
        return selected;
    }
}
