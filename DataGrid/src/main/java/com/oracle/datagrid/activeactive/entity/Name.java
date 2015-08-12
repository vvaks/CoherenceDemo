package com.oracle.datagrid.activeactive.entity;


import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Name")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Name implements PortableObject, Serializable {
    @SuppressWarnings("compatibility:-4869011919418444192")
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    
    public Name() {}
    
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void readExternal(PofReader pofReader) throws IOException {
        pofReader.registerIdentity(this);
        firstName = pofReader.readString(0);
        lastName = pofReader.readString(1);
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeString(0, firstName);
        pofWriter.writeString(1, lastName);
    }
}
