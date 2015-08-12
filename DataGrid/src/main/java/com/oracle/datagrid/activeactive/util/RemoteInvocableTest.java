package com.oracle.datagrid.activeactive.util;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;

import java.io.IOException;

import java.util.Map;


@SuppressWarnings("serial")
public class RemoteInvocableTest implements Invocable, PortableObject{
	String cacheName = "DataGridEastReplicated";
	Map memberSet;
	
	public RemoteInvocableTest(String method){
		System.out.println(method);
	}
	
	public Object getResult() {
		return memberSet;
	}

	public void init(InvocationService invocationService) {
		
	}
	
	public void run(){
		System.out.println("Running Invocable");
		NamedCache cache = CacheFactory.getCache("customerEast");
		//memberSet = (Map) cache.getCacheService().getCluster().getMemberSet();
		memberSet = (Map) cache.getCacheService().getCluster().getMemberSet();
		
		String s = "";
		for(int i = 0; i<10; i++){
			s = s + i;
			System.out.println(s);
		}	
		/**
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NamedCache  cache   = CacheFactory.getCache(cacheName);
		System.out.println("Locking target record");
        cache.lock("key", -1);
        try{
        	cache.put("key", 81);
            System.out.println("Writing target record on cache  " + cacheName);	
        }
        finally{
        	System.out.println("Unlocking target record");
        	cache.unlock("key");
        }**/
	}
	
	public void setCacheName(String cacheName){
		this.cacheName = cacheName;
	}
	
	public void readExternal(PofReader reader) throws IOException {
		cacheName = reader.readString(0);
		//memberSet = (Set) reader.readCollection(1, memberSet);
		memberSet = reader.readMap(1, memberSet);
	}

	public void writeExternal(PofWriter writer) throws IOException {
		if (cacheName != null) { 
			writer.writeString(0, cacheName);  
		}
			//writer.writeCollection(1, memberSet);
			writer.writeMap(1, memberSet);
	}
}