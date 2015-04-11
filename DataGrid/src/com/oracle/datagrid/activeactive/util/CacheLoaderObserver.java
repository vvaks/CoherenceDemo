package com.oracle.datagrid.activeactive.util;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationObserver;
import com.tangosol.net.Member;
import com.tangosol.net.NamedCache;

public class CacheLoaderObserver implements InvocationObserver {
	private final String GRID_ENVIRONMENT_NAME = "GridEnvironment";
	NamedCache localGridEnvironmentCache;
	Boolean escape = false;
	public CacheLoaderObserver(){
		localGridEnvironmentCache = CacheFactory.getCache(GRID_ENVIRONMENT_NAME);
	}
	public synchronized void memberCompleted(Member member, Object result) {
		localGridEnvironmentCache.put("RemoteGridLoding", Boolean.FALSE);
		localGridEnvironmentCache.put("RemoteGridLoded", Boolean.TRUE);
		localGridEnvironmentCache.put("ReplicationStatus", Boolean.TRUE);
		localGridEnvironmentCache.put("InvocationComplete", Boolean.TRUE);
		System.out.println("Remote Grid Loaded Sucessfully");
	}

	public synchronized void memberFailed(Member member, Throwable eFailure) {
		localGridEnvironmentCache.put("RemoteGridLoding", Boolean.FALSE);
		localGridEnvironmentCache.put("RemoteGridLoded", Boolean.FALSE);
		localGridEnvironmentCache.put("ReplicationStatus", Boolean.FALSE);
		localGridEnvironmentCache.put("InvocationComplete", Boolean.TRUE);
		System.out.println("Remote Grid Loading Unsucessful");
	}

	public synchronized void memberLeft(Member member) {
		// TODO Auto-generated method stub
	
	}

	public synchronized void invocationCompleted() {
		localGridEnvironmentCache.put("InvocationComplete", Boolean.TRUE);
		System.out.println("Remote Grid Loading Complete");
		escape = true;
	}
	
	public synchronized Boolean getResults() {  
			while(!escape) {
		       try {
		           Thread.sleep(10);
		       } catch (InterruptedException e) {
		           Thread.currentThread().interrupt();
		       }
		   }
		   return true;
	} 
}