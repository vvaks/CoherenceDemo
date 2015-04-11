package com.oracle.datagrid.activeactive;

import com.oracle.datagrid.activeactive.entity.Customer;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class Scratch {
    public static void main(String[] args) {
    NamedCache cache = CacheFactory.getCache("East-Customer");
    System.out.println(((Customer)cache.get(54462)).getCustomerLocation().getPostalCode());
    
    }
}
