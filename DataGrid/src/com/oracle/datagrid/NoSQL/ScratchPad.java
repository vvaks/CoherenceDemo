package com.oracle.datagrid.NoSQL;

import com.oracle.datagrid.NoSQL.entity.*;

import com.oracle.datagrid.NoSQL.util.RemoteDistributedCacheLoader;

import com.oracle.datagrid.activeactive.entity.Sequence;
import com.oracle.datagrid.activeactive.util.SequenceProcessor;

import com.sleepycat.persist.impl.Store;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import oracle.kv.Direction;
import oracle.kv.KVStore;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.Key;
import oracle.kv.avro.AvroCatalog;
import oracle.kv.avro.SpecificAvroBinding;

public class ScratchPad {
    public static void main(String[] args) {
        final String INVOCATION_SERVICE_NAME = "InvocationService";
        InvocationService invocationService;
        invocationService = (InvocationService) CacheFactory.getConfigurableCacheFactory().ensureService(INVOCATION_SERVICE_NAME);
        Customer customer = new Customer();
        Location location = new Location();
        Sequence sequence;
        Name name = new Name();
        Device cm = new Device();
        Device mta = new Device();
        Service hbo = new Service();
        Service sho = new Service();
        Service hsd = new Service();
        Service voice = new Service();
        List<Customer> customerList = new ArrayList<Customer>();
        List<Location> customerLocations = new ArrayList<Location>();
        List<Device> customerDevices = new ArrayList<Device>();
        List<Service> customerServices = new ArrayList<Service>();
        Integer manualOveride = 54321;
        //String manualOveride = "54321";
        Boolean demoWriteThrough = true;
        Boolean demoRecovery = true;
        Boolean reloadGrid = false;
        
        NamedCache SequenceCache = CacheFactory.getCache("Sequence");
        NamedCache myCache = CacheFactory.getCache("AvroCustomerCache");
        if(reloadGrid /*&& (SequenceCache.size() <= 0 || SequenceCache == null)*/){
            SequenceCache.clear();
            myCache.clear();
            loadGrid();
        
            System.out.println("Sequence Cache Size: " + SequenceCache.size());
            sequence = (Sequence) SequenceCache.invoke(1, new SequenceProcessor()); 
        }
        //customer.setCustomerId(sequence.getMaxSequenceCustomer());
        //customer.setCustomerAccountNumber(sequence.getMaxSequenceCustomer());
        customer.setCustomerId(manualOveride);
        customer.setAccountNumber(manualOveride);
        name.setFirstName("Jane");
        name.setLastName("Doe");
        customer.setCustomerPhone("555-1212");
        customer.setCustomerName(name);
        customer.setCustomerLocation(location);
        location.setLocationId(1);
        //location.setCustomer(customer);
        location.setAddress("123 Market St.");
        location.setCity("Philadelphia");
        location.setState("PA");
        location.setPostalCode("19103");
        location.setCountry("USA");
        //cm.setDeviceId(sequence.getMaxSequenceDevice());
        cm.setDeviceId(1);
        //cm.setCustomer(customer);
        cm.setDeviceMake("Motarola");
        cm.setDeviceModel("CX9400");
        cm.setDeviceSerialNumber("11223344");
        cm.setDeviceStatus("Active");
        cm.setDeviceType("CM");
        cm.setIpAddress("10.10.10.1");
        cm.setMacAddress("aa-bb-cc-dd-ee");
        /*
        sequence = (Sequence) SequenceCache.invoke(1, new SequenceProcessor());
        mta.setDeviceId(2);
        mta.setCustomer(customer);
        mta.setDeviceMake("Motarola");
        mta.setDeviceModel("BX1000");
        mta.setDeviceSerialNumber("11223355");
        mta.setDeviceStatus("Active");
        mta.setDeviceType("MTA");
        mta.setIpAddress("10.10.10.2");
        mta.setMacAddress("ab-bc-cd-de-ef");*/
        hbo.setServiceId(0);
        hbo.setServiceCode("HBO123");
        hbo.setServiceDescription("HBO");
        hbo.setServiceLOB("Video");
        hbo.setServiceName("HBO Premium");
        hbo.setServiceRate("10.00");
        //sequence = (Sequence) SequenceCache.invoke(1, new SequenceProcessor());
        sho.setServiceId(1);
        sho.setServiceCode("SHO123");
        sho.setServiceDescription("Show Time");
        sho.setServiceLOB("Video");
        sho.setServiceName("Show Time Premium");
        sho.setServiceRate("15.00");
        //sequence = (Sequence) SequenceCache.invoke(1, new SequenceProcessor());
        hsd.setServiceId(2);
        hsd.setServiceCode("HSD123");
        hsd.setServiceDescription("Internet Service");
        hsd.setServiceLOB("HSD");
        hsd.setServiceName("High Speed Internet");
        hsd.setServiceRate("30.00");
        //sequence = (Sequence) SequenceCache.invoke(1, new SequenceProcessor());
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
        
        customer.setCustomerDevices(customerDevices);
        customer.setCustomerServices(customerServices);
        
        //NamedCache myCache = CacheFactory.getCache("AvroCustomerCache");
        
        if(demoWriteThrough){
            final Key key = Key.createKey(Arrays.asList("customer", "avro", "0000000001"),"Minor05");
            myCache.put(key, customer);
        }
        if(demoRecovery){
            KVStore store;
            Key nextKey;

            AvroCatalog catalog;
            SpecificAvroBinding<Customer> binding;
            String storeName = "kvstore";
            String hostName = "fmw02";
            String hostPort = "5000";
            Integer storeKeyCount = 0;
            
            /* Open the KVStore. */
            store = KVStoreFactory.getStore (new KVStoreConfig(storeName, hostName + ":" + hostPort));

            /* Create a specific binding for the MemberInfo class/schema. */
            catalog = store.getAvroCatalog();
            binding = catalog.getSpecificBinding(Customer.class);
            Iterator keyIterator = store.storeKeysIterator(Direction.UNORDERED, 0);
            
            while(keyIterator.hasNext()){
                nextKey = (Key)keyIterator.next();
                if(!myCache.containsKey(nextKey)){
                    System.out.println("Key " + nextKey + " has been lost and will be recovered");
                    myCache.get(nextKey);            
                }
            }
        }
    }        
    
    public static void loadGrid(){
        InvocationService service = (InvocationService) CacheFactory.getConfigurableCacheFactory().ensureService("InvocationService");
        service.query(new RemoteDistributedCacheLoader(), null);
        System.out.println("Cache Loading Complete");
    }
}