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
public class CustomerProcessor extends AbstractProcessor implements PortableObject {
	Binary customer;
	
	public CustomerProcessor(){}
	
	public CustomerProcessor(Binary customer) {
		this.customer = customer;
	}

	public Object process(Entry entry) {
		((BinaryEntry) entry).updateBinaryValue(customer);
		return null;
	}

	public void readExternal(PofReader reader) throws IOException {
		customer = reader.readBinary(0);
	}

	public void writeExternal(PofWriter writer) throws IOException {
		if(customer != null){
			writer.writeBinary(0, customer);
		}
	}
}