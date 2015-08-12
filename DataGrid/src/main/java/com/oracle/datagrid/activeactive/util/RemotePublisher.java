package com.oracle.datagrid.activeactive.util;

import com.oracle.datagrid.activeactive.entity.Customer;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;

import java.io.IOException;


public class RemotePublisher implements Invocable, PortableObject{
	String cacheName;
	long key;
	Customer customer;
	
	public RemotePublisher(){}
	
	public Object getResult() {
		return null;
	}

	public void init(InvocationService invocationService) {
		
	}
	
	public void run(){
        NamedCache        cache   = CacheFactory.getCache(cacheName);
        //CacheService      service = cache.getCacheService();
        //BackingMapManager manager = service.getBackingMapManager();
        
        //if (manager instanceof DefaultConfigurableCacheFactory.Manager){
        //    ReadWriteBackingMap map = (ReadWriteBackingMap) ((DefaultConfigurableCacheFactory.Manager) manager).getBackingMap(cacheName);
        //}
    
        customer.setDbmsPersist(false);
        System.out.println("Writing Customer key: " + key + " On cache:" + cacheName);
        cache.put(key, customer);
	}

	public String getCacheName(){
		return cacheName;
	}
	
	public long getkey(){
		return key;
	}
	public Customer getCustomer(){
		return customer;
	}
	
	public void setCacheName(String cacheName){
		this.cacheName = cacheName;
	}
	public void setkey(long key){
		this.key = key;
	}
	public void setCustomer(Customer customer){
		this.customer = customer;
	}
	
	public void readExternal(PofReader reader) throws IOException {
		reader.registerIdentity(getCustomer());
		cacheName = reader.readString(0);
		key = reader.readLong(1);
		customer = (Customer) reader.readObject(2);
	}

	public void writeExternal(PofWriter writer) throws IOException {
		if (cacheName != null) { 
			writer.writeString(0, cacheName);  
		}
		if (key != 0) { 
			writer.writeLong(1, key);  
		}
		if (customer != null) { 
			writer.writeObject(2, customer);  
		}
	}
}