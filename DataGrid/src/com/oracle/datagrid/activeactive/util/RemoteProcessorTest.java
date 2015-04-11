package com.oracle.datagrid.activeactive.util;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.InvocableMap.Entry;
import com.tangosol.util.processor.AbstractProcessor;

import java.io.IOException;


public class RemoteProcessorTest extends AbstractProcessor implements PortableObject{
	
	public RemoteProcessorTest(){
	}

	public Object process(BinaryEntry binaryEntry) {
		System.out.println("Executing Entry Processor");
		binaryEntry.setValue(81);
		return binaryEntry.getValue();
	}

	public void readExternal(PofReader reader) throws IOException {
		
	}

	public void writeExternal(PofWriter writer) throws IOException {
		
	}

	public Object process(Entry arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}