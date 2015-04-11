package com.oracle.datagrid.activeactive.util;

import com.oracle.datagrid.activeactive.entity.Customer;
import com.oracle.datagrid.activeactive.entity.Device;
import com.oracle.datagrid.activeactive.entity.Location;
import com.oracle.datagrid.activeactive.entity.Name;
import com.oracle.datagrid.activeactive.entity.Sequence;
import com.oracle.datagrid.activeactive.entity.Service;

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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

/*
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
*/

@SuppressWarnings("serial")
public class RemoteDistributedCacheLoader implements Invocable, PortableObject{
	private static final String INVOCATION_SERVICE_NAME = "InvocationService";
	private final String PERSISTENCE_QUEUE_CACHE_NAME = "Customer";
	private static final String GRID_ENVIRONMENT_NAME = "GridEnvironment";
	//private final String EAST_GRID_ENVIRONMENT_NAME = "GridEnvironmentEast";
	//private final String WEST_GRID_ENVIRONMENT_NAME = "GridEnvironmentWest";
	private final String CUSTOMER_EAST_CACHE_NAME = "East-Customer";
	private final String CUSTOMER_WEST_CACHE_NAME = "West-Customer";
        private final String LOCATION_EAST_CACHE_NAME = "East-Location";
        private final String LOCATION_WEST_CACHE_NAME = "West-Location";
        private final String DEVICE_EAST_CACHE_NAME = "East-Device";
        private final String DEVICE_WEST_CACHE_NAME = "West-Device";
        private final String SERVICES_EAST_CACHE_NAME = "East-Services";
        private final String SERVICES_WEST_CACHE_NAME = "West-Services";    
        
	private InvocationService invocationService;
	private NamedCache SequenceCache;
	private NamedCache PersistenceCache;
	private NamedCache CustomerCache;
        private NamedCache LocationCache;
        private NamedCache DeviceCache;
        private NamedCache ServicesCache;
        private NamedCache RemoteCustomerCache;
	private NamedCache localGridEnvironmentCache;
	
	private Customer customer;
	private Device device;
	private Location location;
	private Name name;
	private Service service;
	private Sequence sequence;
	private Connection conn;
	//private String dbUrl = "jdbc:oracle:thin:@192.168.56.102:1521:fmw12c";
        private String dbUrl = "jdbc:oracle:thin:@fmwdb02:1521/fmw12c.usoracle88058.oraclecloud.internal";
        private String dbUser = "TELECOM";
	private String dbPass = "welcome1";
	private String dbClass = "oracle.jdbc.OracleDriver";
	private String clusterLocation;
	private Boolean isCascaded;
	
	public void init(InvocationService invocationService) {
        try{
                localGridEnvironmentCache = CacheFactory.getCache(GRID_ENVIRONMENT_NAME);
        }
        catch(Throwable e){
            e.printStackTrace();
        }
                localGridEnvironmentCache.put("ActiveActiveDemo", Boolean.TRUE);
                if((Boolean) localGridEnvironmentCache.get("ActiveActiveDemo")){
                    this.invocationService = (InvocationService) CacheFactory.getConfigurableCacheFactory().ensureService(INVOCATION_SERVICE_NAME);
		}
		SequenceCache = CacheFactory.getCache("Sequence");
		PersistenceCache = CacheFactory.getCache(PERSISTENCE_QUEUE_CACHE_NAME);
		CustomerCache = null;
		RemoteCustomerCache = null;
		
		BackingMapManager backMapManager = PersistenceCache.getCacheService().getBackingMapManager();
		BackingMapManagerContext backMapManagerCtx = backMapManager.getContext();
		
		String localCacheNodeName = backMapManagerCtx.getCacheService().getCluster().getLocalMember().getMemberName();
		clusterLocation = backMapManagerCtx.getCacheService().getCluster().getLocalMember().getSiteName();
		System.out.println("Cluster Location is: " + clusterLocation);
                
		if (clusterLocation.equalsIgnoreCase("East")){
			CustomerCache = CacheFactory.getCache(CUSTOMER_EAST_CACHE_NAME);
                        if((Boolean) localGridEnvironmentCache.get("ActiveActiveDemo")){    
                            RemoteCustomerCache = CacheFactory.getCache(CUSTOMER_WEST_CACHE_NAME);
                        }
                        //localGridEnvironmentCache = CacheFactory.getCache(EAST_GRID_ENVIRONMENT_NAME);
			LocationCache = CacheFactory.getCache(LOCATION_EAST_CACHE_NAME);
			//LOCATION_WEST_CACHE_NAME = "Location-West";
			DeviceCache = CacheFactory.getCache(DEVICE_EAST_CACHE_NAME);
			//DEVICE_WEST_CACHE_NAME = "Device-West";
			ServicesCache = CacheFactory.getCache(SERVICES_EAST_CACHE_NAME);
			//SERVICES_WEST_CACHE_NAME = "Services-West";
		}
		else if(clusterLocation.equalsIgnoreCase("West")){
			CustomerCache = CacheFactory.getCache(CUSTOMER_WEST_CACHE_NAME);
                        if((Boolean) localGridEnvironmentCache.get("ActiveActiveDemo")){    
                            RemoteCustomerCache = CacheFactory.getCache(CUSTOMER_EAST_CACHE_NAME);
                        }
                        //localGridEnvironmentCache = CacheFactory.getCache(WEST_GRID_ENVIRONMENT_NAME);
			LocationCache = CacheFactory.getCache(LOCATION_WEST_CACHE_NAME);
			DeviceCache = CacheFactory.getCache(DEVICE_WEST_CACHE_NAME);
			ServicesCache = CacheFactory.getCache(SERVICES_WEST_CACHE_NAME);
                }
		
                try {
                    Context ctx = null;
                    Hashtable ht = new Hashtable();
                    ht.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
                    ht.put(Context.PROVIDER_URL, System.getProperty("weblogic.management.server").replace("http", "t3"));
                    ctx = new InitialContext(ht);
                    DataSource dataSource = (DataSource) ctx.lookup("jdbc/Telecom");
                    conn = dataSource.getConnection();
	        
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                /*
		try{
			Class.forName(dbClass);
			conn = DriverManager.getConnection (dbUrl, dbUser, dbPass);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

		catch(SQLException e) {
		e.printStackTrace();
		}*/		
	}
	
	public void run() {
		//BackingMapManager backMapManager = PersistenceCache.getCacheService().getBackingMapManager();
		//BackingMapManagerContext backMapManagerCtx = backMapManager.getContext();
		//BackingMapContext GEBackMapCtx = backMapManagerCtx.getBackingMapContext(GridEnvironmentCache.getCacheName());
		//ObservableMap GEBackMap = GEBackMapCtx.getBackingMap();
		
		//BackingMapManager PABackMapManager = PolicyActionCache.getCacheService().getBackingMapManager();
		//BackingMapManagerContext PABackMapManagerCtx = PABackMapManager.getContext();
		//BackingMapContext PABackMapCtx = PABackMapManagerCtx.getBackingMapContext(PolicyActionCache.getCacheName());
		//ObservableMap PABackMap = PABackMapCtx.getBackingMap();
		
		//Binary binReplicationStatusKey = ExternalizableHelper.toBinary("ReplicationStatus");
		//Binary binReplicationStatusValue = ExternalizableHelper.toBinary(Boolean.FALSE);
		//GEBackMap.put(binReplicationStatusKey, binReplicationStatusValue);
		
		localGridEnvironmentCache.put("ReplicationStatus", Boolean.FALSE);
		localGridEnvironmentCache.put("LocalGridLoading", Boolean.TRUE);
		localGridEnvironmentCache.put("LocalGridLoaded", Boolean.FALSE);
		
		if(localGridEnvironmentCache.get("ClusterLocation") == null){
			//GEBackMap.put("ClusterLocation", clusterLocation);
			localGridEnvironmentCache.put("ClusterLocation", clusterLocation);
                        System.out.println("Local Cluster Location is: " + localGridEnvironmentCache.get("ClusterLocation"));
		}
		
		Boolean remoteGridLoadingStatus = (Boolean) localGridEnvironmentCache.get("RemoteGridLoading");
		Boolean remoteGridLoadedStatus = (Boolean) localGridEnvironmentCache.get("RemoteGridLoaded");
		CacheLoaderObserver cacheLoaderObserver = null;
		if(remoteGridLoadingStatus == null || !remoteGridLoadingStatus){
			if(remoteGridLoadedStatus == null || !remoteGridLoadingStatus){
				if(!isCascaded || isCascaded == null){
					localGridEnvironmentCache.put("RemoteGridLoading", Boolean.TRUE);
					localGridEnvironmentCache.put("RemoteGridLoaded", Boolean.FALSE);
					System.out.println("Preparing to Execute Remote CacheLoader");
					RemoteDistributedCacheLoader cacheLoader = new RemoteDistributedCacheLoader();
					//cacheLoaderObserver = new CacheLoaderObserver();
					cacheLoader.setIsCascaded(true);
					System.out.println("Executing Remote CacheLoader");
					//invocationService.execute(cacheLoader, null, cacheLoaderObserver);	
                                        if((Boolean)localGridEnvironmentCache.get("ActiveActiveDemo")){
                                            invocationService.query(cacheLoader, null);
                                        }
                                }
			}
			localGridEnvironmentCache.put("RemoteGridLoading", Boolean.FALSE);
			localGridEnvironmentCache.put("RemoteGridLoaded", Boolean.TRUE);
		}
		
		if(SequenceCache.size() <= 0){
                        System.out.println("Loading Sequences");
			this.loadCustomerSequences();
		}
		if(CustomerCache.size() <= 0){
                    System.out.println("Loading Customer Cache");
                    try {
                        //this.loadCustomerCacheORM();
                        this.loadCustomerCache();
                    } catch (IOException e) {
                        System.out.print(e.getStackTrace());
                    }
                }
		
		localGridEnvironmentCache.put("LocalGridLoading", Boolean.FALSE);
		localGridEnvironmentCache.put("LocalGridLoaded", Boolean.TRUE);
		//localGridEnvironmentCache.put("ReplicationStatus", Boolean.TRUE);
		//binReplicationStatusValue = ExternalizableHelper.toBinary(Boolean.TRUE);
		//GEBackMap.put(binReplicationStatusKey, binReplicationStatusValue);
		
		if(!(cacheLoaderObserver == null)){
			System.out.println(((CacheLoaderObserver) cacheLoaderObserver).getResults());
			localGridEnvironmentCache.remove("InvocationComplete");
		}
		
                System.out.println("Ensuring Grid State is Ready");
		if(!(Boolean)localGridEnvironmentCache.get("LocalGridLoading") && 
				(Boolean)localGridEnvironmentCache.get("LocalGridLoaded") && 
				!(Boolean)localGridEnvironmentCache.get("RemoteGridLoading") && 
				(Boolean)localGridEnvironmentCache.get("RemoteGridLoaded")){
                    if((Boolean)localGridEnvironmentCache.get("ActiveActiveDemo")){
                        System.out.println("Enabling Replication");
			localGridEnvironmentCache.put("ReplicationStatus", Boolean.TRUE);
                    }
                    else{
                        System.out.println("ActiveActive Demo is Disabled: Disable Replication");
                        localGridEnvironmentCache.put("ReplicationStatus", Boolean.FALSE);
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
	            while (rs.next()) {     
	                location = new Location();
	                location.setLocationId(rs.getInt("location_id"));
	                location.setAdress(rs.getString("address"));
	                location.setCity(rs.getString("city"));
	                location.setState(rs.getString("state"));
	                location.setPostalCode(rs.getString("postal_code"));
	                location.setCountry(rs.getString("country"));
	                
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
                    location = new Location();
                            
                    customerKey = rs.getInt("account_number");
                    customer.setCustomerId(rs.getInt("account_number"));
                    customer.setCustomerAccountNumber(rs.getInt("account_number")); 
                    customer.setCustomerPhone(rs.getString("home_phone"));
                    customer.setCustomerName(name);
                    customer.setCustomerLocation(location);
                    name.setFirstName(rs.getString("first_name"));
                    name.setLastName(rs.getString("last_name"));
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
	        location = new Location();
                customerKey = 0;
	            
	        while (rs.next()) {
	            nextCustomerKey = rs.getInt("account_number");
	            System.out.println("Load Location:1: "+customerKey+" "+nextCustomerKey);
                    if(customerKey != nextCustomerKey && customerKey == 0){
	                customer = new Customer();
	                customerKey = rs.getInt("account_number"); 
	                customer = customerMap.get(customerKey);
                    }
	            else if(customerKey != nextCustomerKey && customerKey > 0){    
	                customer.setCustomerLocation(location);
	                customerMap.put(customerKey, customer);
	                System.out.println("Load Location:4: "+customer.getCustomerAccountNumber()+" "+customer.getCustomerLocation().getCustomer().getCustomerAccountNumber());
                        location = new Location();
	                customerKey = nextCustomerKey; 
	                customer = new Customer();
	                customer = customerMap.get(customerKey);
	            }  
	            System.out.println("Load Location:2: "+customer.getCustomerAccountNumber());
	            location.setCustomer(customer);
	            location.setLocationId(rs.getInt("location_id"));
	            location.setAdress(rs.getString("address"));
	            location.setCity(rs.getString("city"));
	            location.setState(rs.getString("state"));
	            location.setPostalCode(rs.getString("postal_code"));
	            location.setCountry(rs.getString("country"));
	            System.out.println("Load Location:3: "+location.getCustomer().getCustomerAccountNumber());
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
                    device.setCustomer(customer);
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
	                System.out.println("2: "+customer.getCustomerAccountNumber());
                        customer = new Customer();
	                System.out.println("3: "+customerKey+" "+customerMap.get(customerKey));
	                customer = customerMap.get(customerKey);
	                System.out.println("4: "+customer.getCustomerAccountNumber());
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
	    CustomerCache.putAll(customerMap);
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
 /*       
    public void loadCustomerCacheORM() throws IOException {
        System.out.println("LOADING DATA TO MEMORY");
        Integer customerKey;
        Map <Integer, Customer>customerMap = new HashMap<Integer, Customer>();
        Map <Integer, Location>locationMap = new HashMap<Integer, Location>();
        Map <Integer, Device>deviceMap = new HashMap<Integer, Device>();
        Map <Integer, Service>servicesMap = new HashMap<Integer, Service>();
        List <Device> deviceList = new ArrayList<Device>();
        List <Service> serviceList = new ArrayList<Service>();
        List<Customer> ctxManagedCustomer = new ArrayList<Customer>();
        List<Location> ctxManagedLocation = new ArrayList<Location>();
        List<Device> ctxManagedDevice = new ArrayList<Device>();
        List<Service> ctxManagedServices = new ArrayList<Service>();
        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory("TelecomDB");
        EntityManager em = emf.createEntityManager();
        try{
            String customerQuery = "SELECT C FROM Customer C ";
            String locationQuery = "SELECT L FROM Location L ";
            String deviceQuery = "SELECT D FROM Device D";
            String servicesQuery = "SELECT S FROM Service S";
            
            Query emCustomerQuery = em.createQuery(customerQuery);
            Query emLocationQuery = em.createQuery(locationQuery);
            Query emDeviceQuery = em.createQuery(deviceQuery);
            Query emServicesQuery = em.createQuery(servicesQuery);
            //emQuery.setParameter("accountNumber", customer.getCustomerAccountNumber());
            ctxManagedCustomer = emCustomerQuery.getResultList();
            ctxManagedLocation = emLocationQuery.getResultList();
            ctxManagedDevice = emDeviceQuery.getResultList();
            ctxManagedServices = emServicesQuery.getResultList();
              
            System.out.println("LOADING CUSTOMER CACHE");
            
            for(Customer customer: ctxManagedCustomer){
                customerMap.put(customer.getCustomerId(), customer);    
            }
            
            CustomerCache.putAll(customerMap);
            
            System.out.println("LOADING LOCATION CACHE");
            
            for(Location location: ctxManagedLocation){
                locationMap.put(location.getLocationId(), location);    
            }
            
            LocationCache.putAll(locationMap);
            
            System.out.println("LOADING DEVICE CACHE");
            
            for(Device device: ctxManagedDevice){
                deviceMap.put(device.getDeviceId(), device);    
            }
            
            DeviceCache.putAll(deviceMap);
        
            System.out.println("LOADING SERVICES CACHE");
            for(Service service: ctxManagedServices){
                servicesMap.put(service.getServiceId(), service);    
            }
            
            ServicesCache.putAll(servicesMap);
        }    
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            em.close();
        }
    }
*/
}