package com.oracle.datagrid.activeactive.util;

import com.oracle.datagrid.activeactive.entity.Sequence;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;

import java.io.IOException;


@SuppressWarnings("serial")
public class SequenceProcessor extends AbstractProcessor implements PortableObject {
	Sequence sequence;
	private Integer maxSequenceCustomer = 0;
	private Integer maxSequenceDevice = 0;
	private Integer maxSequenceLocation = 0;
	private Integer maxSequenceService = 0;
	private NamedCache sequenceCache;
	private String clusterLocation;
	
	public SequenceProcessor(){
		sequenceCache = CacheFactory.getCache("Sequence");
		clusterLocation = sequenceCache.getCacheService().getCluster().getLocalMember().getSiteName();
		//BackingMapManager backMapManager = CacheFactory.getCache(sequenceCache.getCacheName()).getCacheService().getBackingMapManager();
		//BackingMapManagerContext backMapManagerCtx = backMapManager.getContext();
		//BackingMapContext sequenceBackMapCtx = backMapManagerCtx.getBackingMapContext("Sequence");
		//sequenceBackMap = sequenceBackMapCtx.getBackingMap();
		System.out.println("Sequence Processor: " + clusterLocation);
	}
	
	//public SequenceProcessor(Binary sequence) {
	//	this.sequence = sequence;
	//}

	public Object process(InvocableMap.Entry entry) {
		//sequence = (Sequence) sequenceCache.get(1);
		//entry = (InvocableMap.Entry) sequenceBackMap.get(1);
		String sequenceParity = "";
		if(clusterLocation.equalsIgnoreCase("West")){
			sequenceParity = "Even";
		}
		else if(clusterLocation.equalsIgnoreCase("East")){
			sequenceParity = "Odd";
		}

		sequence = (Sequence) entry.getValue();
                System.out.println("Sequence Processor: " + sequenceParity);
                if(sequenceParity.equalsIgnoreCase("Even")){
			incrementSequenceEven(sequence);
			//this.displaySequences();
			//entry = (Entry) sequence;
			//entry.setValue(sequence);
			//setNewSequences(sequence);
		}
		else if(sequenceParity.equalsIgnoreCase("Odd")){
			incrementSequenceOdd(sequence);
			//this.displaySequences();
			//entry = (Entry) sequence;
			//entry.setValue(sequence);
			//setNewSequences(sequence);
		}
                System.out.println("Sequence Processor Values");
		sequence.displaySequences();
		setNewSequences(sequence);
                sequence.displaySequences();
		//sequenceCache.put(1, sequence);		
		entry.setValue(sequence);
		return sequence;
	}
	
	private boolean isEven(Integer currentSequence){
		if(currentSequence % 2 == 0){	
			return true;
		}
		else{
			return false;
		}
	}
	private void setNewSequences(Sequence sequence){
		this.setMaxSequenceCustomer(sequence.getMaxSequenceCustomer());
		this.setMaxSequenceDevice(sequence.getMaxSequenceDevice());
		this.setMaxSequenceLocation(sequence.getMaxSequenceLocation());
		this.setMaxSequenceService(sequence.getMaxSequenceService());
	}
	public void setMaxSequenceCustomer(Integer maxSequenceCustomer){
		this.maxSequenceCustomer = maxSequenceCustomer;
	}
	public void setMaxSequenceDevice(Integer maxSequenceDevice){
		this.maxSequenceDevice = maxSequenceDevice;
	}
	public void setMaxSequenceLocation(Integer maxSequenceLocation){
		this.maxSequenceLocation = maxSequenceLocation;
	}
	public void setMaxSequenceService(Integer maxSequenceService){
		this.maxSequenceService = maxSequenceService;
	}
	
	public Integer getMaxSequenceCustomer(){
		return maxSequenceCustomer;
	}
	public Integer getMaxSequenceDevice(){
		return maxSequenceDevice;
	}
	public Integer getMaxSequenceLocation(){
		return maxSequenceLocation;
	}
	public Integer getMaxSequenceService(){
		return maxSequenceService;
	}
	
    public void incrementSequenceEven(Sequence sequence){
            if(isEven(sequence.getMaxSequenceCustomer())){
                    sequence.setMaxSequenceCustomer(sequence.getMaxSequenceCustomer() + 2);
            }
            else{
                    sequence.setMaxSequenceCustomer(sequence.getMaxSequenceCustomer() + 1);
            }
            if(isEven(sequence.getMaxSequenceDevice())){
                    sequence.setMaxSequenceDevice(sequence.getMaxSequenceDevice() + 2);
            }
            else{
                    sequence.setMaxSequenceDevice(sequence.getMaxSequenceDevice() + 1);
            }
            if(isEven(sequence.getMaxSequenceLocation())){
                    sequence.setMaxSequenceLocation(sequence.getMaxSequenceLocation() + 2);
            }
            else{
                    sequence.setMaxSequenceLocation(sequence.getMaxSequenceLocation() + 1);
            }
            if(isEven(sequence.getMaxSequenceService())){
                    sequence.setMaxSequenceService(sequence.getMaxSequenceService() + 2);
            }
            else{
                    sequence.setMaxSequenceService(sequence.getMaxSequenceService() + 1);
            }
            
    }
    public void incrementSequenceOdd(Sequence sequence){
            if(! isEven(sequence.getMaxSequenceCustomer())){
                    sequence.setMaxSequenceCustomer(sequence.getMaxSequenceCustomer() + 2);
            }
            else{
                    sequence.setMaxSequenceCustomer(sequence.getMaxSequenceCustomer() + 1);
            }
            if(! isEven(sequence.getMaxSequenceDevice())){
                    sequence.setMaxSequenceDevice(sequence.getMaxSequenceDevice() + 2);
            }
            else{
                    sequence.setMaxSequenceDevice(sequence.getMaxSequenceDevice() + 1);
            }
            if(! isEven(sequence.getMaxSequenceLocation())){
                    sequence.setMaxSequenceLocation(sequence.getMaxSequenceLocation() + 2);
            }
            else{
                    sequence.setMaxSequenceLocation(sequence.getMaxSequenceLocation() + 1);
            }
            if(! isEven(sequence.getMaxSequenceService())){
                    sequence.setMaxSequenceService(sequence.getMaxSequenceService() + 2);
            }
            else{
                    sequence.setMaxSequenceService(sequence.getMaxSequenceService() + 1);
            }
            
    }
	
    public void displaySequences(){
            System.out.println(getMaxSequenceCustomer());
            System.out.println(getMaxSequenceDevice());
            System.out.println(getMaxSequenceLocation());
            System.out.println(getMaxSequenceService());
    }
	  
	public void readExternal(PofReader reader) throws IOException {
		//sequence = reader.readBinary(0);
		System.out.println("SEQUENCE PROCESSOR DESERIALIZATION");
		maxSequenceCustomer = reader.readInt(0);
		maxSequenceDevice = reader.readInt(1);
		maxSequenceLocation = reader.readInt(2);
		maxSequenceService = reader.readInt(3);
    	}

	public void writeExternal(PofWriter writer) throws IOException {
		if(sequence != null){
			//writer.writeBinary(0, sequence);
			System.out.println("SEQUENCE PROCESSOR SERIALIZATION");
			if (maxSequenceCustomer != 0) {  
				writer.writeInt(0, maxSequenceCustomer);  
				}  
			if (maxSequenceDevice != 0) { 
				writer.writeInt(1, maxSequenceDevice);  
			}
			if (maxSequenceLocation != 0) { 
				writer.writeInt(2, maxSequenceLocation);  
			}
			if (maxSequenceService != 0) { 
				writer.writeInt(3, maxSequenceService);  
			}
		}
	}
}