package com.oracle.datagrid.NoSQL.util;

import com.oracle.datagrid.NoSQL.entity.Customer;
import com.oracle.datagrid.NoSQL.entity.Device;
import com.oracle.datagrid.NoSQL.entity.Location;
import com.oracle.datagrid.NoSQL.entity.Name;
import com.oracle.datagrid.NoSQL.entity.Service;

import com.oracle.datagrid.activeactive.entity.Sequence;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.BackingMapManager;
import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.kv.Key;

@SuppressWarnings("serial")
public class RemoteDistributedCacheLoader implements Invocable, PortableObject{
	private static final String INVOCATION_SERVICE_NAME = "InvocationService";
	private static final String GRID_ENVIRONMENT_NAME = "GridEnvironment";
	private final String CUSTOMER_WEST_CACHE_NAME = "AvroCustomerCache";
        private final String LOCATION_WEST_CACHE_NAME = "Location";
        private final String DEVICE_WEST_CACHE_NAME = "Device";
        private final String SERVICES_WEST_CACHE_NAME = "Services";    
        
	private InvocationService invocationService;
	private NamedCache SequenceCache;
	private NamedCache CustomerCache;
        private NamedCache LocationCache;
        private NamedCache DeviceCache;
        private NamedCache ServicesCache;
	private NamedCache localGridEnvironmentCache;
	
	private Customer customer;
	private Device device;
	private Location location;
	private Name name;
	private Service service;
	private Sequence sequence;
	private Connection conn;
	private String dbUrl = "jdbc:oracle:thin:@192.168.56.1:1522:oem12c";
	private String dbUser = "TELECOM";
	private String dbPass = "letmein";
	private String dbClass = "oracle.jdbc.OracleDriver";
	private Boolean isCascaded;
	
	public void init(InvocationService invocationService) {
		this.invocationService = (InvocationService) CacheFactory.getConfigurableCacheFactory().ensureService(INVOCATION_SERVICE_NAME);
		localGridEnvironmentCache = CacheFactory.getCache(GRID_ENVIRONMENT_NAME);
		SequenceCache = CacheFactory.getCache("Sequence");
		CustomerCache = null;
		
		//BackingMapManager backMapManager = PersistenceCache.getCacheService().getBackingMapManager();
		//BackingMapManagerContext backMapManagerCtx = backMapManager.getContext();
		//String localCacheNodeName = backMapManagerCtx.getCacheService().getCluster().getLocalMember().getMemberName();
                
                CustomerCache = CacheFactory.getCache(CUSTOMER_WEST_CACHE_NAME);
		LocationCache = CacheFactory.getCache(LOCATION_WEST_CACHE_NAME);
		DeviceCache = CacheFactory.getCache(DEVICE_WEST_CACHE_NAME);
		ServicesCache = CacheFactory.getCache(SERVICES_WEST_CACHE_NAME);
		
		try{
			Class.forName(dbClass);
			conn = DriverManager.getConnection (dbUrl, dbUser, dbPass);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

		catch(SQLException e) {
		e.printStackTrace();
		}		
	}
	
	public void run() {
		System.out.println("Preparing to Execute Remote CacheLoader");
		RemoteDistributedCacheLoader cacheLoader = new RemoteDistributedCacheLoader();
		cacheLoader.setIsCascaded(true);
                System.out.println("Executing Remote CacheLoader");
		
		if(SequenceCache.size() <= 0){
                        System.out.println("Loading Sequences");
			this.loadCustomerSequences();
		}
		if(CustomerCache.size() <= 0){
                    System.out.println("Loading Customer Cache");
                    try {
                        this.loadCustomerCache();
                    } catch (IOException e) {
                        System.out.print(e.getStackTrace());
                    }
                }
		
		try{
                    this.closeConnection();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public Object getResult() {
            return null;
	}

	public void loadCustomerSequences(){
		System.out.println("LOADING SEQUENCES");
		NamedCache SequenceCache = CacheFactory.getCache("Sequence");
		sequence = new Sequence();
		
		String query = "SELECT (SELECT MAX(CUSTOMER_ID) FROM CUSTOMER) AS CUSTOMERID, " +
				"(SELECT MAX(DEVICE_ID) FROM DEVICE) AS DEVICEID, " +
				"(SELECT MAX(LOCATION_ID) FROM LOCATION) AS LOCATIONID, " +
				"(SELECT MAX(SERVICE_ID) FROM SERVICE) AS SERVICEID " +
				"FROM DUAL";	
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
                        
                        Integer customerSequence = 0;
                        Integer deviceSequence = 0;
                        Integer locationSequence = 0;
                        Integer serviceSequence = 0;
			while (rs.next()) {
                            customerSequence = rs.getInt("CUSTOMERID");
                            deviceSequence = rs.getInt("DEVICEID");
			    locationSequence = rs.getInt("LOCATIONID");
			    serviceSequence = rs.getInt("SERVICEID");
                            
                            if(customerSequence == null){
                                customerSequence = 0;
                            }
			    if(deviceSequence == null){
                                deviceSequence = 0;	
                            }
			    if(locationSequence == null){
                                locationSequence = 0;
                            }
			    if(serviceSequence == null){
                                serviceSequence = 0;
                            }	
                                sequence.displaySequences();
			}
                        sequence.setMaxSequenceCustomer(customerSequence + 1);
                        sequence.setMaxSequenceDevice(deviceSequence + 1);
                        sequence.setMaxSequenceLocation(locationSequence + 1);
                        sequence.setMaxSequenceService(serviceSequence + 1);
                        SequenceCache.put(1, sequence);
		} 
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadCustomerCache() throws IOException {
		System.out.println("LOADING DATA TO MEMORY");
		Integer customerKey;
                Map <Integer, Customer>customerMap = new HashMap<Integer, Customer>();
                Map <Integer, Location>locationMap = new HashMap<Integer, Location>();
                Map <Integer, Device>deviceMap = new HashMap<Integer, Device>();
                Map <Integer, Service>servicesMap = new HashMap<Integer, Service>();
                List <Location> locationList = new ArrayList<Location>();
                List <Device> deviceList = new ArrayList<Device>();
                List <Service> serviceList = new ArrayList<Service>();
                
                String locationQuery = "SELECT location_id, address, city, state, postal_code, country FROM Location ";
                
                String deviceQuery = "SELECT device_id, type, serial_number, status, make, model, ip_address, mac_address FROM Device ";
                
                String servicesQuery = "SELECT service_id, description, lob, name, rate, service_code FROM Service";
                
		String customerQuery = "SELECT customer_id, account_number, first_name, last_name, home_phone " +
                                        "FROM Customer C " +
                                        "ORDER BY C.account_number";
                
                String customerLocationQuery = "SELECT C.account_number, location_id, address, city, state, postal_code, country " +
	                            "FROM Customer C, Location L " +
	                            "WHERE C.Account_Number = L.Account_Number " +
	                            "ORDER BY C.account_number";
                
                String customerDeviceQuery = "SELECT C.account_number account_number, device_id, type, serial_number, status, make, model, ip_address, mac_address " +
                                        "FROM Customer C, Device D " +
                                        "WHERE C.Account_Number = D.Account_Number " +
                                        "ORDER BY C.account_number";
                
                String customerServicesQuery = "SELECT C.account_number account_number, s.service_id service_id, description, lob, name, rate, service_code " +
                                        "FROM Customer C, Customer_Service CS, Service S " +
                                        "WHERE C.Account_Number = CS.Account_Number " +
                                        "and CS.Service_id = S.Service_id " +
                                        "ORDER BY C.account_number";
	    
            System.out.println("LOADING LOCATION CACHE");
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(locationQuery);
                if(rs.first()){
                    location = new Location();
                    location.setLocationId(rs.getInt("location_id"));
                    location.setAddress(rs.getString("address"));
                    location.setCity(rs.getString("city"));
                    location.setState(rs.getString("state"));
                    location.setPostalCode(rs.getString("postal_code"));
                    location.setCountry(rs.getString("country"));
                    
                    locationMap.put(location.getLocationId(), location);
                    while (rs.next()) {     
                        location = new Location();
                        location.setLocationId(rs.getInt("location_id"));
                        location.setAddress(rs.getString("address"));
                        location.setCity(rs.getString("city"));
                        location.setState(rs.getString("state"));
                        location.setPostalCode(rs.getString("postal_code"));
                        location.setCountry(rs.getString("country"));
                        
                        locationMap.put(location.getLocationId(), location);
                    }    
                }
                else{
                    location = new Location();
                    location.setLocationId(1000);
                    location.setAddress("12345 Market St.");
                    location.setCity("Philadelphia");
                    location.setState("PA");
                    location.setPostalCode("19100");
                    location.setCountry("USA");
                    
                    locationMap.put(location.getLocationId(), location);
                }
	    }
	    catch(SQLException e) {
	            e.printStackTrace();
	    }
            LocationCache.putAll(locationMap);
            
	    System.out.println("LOADING DEVICE CACHE");
	    try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(deviceQuery);
                if(rs.first()){
                    while (rs.next()) {     
	                device = new Device();
	                device.setDeviceId(rs.getInt("device_id"));
	                device.setDeviceMake(rs.getString("make"));
	                device.setDeviceModel(rs.getString("model"));
	                device.setDeviceSerialNumber(rs.getString("serial_number"));
	                device.setDeviceStatus(rs.getString("status"));
	                device.setDeviceType(rs.getString("type"));
	                device.setIpAddress(rs.getString("ip_address"));
	                device.setMacAddress(rs.getString("mac_address"));
	                
	                deviceMap.put(device.getDeviceId(), device);
	            }
                }
                else{
                    device = new Device();
                    device.setDeviceId(1000);
                    device.setDeviceMake("Cisco");
                    device.setDeviceModel("1000CX");
                    device.setDeviceSerialNumber("RS100000");
                    device.setDeviceStatus("Active");
                    device.setDeviceType("CM");
                    device.setIpAddress("10.10.10.1");
                    device.setMacAddress("ab-ac-fv-01-ex");        
                }
            }
	    catch(SQLException e) {
	            e.printStackTrace();
	    }
	    DeviceCache.putAll(deviceMap);
            
	    System.out.println("LOADING SERVICES CACHE");
	    try{
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(servicesQuery);
	            while (rs.next()) {     
	                service = new Service();
	                service.setServiceId(rs.getInt("service_id"));
	                service.setServiceDescription(rs.getString("description"));
	                service.setServiceLOB(rs.getString("lob"));
	                service.setServiceName(rs.getString("name"));
	                service.setServiceRate(rs.getString("rate"));
	                service.setServiceCode(rs.getString("service_code"));
	                
	                servicesMap.put(service.getServiceId(), service);
	            }
	    }
	    catch(SQLException e) {
	            e.printStackTrace();
	    }
	    ServicesCache.putAll(servicesMap);
            
	    System.out.println("LOADING CUSTOMER CACHE");
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(customerQuery);
                while (rs.next()) {	
                    customer = new Customer();
                    name = new Name();
                    customerKey = rs.getInt("account_number");
                    customer.setCustomerId(rs.getInt("account_number"));
                    customer.setAccountNumber(rs.getInt("account_number")); 
                    customer.setCustomerPhone(rs.getString("home_phone"));
                    customer.setCustomerName(name);
                    name.setFirstName(rs.getString("first_name"));
                    name.setLastName(rs.getString("last_name"));
                    location = new Location();
                    location.setAddress("1111 Test");
                    location.setCity("Phila");
                    location.setState("PA");
                    location.setPostalCode("19100");
                    location.setCountry("USA");
                    location.setLocationId(1000);
                    customer.setCustomerLocation(location);
                    device = new Device();
                    device.setDeviceId(1000);
                    device.setDeviceMake("Cisco");
                    device.setDeviceModel("1000CX");
                    device.setDeviceSerialNumber("RS100000");
                    device.setDeviceStatus("Active");
                    device.setDeviceType("CM");
                    device.setIpAddress("10.10.10.1");
                    device.setMacAddress("ab-ac-fv-01-ex");
                    deviceList = new ArrayList<Device>();
                    deviceList.add(device);
                    customer.setCustomerDevices(deviceList);
                    service = new Service();                    
                    service.setServiceId(1000);
                    service.setServiceDescription("Premium Channel");
                    service.setServiceLOB("VIDEO");
                    service.setServiceName("HBO");
                    service.setServiceRate("25.00");
                    service.setServiceCode("VSD");
                    serviceList = new ArrayList<Service>();
                    serviceList.add(service);
                    customer.setCustomerServices(serviceList);
                    customerMap.put(customerKey, customer);
                }
            } 
            catch(SQLException e) {
                e.printStackTrace();
            }
	    try{
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(customerLocationQuery);
	        Integer nextCustomerKey = 0;
	        customer = new Customer();
	        customerKey = 0;
	        
            if(rs.first()){
                customer = new Customer();
                customerKey = rs.getInt("account_number"); 
                customer = customerMap.get(customerKey);
                location = new Location();
                location.setLocationId(rs.getInt("location_id"));
                location.setAddress(rs.getString("address"));
                location.setCity(rs.getString("city"));
                location.setState(rs.getString("state"));
                location.setPostalCode(rs.getString("postal_code"));
                location.setCountry(rs.getString("country"));
                while (rs.next()) {
                    customer.setCustomerLocation(location);
                    customerMap.put(customerKey, customer);
                    location = new Location();
                    customerKey = rs.getInt("account_number"); 
                    customer = new Customer();
                    customer = customerMap.get(customerKey);  
	            //location.setCustomer(customer);
	            location.setLocationId(rs.getInt("location_id"));
	            location.setAddress(rs.getString("address"));
	            location.setCity(rs.getString("city"));
	            location.setState(rs.getString("state"));
	            location.setPostalCode(rs.getString("postal_code"));
	            location.setCountry(rs.getString("country"));
	        }
            }
            else{
                location = new Location();
                location.setAddress("1111 Test");
                location.setCity("Phila");
                location.setState("PA");
                location.setPostalCode("19100");
                location.setCountry("USA");
                location.setLocationId(1000);
                customer.setCustomerLocation(location);
            }
	    } 
	    catch(SQLException e) {
	        e.printStackTrace();
	    }
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(customerDeviceQuery);
                Integer nextCustomerKey = 0;
                customer = new Customer();
                customerKey = 0;
                if(rs.first()){
                    device.setDeviceId(rs.getInt("device_id"));
                    device.setDeviceMake(rs.getString("make"));
                    device.setDeviceModel(rs.getString("model"));
                    device.setDeviceSerialNumber(rs.getString("serial_number"));
                    device.setDeviceStatus(rs.getString("status"));
                    device.setDeviceType(rs.getString("type"));
                    device.setIpAddress(rs.getString("ip_address"));
                    device.setMacAddress(rs.getString("mac_address"));   
                while (rs.next()) {
                    device = new Device();
                    nextCustomerKey = rs.getInt("account_number");
                    if(customerKey != nextCustomerKey && customerKey == 0){
                        customer = new Customer();
                        customerKey = rs.getInt("account_number"); 
                        customer = customerMap.get(customerKey);
                    }
                    else if(customerKey != nextCustomerKey && customerKey > 0){    
                        customer.setCustomerDevices(deviceList);
                        customerMap.put(customerKey, customer);
                        deviceList = new ArrayList<Device>();
                        customerKey = nextCustomerKey; 
                        customer = new Customer();
                        customer = customerMap.get(customerKey);
                    }
                    //device.setCustomer(customer);
                    device.setDeviceId(rs.getInt("device_id"));
                    device.setDeviceMake(rs.getString("make"));
                    device.setDeviceModel(rs.getString("model"));
                    device.setDeviceSerialNumber(rs.getString("serial_number"));
                    device.setDeviceStatus(rs.getString("status"));
                    device.setDeviceType(rs.getString("type"));
                    device.setIpAddress(rs.getString("ip_address"));
                    device.setMacAddress(rs.getString("mac_address"));
                    deviceList.add(device);
                }
                }
                else{
                    device = new Device();
                    device.setDeviceId(1000);
                    device.setDeviceMake("Cisco");
                    device.setDeviceModel("1000CX");
                    device.setDeviceSerialNumber("RS100000");
                    device.setDeviceStatus("Active");
                    device.setDeviceType("CM");
                    device.setIpAddress("10.10.10.1");
                    device.setMacAddress("ab-ac-fv-01-ex");   
                }
	    } 
	    catch(SQLException e) {
                e.printStackTrace();
	    }
	    try{
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(customerServicesQuery);
	        Integer nextCustomerKey = 0;
	        customer = new Customer();
                customerKey = 0;
	        while (rs.next()) {
	            service = new Service();
	            nextCustomerKey = rs.getInt("account_number");
	            System.out.println("1: "+customerKey+" "+nextCustomerKey+" "+serviceList.size());
                    if(customerKey != nextCustomerKey && customerKey == 0){
	                customer = new Customer();
	                customerKey = rs.getInt("account_number"); 
	                customer = customerMap.get(customerKey);
	            }
	            else if(customerKey != nextCustomerKey && customerKey > 0){    
	                customer.setCustomerServices(serviceList);
	                customerMap.put(customerKey, customer);
	                serviceList = new ArrayList<Service>();
	                customerKey = nextCustomerKey; 
	                System.out.println("2: "+customer.getAccountNumber());
                        customer = new Customer();
	                System.out.println("3: "+customerKey+" "+customerMap.get(customerKey));
	                customer = customerMap.get(customerKey);
	                System.out.println("4: "+customer.getAccountNumber());
	            }
                    service.setServiceId(rs.getInt("service_id"));
	            service.setServiceDescription(rs.getString("description"));
	            service.setServiceLOB(rs.getString("lob"));
	            service.setServiceName(rs.getString("name"));
	            service.setServiceRate(rs.getString("rate"));
	            service.setServiceCode(rs.getString("service_code"));
                    serviceList.add(service);
                    //System.out.println(customer.getCustomerAccountNumber());
	        }
	    }
	    catch(SQLException e) {
	        e.printStackTrace();
	    }
            
	    Customer currCustomer= new Customer();
	    Iterator it = customerMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry)it.next();
                currCustomer = (Customer)entry.getValue();
                CustomerCache.put(Key.createKey(Arrays.asList("customer", "avro", currCustomer.getAccountNumber().toString())),currCustomer);
            }
	    //CustomerCache.putAll(customerMap);
	}
	public boolean getIsCascaded(){
            return this.isCascaded;
	}
	public void setIsCascaded(Boolean cascadeStatus){
            this.isCascaded = cascadeStatus;
	}
	public void closeConnection() throws SQLException{
            this.conn.close();
	}
	public void readExternal(PofReader reader) throws IOException {
            isCascaded = reader.readBoolean(0);
	}
	public void writeExternal(PofWriter writer) throws IOException {
            if(isCascaded != null){
                    writer.writeBoolean(0, isCascaded);
            }
	}
}