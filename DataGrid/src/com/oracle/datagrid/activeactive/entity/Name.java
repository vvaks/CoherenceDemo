package com.oracle.datagrid.activeactive.entity;


import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

public class Name implements PortableObject {
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
