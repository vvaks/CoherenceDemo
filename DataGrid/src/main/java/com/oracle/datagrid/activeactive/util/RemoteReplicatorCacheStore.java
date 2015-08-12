package com.oracle.datagrid.activeactive.util;

import com.oracle.datagrid.activeactive.entity.Customer;

import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationObserver;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.util.Binary;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.ExternalizableHelper;
import com.tangosol.util.ObservableMap;

import java.util.Arrays;
import java.util.Set;

import oracle.kv.Key;

//import projects.aupm.agaree.entity.*;


public class RemoteReplicatorCacheStore implements BinaryEntryStore{
	private static final String INVOCATION_SERVICE_NAME = "InvocationService";
	private static final String PERSISTENCE_QUEUE_CACHE_NAME = "Customer";
        private static final String PERSISTENCE_NOSQL_CACHE_NAME = "AvroCustomerCache";
	private static final String REMOTE_REPLICATED_CUSTOMER_EAST_CACHE_NAME = "East-Customer";
	private static final String REMOTE_REPLICATED_CUSTOMER_WEST_CACHE_NAME = "West-Customer";
        private static final String REMOTE_REPLICATED_LOCATION_EAST_CACHE_NAME = "East-Location";
        private static final String REMOTE_REPLICATED_LOCATION_WEST_CACHE_NAME = "West-Location";
        private static final String REMOTE_REPLICATED_DEVICE_EAST_CACHE_NAME = "East-Device";
        private static final String REMOTE_REPLICATED_DEVICE_WEST_CACHE_NAME = "West-Device";
        private static final String REMOTE_REPLICATED_SERVICES_EAST_CACHE_NAME = "East-Services";
        private static final String REMOTE_REPLICATED_SERVICES_WEST_CACHE_NAME = "West-Services";  
	private static final String GRID_ENVIRONMENT_NAME = "GridEnvironment";
	//private static final String EAST_GRID_ENVIRONMENT_NAME = "GridEnvironmentEast";
	//private static final String WEST_GRID_ENVIRONMENT_NAME = "GridEnvironmentWest";
	private NamedCache remoteReplicatedCache;
        private NamedCache remoteCustomerCache;
        private NamedCache remoteLocationCache;
        private NamedCache remoteDeviceCache;
        private NamedCache remoteServicesCache;
	private NamedCache localGridEnvironmentCache;
	private NamedCache remoteGridEnvironmentCache;
	private BackingMapManagerContext backingMapCtx;
	private InvocationService invocationService;
	private InvocationObserver loaderObserver;
        String cacheName;
        Boolean replicateEntry;
	
	public RemoteReplicatorCacheStore(){}
	public RemoteReplicatorCacheStore(BackingMapManagerContext ctx, String cacheName){
		this.backingMapCtx = ctx;
                this.cacheName = cacheName;
		//invocationService = (InvocationService) CacheFactory.getConfigurableCacheFactory().ensureService(INVOCATION_SERVICE_NAME);
		
		System.out.println("Current Object Type is: " + cacheName);
		localGridEnvironmentCache = CacheFactory.getCache(GRID_ENVIRONMENT_NAME);	
	}
        public RemoteReplicatorCacheStore(BackingMapManagerContext ctx, String cacheName, Boolean replicateEntry){
            this.backingMapCtx = ctx;
            this.cacheName = cacheName;
            this.replicateEntry = replicateEntry;
            
            System.out.println("Current Object Type is: " + cacheName);
            System.out.println("ReplicateEntry is set to: " + replicateEntry);
            localGridEnvironmentCache = CacheFactory.getCache(GRID_ENVIRONMENT_NAME);       
        }
	
	public void erase(BinaryEntry binaryEntry) {
	    Integer key = (Integer) binaryEntry.getKey();
	    Binary binaryKey = binaryEntry.getBinaryKey();
	    Binary binaryValue = binaryEntry.getBinaryValue();
	    String siteName = backingMapCtx.getCacheService().getCluster().getLocalMember().getSiteName();
	    
	    System.out.println("Initiating CacheStore Store Entry Processing");
	    Boolean replicationStatus = (Boolean) localGridEnvironmentCache.get("ReplicationStatus");
	    
	    System.out.println("Replication Status: " + replicationStatus);
	    if(replicationStatus == null){
                replicationStatus = false;
	    }
	    
	    System.out.println("Checking whether Customer has already been replicated");
	    
	    if(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_CUSTOM) != Binary.NO_BINARY && replicationStatus.booleanValue() && (Boolean)localGridEnvironmentCache.get("ActiveActiveDemo")){
	        System.out.println("Decoration not detected, Customer has not been replicated... Replicating Customer....");
	        System.out.println("Replicating Customer....");    
                    if (siteName.equalsIgnoreCase("East")){
	                if(cacheName.equalsIgnoreCase("East-Customer")){
	                    remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_CUSTOMER_WEST_CACHE_NAME);
	                }
	                else if(cacheName.equalsIgnoreCase("East-Location")){
	                    remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_LOCATION_WEST_CACHE_NAME);
	                }
	                else if(cacheName.equalsIgnoreCase("East-Device")){
	                    remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_DEVICE_WEST_CACHE_NAME);
	                }
	                else if(cacheName.equalsIgnoreCase("East-Services")){
	                    remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_SERVICES_WEST_CACHE_NAME);
	                }
	            }
	            else if(siteName.equalsIgnoreCase("West")){
	                if(cacheName.equalsIgnoreCase("West-Customer")){
	                    remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_CUSTOMER_EAST_CACHE_NAME);
	                }
	                else if(cacheName.equalsIgnoreCase("West-Location")){
	                    remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_LOCATION_EAST_CACHE_NAME);
	                }
	                else if(cacheName.equalsIgnoreCase("West-Device")){
	                    remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_DEVICE_EAST_CACHE_NAME);
	                }
	                else if(cacheName.equalsIgnoreCase("West-Services")){
	                    remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_SERVICES_EAST_CACHE_NAME);
	                }
	            }
                    Binary keypassedDecoBinary = new Binary();
                    Binary deleteDecoBinary = new Binary();
                    keypassedDecoBinary = ExternalizableHelper.toBinary(001);
	            deleteDecoBinary = ExternalizableHelper.toBinary(002);
                    
                    System.out.println("Checking whether customer should be processed at this site...");
                    if(siteName.equalsIgnoreCase("West") && isEven(key)){
	                System.out.println("Current Site is West and Customer Key is Even.... ");
	                System.out.println("Checking if customer was passed....");
                        System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1) + "......." + keypassedDecoBinary);
	                System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_2) + "......." + deleteDecoBinary);
	                if(!keypassedDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1))){
	                    System.out.println("Calling service for Remote Cache:" + remoteReplicatedCache.getCacheName());
                            binaryValue = ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_CUSTOM, Binary.NO_BINARY);
	                    binaryValue = ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_APP_2, deleteDecoBinary);
                            remoteReplicatedCache.invoke(key, new RemoteProcessor(binaryValue, "Erase"));
	                }
	                System.out.println("Remote Processor completed sucessfully");
	                ObservableMap backingMap = this.backingMapCtx.getBackingMapContext(PERSISTENCE_QUEUE_CACHE_NAME).getBackingMap();   
	                /** Create a copy of the Binary to ensure no reference conflict between caches**/
	                Binary persitedBinaryValue = new Binary(binaryValue);
	                backingMap.remove(persitedBinaryValue);
	            }
	            else if(siteName.equalsIgnoreCase("East") && !isEven(key)){
	                /**Invoke EntryProcessor using the decorated Binary representation of Customer **/
	                System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1) + "......." + keypassedDecoBinary);
	                if(!keypassedDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1))){
	                    System.out.println("Calling service for Remote Cache:" + remoteReplicatedCache.getCacheName());
	                    binaryValue = ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_CUSTOM, Binary.NO_BINARY);
	                    binaryValue = ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_APP_2, deleteDecoBinary);
	                    remoteReplicatedCache.invoke(key, new RemoteProcessor(binaryValue, "Erase"));
	                }
	                System.out.println("Remote Processor completed sucessfully");
	                ObservableMap backingMap = this.backingMapCtx.getBackingMapContext(PERSISTENCE_QUEUE_CACHE_NAME).getBackingMap();   
	                /** Create a copy of the Binary to ensure no reference conflict between caches**/
	                Binary persitedBinaryValue = new Binary(binaryValue);
	                backingMap.remove(persitedBinaryValue);
	            }
	            else{
	                System.out.println("### This key should not be processed at this site ###");
	                System.out.println("Calling service for Remote Cache: " + remoteReplicatedCache.getCacheName() + " to handle the processing of this key");
	                /**Invoke EntryProcessor using the undecorated Binary representation of Customer **/
	                System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1) + "......." + keypassedDecoBinary);
	                if(!keypassedDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1))){
	                    binaryValue = ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_APP_1, keypassedDecoBinary);
	                    binaryValue = ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_APP_2, deleteDecoBinary);
	                    System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1));
	                    System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_2));
                            remoteReplicatedCache.invoke(key, new RemoteProcessor(binaryValue, "Erase"));
	                }
	                else if(keypassedDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1))){ 
	                    System.out.println("### This Key has already been processed at the owner site... DO NOT REPLICATE ###");
	                }
	                System.out.println("Remote Processor completed sucessfully");
	            }
	    }
            else{
                Binary deleteDecoBinary = new Binary();
                if(deleteDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_2))){
                    ObservableMap backingMap = this.backingMapCtx.getBackingMapContext(PERSISTENCE_QUEUE_CACHE_NAME).getBackingMap();   
                    backingMap.remove(binaryKey);
                }
            }
	    System.out.println("Completed CacheStore Store Entry Processing");
        }
	
	public void eraseAll(Set arg0) {
		
	}

	public void load(BinaryEntry arg0) {
		
	}

	public void loadAll(Set arg0) {
		
	}
	
	@SuppressWarnings("unchecked")
	public void store(BinaryEntry binaryEntry) {
		Integer key = (Integer) binaryEntry.getKey();
		Binary binaryKey = binaryEntry.getBinaryKey();
		Binary binaryValue = binaryEntry.getBinaryValue();
                String siteName = backingMapCtx.getCacheService().getCluster().getLocalMember().getSiteName();
                //BackingMapManager backMapManager = GridEnvironmentCache.getCacheService().getBackingMapManager();
		//BackingMapManagerContext backMapManagerCtx = backMapManager.getContext();
		//BackingMapContext GEBackMapCtx = backMapManagerCtx.getBackingMapContext(GridEnvironmentCache.getCacheName());
		//ObservableMap GEBackMap = GEBackMapCtx.getBackingMap();
		//Boolean replicationStatus =  (Boolean) ExternalizableHelper.fromBinary((Binary)GEBackMap.get("ReplicationStatus"));
		
		System.out.println("Initiating CacheStore Store Entry Processing");
		Boolean replicationStatus = (Boolean) localGridEnvironmentCache.get("ReplicationStatus");
		
		System.out.println("Replication Status: " + replicationStatus);
		if(replicationStatus == null){
			replicationStatus = false;
		}
		
                System.out.println("#### - Checking whether Customer has already been processed at at remote site....");
                
		if(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_CUSTOM) != Binary.NO_BINARY && replicationStatus.booleanValue()){
                    System.out.println("#### - Decoration not detected, customer has not been yet been processed at a remote site....");
                    System.out.println("#### - Checking if customer should be processed at this site or be referred to a remote site for processing....");
                        if (siteName.equalsIgnoreCase("East")){
                            if(cacheName.equalsIgnoreCase("East-Customer")){
                                remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_CUSTOMER_WEST_CACHE_NAME);
                            }
                            else if(cacheName.equalsIgnoreCase("East-Location")){
                                remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_LOCATION_WEST_CACHE_NAME);
                            }
                            else if(cacheName.equalsIgnoreCase("East-Device")){
                                remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_DEVICE_WEST_CACHE_NAME);
                            }
                            else if(cacheName.equalsIgnoreCase("East-Services")){
                                remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_SERVICES_WEST_CACHE_NAME);
                            }
			}
			else if(siteName.equalsIgnoreCase("West")){
			    if(cacheName.equalsIgnoreCase("West-Customer")){
			        remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_CUSTOMER_EAST_CACHE_NAME);
			    }
			    else if(cacheName.equalsIgnoreCase("West-Location")){
			        remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_LOCATION_EAST_CACHE_NAME);
			    }
			    else if(cacheName.equalsIgnoreCase("West-Device")){
			        remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_DEVICE_EAST_CACHE_NAME);
			    }
			    else if(cacheName.equalsIgnoreCase("West-Services")){
			        remoteReplicatedCache = CacheFactory.getCache(REMOTE_REPLICATED_SERVICES_EAST_CACHE_NAME);
			    }
			}
                        
                        Binary keypassedDecoBinary = new Binary();
                        Binary deleteDecoBinary = new Binary();
                        keypassedDecoBinary = ExternalizableHelper.toBinary(001);
                        deleteDecoBinary = ExternalizableHelper.toBinary(002); 
                        
                        System.out.println("#### - Checking whether customer should be processed by the current site.... ");
                        if(siteName.equalsIgnoreCase("West") && isEven(key)){
                            System.out.println("#### - Current site is WEST and the current customer key is EVEN.... ");
                            System.out.println("#### - Processing Customer....");
                            System.out.println("#### - Checking whether the customer was refered to this site for processing from a remote site... ");
                            System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1) + "......." + keypassedDecoBinary);
                            if(!keypassedDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1))){
                                System.out.println("#### - The customer was NOT refered to this site from a remote site.... ");
                                System.out.println("#### - Replicating Customer to remote site before proceeding with processing.... ");
                                System.out.println("#### - Calling service for Remote Cache:" + remoteReplicatedCache.getCacheName());
                                remoteReplicatedCache.invoke(key, new RemoteProcessor(ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_CUSTOM, Binary.NO_BINARY), "Store"));
                                System.out.println("#### - Remote Processor completed sucessfully... customer has been replicated to reomote site");
                            }
                            else{
                                System.out.println("#### - The customer WAS refered to this site from a remote site.... ");
                                System.out.println("#### - There is no need to replicate the customer.... ");
                            }
                            ObservableMap backingMap = this.backingMapCtx.getBackingMapContext(PERSISTENCE_QUEUE_CACHE_NAME).getBackingMap();	
                            /** Create a copy of the Binary to ensure no reference conflict between caches**/
                            Binary persitedBinaryValue = new Binary(binaryValue);
                            if(deleteDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_2))){
                                //TODO Implement Remove Functionality
                                backingMap.put(binaryKey, persitedBinaryValue);
                                //backingMap.remove(persitedBinaryValue);
                            }
                            else{
                                System.out.println("#### - Persisting customer to the database asynchronously....");
                                backingMap.put(binaryKey, persitedBinaryValue);
                            }
                        }
                        else if(siteName.equalsIgnoreCase("East") && !isEven(key)){
                            System.out.println("#### - Current site is EAST and the current customer key is ODD.... ");
                            System.out.println("#### - Processing Customer....");
                            System.out.println("#### - Checking whether the customer was refered to this site for processing from a remote site... ");
                            System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1) + "......." + keypassedDecoBinary);
                            if(!keypassedDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1))){
                                System.out.println("#### - The customer was NOT refered to this site from a remote site.... ");
                                System.out.println("#### - Replicating Customer to remote site before proceeding with processing.... ");
                                System.out.println("#### - Calling service for Remote Cache:" + remoteReplicatedCache.getCacheName());
                                remoteReplicatedCache.invoke(key, new RemoteProcessor(ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_CUSTOM, Binary.NO_BINARY), "Store"));
                                System.out.println("#### - Remote Processor completed sucessfully... customer has been replicated to reomote site");
                            }
                            else{
                                System.out.println("#### - The customer WAS refered to this site from a remote site.... ");
                                System.out.println("#### - There is no need to replicate the customer.... ");
                            }
                            ObservableMap backingMap = this.backingMapCtx.getBackingMapContext(PERSISTENCE_QUEUE_CACHE_NAME).getBackingMap();   
                            /** Create a copy of the Binary to ensure no reference conflict between caches**/
                            Binary persitedBinaryValue = new Binary(binaryValue);
                            if(deleteDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_2))){
                                //TODO Implement Remove Functionality
                                backingMap.put(binaryKey, persitedBinaryValue);
                                //backingMap.remove(persitedBinaryValue);
                            }
                            else{
                                System.out.println("#### - Persisting customer to the database asynchronously....");
                                backingMap.put(binaryKey, persitedBinaryValue);
                            }
                        }
                        else{
                            if(siteName.equalsIgnoreCase("West")){
                                System.out.println("#### - Current site is WEST but the current customer key is ODD.... ");
                            }
                            else if(siteName.equalsIgnoreCase("East")){
                                System.out.println("#### - Current site is EAST but the current customer key is EVEN.... ");
                            }
                            System.out.println("#### - This customer should not be processed at this site");
                            System.out.println("#### - Checking whether this customer has already been processed at the owner site.... ");
                            System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1) + "......." + keypassedDecoBinary);
                            if(!keypassedDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1))){
                                System.out.println("#### - The customer was NOT processed at the owner site.... ");
                                System.out.println("#### - Refering customer to the correct site for processing.... ");
                                System.out.println("#### - Calling service for Remote Cache:" + remoteReplicatedCache.getCacheName());
                                binaryValue = ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_APP_1, keypassedDecoBinary);
                                System.out.println(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1));
                                remoteReplicatedCache.invoke(key, new RemoteProcessor(binaryValue, "Store"));
                            }
                            else if(keypassedDecoBinary.equals(ExternalizableHelper.getDecoration(binaryValue, ExternalizableHelper.DECO_APP_1))){ 
                                System.out.println("#### - This customer has already been processed at the owner site... DO NOT REPLICATE");
                            }
                            System.out.println("#### - Customer has been sucessfully processed at the correct owner site... ");
                        }
            }
            else{
                /*Binary deleteDecoBinary = new Binary();
                deleteDecoBinary = ExternalizableHelper.toBinary(002); 
                ObservableMap backingMap = this.backingMapCtx.getBackingMapContext(PERSISTENCE_QUEUE_CACHE_NAME).getBackingMap();   
                backingMap.put(binaryKey, binaryValue);
                binaryValue = ExternalizableHelper.decorate(binaryValue, ExternalizableHelper.DECO_APP_2, deleteDecoBinary);
                //backingMap.remove(binaryKey);*/
            }
            //Persist to NoSQL
            //persistNoSQL(binaryValue);
            System.out.println("Completed CacheStore Store Entry Processing");
        }	

    public void storeAll(Set arg0) {
		
    }
    
    private void persistNoSQL(Binary binaryValue){
        Customer customer;
        com.oracle.datagrid.NoSQL.entity.Customer noSQLCustomer;
        ObservableMap backingMap = this.backingMapCtx.getBackingMapContext(PERSISTENCE_NOSQL_CACHE_NAME).getBackingMap();
        customer = (Customer)ExternalizableHelper.fromBinary(binaryValue);
        noSQLCustomer = convertCustomerNoSQL(customer);
        
        Key key = Key.createKey(Arrays.asList("customer", customer.getCustomerAccountNumber().toString()));
        backingMap.put(key, noSQLCustomer);
    }
            
    private com.oracle.datagrid.NoSQL.entity.Customer convertCustomerNoSQL(Customer customer){
        com.oracle.datagrid.NoSQL.entity.Customer noSqlCustomer= new com.oracle.datagrid.NoSQL.entity.Customer();
        com.oracle.datagrid.NoSQL.entity.Name name = new com.oracle.datagrid.NoSQL.entity.Name();
        com.oracle.datagrid.NoSQL.entity.Location location = new com.oracle.datagrid.NoSQL.entity.Location();
        
        noSqlCustomer.setCustomerId(customer.getCustomerAccountNumber());
        noSqlCustomer.setAccountNumber(customer.getCustomerAccountNumber());
        name.setFirstName(customer.getCustomerFirstName());
        name.setLastName(customer.getCustomerLastName());
        noSqlCustomer.setCustomerPhone(customer.getCustomerPhone());
        noSqlCustomer.setCustomerName(name);
        noSqlCustomer.setCustomerLocation(location);
        location.setLocationId(customer.getCustomerLocation().getLocationId());
        location.setAddress(customer.getCustomerLocation().getAdress());
        location.setCity(customer.getCustomerLocation().getCity());
        location.setState(customer.getCustomerLocation().getState());
        location.setPostalCode(customer.getCustomerLocation().getPostalCode());
        location.setCountry(customer.getCustomerLocation().getCountry());
        /*
        cm.setDeviceId(1);
        cm.setDeviceMake("Motarola");
        cm.setDeviceModel("CX9400");
        cm.setDeviceSerialNumber("11223344");
        cm.setDeviceStatus("Active");
        cm.setDeviceType("CM");
        cm.setIpAddress("10.10.10.1");
        cm.setMacAddress("aa-bb-cc-dd-ee");

        mta.setDeviceId(2);
        mta.setCustomer(customer);
        mta.setDeviceMake("Motarola");
        mta.setDeviceModel("BX1000");
        mta.setDeviceSerialNumber("11223355");
        mta.setDeviceStatus("Active");
        mta.setDeviceType("MTA");
        mta.setIpAddress("10.10.10.2");
        mta.setMacAddress("ab-bc-cd-de-ef");
        hbo.setServiceId(0);
        hbo.setServiceCode("HBO123");
        hbo.setServiceDescription("HBO");
        hbo.setServiceLOB("Video");
        hbo.setServiceName("HBO Premium");
        hbo.setServiceRate("10.00");
        
        sho.setServiceId(1);
        sho.setServiceCode("SHO123");
        sho.setServiceDescription("Show Time");
        sho.setServiceLOB("Video");
        sho.setServiceName("Show Time Premium");
        sho.setServiceRate("15.00");
        
        hsd.setServiceId(2);
        hsd.setServiceCode("HSD123");
        hsd.setServiceDescription("Internet Service");
        hsd.setServiceLOB("HSD");
        hsd.setServiceName("High Speed Internet");
        hsd.setServiceRate("30.00");
        
        voice.setServiceId(3);
        voice.setServiceCode("VOICE123");
        voice.setServiceDescription("VOIP Home Phone Service");
        voice.setServiceLOB("VOICE");
        voice.setServiceName("Digital Voice");
        voice.setServiceRate("25.00");
        
        customerDevices.add(cm);
        //customerDevices.add(mta);
        customerServices.add(hbo);
        customerServices.add(sho);
        customerServices.add(hsd);
        customerServices.add(voice);
        
        noSqlCustomer.setCustomerDevices(customerDevices);
        noSqlCustomer.setCustomerServices(customerServices);
        */
        return noSqlCustomer;                
    }
    
    private boolean isEven(Integer currentSequence){
            if(currentSequence % 2 == 0){   
                    return true;
            }
            else{
                    return false;
            }
    }
}