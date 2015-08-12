package com.oracle.datagrid.activeactive.util;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.util.Binary;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.InvocableMap.Entry;
import com.tangosol.util.processor.AbstractProcessor;

import java.io.IOException;


@SuppressWarnings("serial")
public class RemoteProcessor extends AbstractProcessor implements PortableObject {
	private Binary customer;
        private String operation;
	
	public RemoteProcessor(){}
	
	public RemoteProcessor(Binary customer, String operation) {
		this.customer = customer;
                this.operation = operation;
	}

	public Object process(Entry entry) {
            if (operation.equalsIgnoreCase("Store")){	
                ((BinaryEntry) entry).updateBinaryValue(customer);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
            }
            else if(operation.equalsIgnoreCase("Erase")){
                //((BinaryEntry) entry).updateBinaryValue(null);
                ((BinaryEntry) entry).updateBinaryValue(customer);
                try {
                        Thread.sleep(50);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
            }
		return null;
	}

	public void readExternal(PofReader reader) throws IOException {
		customer = reader.readBinary(0);
                operation = reader.readString(1);
	}

	public void writeExternal(PofWriter writer) throws IOException {
		if(customer != null){
                    writer.writeBinary(0, customer);
		}
                if(operation != null){
	            writer.writeString(1, operation);
                }
	}
}