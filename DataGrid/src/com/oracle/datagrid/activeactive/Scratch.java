package com.oracle.datagrid.activeactive;

import com.oracle.datagrid.activeactive.entity.Customer;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import com.tangosol.util.filter.GreaterEqualsFilter;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Filter;

public class Scratch {
    public static void main(String[] args) {
    
    NamedCache cache = CacheFactory.getCache("OilRigAlerts");
    System.out.println(cache.size());
    }
}
