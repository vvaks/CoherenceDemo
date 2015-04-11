package com.oracle.datagrid.activeactive.entity;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.NamedCache;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Sequence implements PortableObject {
	private Integer maxSequenceCustomer = 0;
	private Integer maxSequenceDevice = 0;
	private Integer maxSequenceLocation = 0;
	private Integer maxSequenceService = 0;
	
	public Sequence(){}
	public Sequence(NamedCache SequenceCache, String clusterLocation){
		Sequence sequence = new Sequence();
		String sequenceParity = "";
		
		if(clusterLocation.equalsIgnoreCase("West")){
			sequenceParity = "Even";
		}
		else if(clusterLocation.equalsIgnoreCase("East")){
			sequenceParity = "Odd";
		}
		
		if(SequenceCache.lock(1, 5)){
			try{
				if(sequenceParity.equalsIgnoreCase("Even")){
					sequence = (Sequence) SequenceCache.get(1);
					incrementSequenceEven(sequence);
					//this.displaySequences();
					setNewSequences(sequence);
				}
				else if(sequenceParity.equalsIgnoreCase("Odd")){
					sequence = (Sequence) SequenceCache.get(1);
					incrementSequenceOdd(sequence);
					//this.displaySequences();
					setNewSequences(sequence);
				}
				//sequence.displaySequences();
				SequenceCache.put(1, sequence);
				System.out.println("Sequence Unlock Status: " + SequenceCache.unlock(1));
			}	
			finally{
				SequenceCache.unlock(1);
			}
		}
		else{
			SequenceCache.unlock(1);
			SequenceCache.lock(1);
			try{
				if(sequenceParity.equalsIgnoreCase("Even")){
					sequence = (Sequence) SequenceCache.get(1);
					incrementSequenceEven(sequence);
					//this.displaySequences();
					setNewSequences(sequence);
				}
				else if(sequenceParity.equalsIgnoreCase("Odd")){
					sequence = (Sequence) SequenceCache.get(1);
					incrementSequenceOdd(sequence);
					//this.displaySequences();
					setNewSequences(sequence);
				}
				//sequence.displaySequences();
				SequenceCache.put(1, sequence);
				System.out.println("Sequence Unlock Status: " + SequenceCache.unlock(1));
			}	
			finally{
				SequenceCache.unlock(1);
			}
		}
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
		sequence.setMaxSequenceCustomer(getMaxSequenceCustomer());
		sequence.setMaxSequenceDevice(getMaxSequenceDevice());
		sequence.setMaxSequenceLocation(getMaxSequenceLocation());
		sequence.setMaxSequenceService(getMaxSequenceService());
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
			this.setMaxSequenceCustomer(sequence.getMaxSequenceCustomer() + 2);
		}
		else{
			this.setMaxSequenceCustomer(sequence.getMaxSequenceCustomer() + 1);
		}
		if(isEven(sequence.getMaxSequenceDevice())){
			this.setMaxSequenceDevice(sequence.getMaxSequenceDevice() + 2);
		}
		else{
			this.setMaxSequenceDevice(sequence.getMaxSequenceDevice() + 1);
		}
		if(isEven(sequence.getMaxSequenceLocation())){
			this.setMaxSequenceLocation(sequence.getMaxSequenceLocation() + 2);
		}
		else{
			this.setMaxSequenceLocation(sequence.getMaxSequenceLocation() + 1);
		}
		if(isEven(sequence.getMaxSequenceService())){
			this.setMaxSequenceService(sequence.getMaxSequenceService() + 2);
		}
		else{
			this.setMaxSequenceService(sequence.getMaxSequenceService() + 1);
		}
		
	}
	public void incrementSequenceOdd(Sequence sequence){
		if(! isEven(sequence.getMaxSequenceCustomer())){
			this.setMaxSequenceCustomer(sequence.getMaxSequenceCustomer() + 2);
		}
		else{
			this.setMaxSequenceCustomer(sequence.getMaxSequenceCustomer() + 1);
		}
		if(! isEven(sequence.getMaxSequenceDevice())){
			this.setMaxSequenceDevice(sequence.getMaxSequenceDevice() + 2);
		}
		else{
			this.setMaxSequenceDevice(sequence.getMaxSequenceDevice() + 1);
		}
		if(! isEven(sequence.getMaxSequenceLocation())){
			this.setMaxSequenceLocation(sequence.getMaxSequenceLocation() + 2);
		}
		else{
			this.setMaxSequenceLocation(sequence.getMaxSequenceLocation() + 1);
		}
		if(! isEven(sequence.getMaxSequenceService())){
			this.setMaxSequenceService(sequence.getMaxSequenceService() + 2);
		}
		else{
			this.setMaxSequenceService(sequence.getMaxSequenceService() + 1);
		}
		
	}
	public void loadCustomerSequences(){
            String dbUrl = "jdbc:oracle:thin:@10.252.164.68:1531:AUPMDEV";
            String dbUser = "TELECOM";
            String dbPass = "letmein";
            String dbClass = "oracle.jdbc.OracleDriver";
	    String query = "SELECT (SELECT MAX(CUSTOMER_ID) FROM CUSTOMER) AS CUSTOMERID, " +
	                    "(SELECT MAX(DEVICE_ID) FROM DEVICE) AS DEVICEID, " +
	                    "(SELECT MAX(LOCATION_ID) FROM LOCATION) AS LOCATIONID, " +
	                    "(SELECT MAX(SERVICE_ID) FROM SERVICE) AS SERVICEID " +
	                    "FROM DUAL";
		try{
			Class.forName(dbClass);
			Connection conn = DriverManager.getConnection (dbUrl, dbUser, dbPass);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

                        while (rs.next()) {
                                setMaxSequenceCustomer(rs.getInt("CUSTOMERID") + 1);
                                setMaxSequenceDevice(rs.getInt("DEVICEID") + 1);
                                setMaxSequenceLocation(rs.getInt("LOCATIONID") + 1);
                                setMaxSequenceService(rs.getInt("SERVICEID") + 1);
                                //displaySequences();
                        }
			conn.close();
		} 
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
		e.printStackTrace();
		}
	}
	public void displaySequences(){
                System.out.println("Displaying Sequence Object Values");
		System.out.println(this.getMaxSequenceCustomer());
		System.out.println(this.getMaxSequenceDevice());
		System.out.println(this.getMaxSequenceLocation());
		System.out.println(this.getMaxSequenceService());
	}
	public void readExternal(PofReader reader) throws IOException {
		System.out.println("SEQUENCE DESERIALIZATION");
		maxSequenceCustomer = reader.readInt(0);
		maxSequenceDevice = reader.readInt(1);
		maxSequenceLocation = reader.readInt(2);
		maxSequenceService = reader.readInt(3);
	}
	public void writeExternal(PofWriter writer) throws IOException {
		System.out.println("SEQUENCE SERIALIZATION");
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