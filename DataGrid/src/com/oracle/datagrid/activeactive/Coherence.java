package com.oracle.datagrid.activeactive;

import com.oracle.datagrid.activeactive.entity.Customer;
import com.oracle.datagrid.activeactive.entity.Device;
import com.oracle.datagrid.activeactive.entity.Location;
import com.oracle.datagrid.activeactive.entity.Name;
import com.oracle.datagrid.activeactive.entity.Sequence;
import com.oracle.datagrid.activeactive.entity.Service;
import com.oracle.datagrid.activeactive.util.RemoteDistributedCacheLoader;
import com.oracle.datagrid.activeactive.util.SequenceProcessor;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.NotFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

/*
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
*/

public class Coherence {

    static {
        System.out.println("**************************** Initializing Cache *****************************************");
        final String INVOCATION_SERVICE_NAME = "InvocationService";
        NamedCache localGridEnvironmentCache = null;
        InvocationService invocationService;
        invocationService = (InvocationService) CacheFactory.getConfigurableCacheFactory().ensureService(INVOCATION_SERVICE_NAME);
        NamedCache SequenceCache = CacheFactory.getCache("Sequence");
        
        if(SequenceCache.size() <= 0 || SequenceCache == null){
            loadGrid();
        }
        System.out.println("**************************** Exiting Initializing Cache *****************************************");
    }
    
   
    
    public Customer getCustomerById(Integer customerID, String site) throws Exception {
        Coherence c = new Coherence();
      
        List<Customer> searchResults = null;
        Customer cust = null;
        try {
            searchResults = c.getCustomers("all", site);
            
            for(int i = 0; i < searchResults.size(); i++) {
                cust = searchResults.get(i);
                if (cust.getCustomerId().intValue() == customerID) {
                    break;
                } else {
                    cust = null;
                }
                           
            }
        } catch(Exception e) {
           throw new Exception("Error in getting customer by Id");
        }
            
        
        return cust;
    }
    
    public void updateCustomer(String fname, String lname, String address,
                               String city, String state, String pin,
                               String telephone, String country,
                               String devices, String services, String customerID) throws Exception {
        String site = null;
         StringTokenizer token = null;
         String id = null;
         Device tmpDevice = new Device();
         Service tmpService = new Service();
         List<Device> customerDevices = new ArrayList<Device>();
         List<Service> customerServices = new ArrayList<Service>();
         site = getClusterLocation();
        Customer cust = this.getCustomerById(new Integer(customerID), site);
         cust.getCustomerName().setFirstName(fname);
         cust.getCustomerName().setLastName(lname);
         cust.setCustomerPhone(telephone);
         cust.getCustomerLocation().setAdress(address);
         cust.getCustomerLocation().setCity(city);
         cust.getCustomerLocation().setCountry(country);
         cust.getCustomerLocation().setPostalCode(pin);
         cust.getCustomerLocation().setState(state);
         
         cust.setCustomerDevices(new ArrayList<Device>());
         cust.setCustomerServices(new ArrayList<Service>());
         
         token = new StringTokenizer(devices, ",");
         while (token.hasMoreElements()) {
             id = token.nextElement().toString();
             if (!id.equalsIgnoreCase("")) {
                 Integer deviceId = new Integer(id);
                 tmpDevice = this.getDeviceByID(deviceId);
                 tmpDevice.setCustomer(cust);
                 cust.getCustomerDevices().add(tmpDevice);
             }
         }
         
         token = new StringTokenizer(services, ",");
         while (token.hasMoreElements()) {
             id = token.nextElement().toString();
             if (!id.equalsIgnoreCase("")) {
                 Integer serviceId = new Integer(id);
                 tmpService = this.getServiceByID(serviceId);
                 cust.getCustomerServices().add(tmpService);
             }
         }
         
         NamedCache myCache = CacheFactory.getCache(site + "-Customer");
         myCache.put(cust.getCustomerAccountNumber(), cust);
     }

    public void createCustomer(String fname, String lname, String address,
                               String city, String state, String pin,
                               String telephone, String country,
                               String devices, String services) throws Exception  {
        String id = null;
        StringTokenizer token = null;
        final String INVOCATION_SERVICE_NAME = "InvocationService";
        InvocationService invocationService;
        invocationService =
                (InvocationService)CacheFactory.getConfigurableCacheFactory().ensureService(INVOCATION_SERVICE_NAME);
        Customer customer = new Customer();
        Location location = new Location();
        Sequence sequence;
        Device tmpDevice = new Device();
        Service tmpService = new Service();
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
        //Integer manualOveride = 54462;
        Integer manualOveride = 54321;
        String site;

        NamedCache SequenceCache = CacheFactory.getCache("Sequence");        
        site = getClusterLocation();
        System.out.println("Sequence Cache Size: " + SequenceCache.size());
        sequence = (Sequence)SequenceCache.invoke(1, new SequenceProcessor());
        customer.setCustomerId(sequence.getMaxSequenceCustomer());
        customer.setCustomerAccountNumber(sequence.getMaxSequenceCustomer());
        //customer.setCustomerId(manualOveride);
        //customer.setCustomerAccountNumber(manualOveride);
        name.setFirstName(fname);
        name.setLastName(lname);
        customer.setCustomerPhone(telephone);
        customer.setCustomerName(name);
        customer.setCustomerLocation(location);
        location.setLocationId(sequence.getMaxSequenceLocation());
        location.setCustomer(customer);
        location.setAdress(address);
        location.setCity(city);
        location.setState(state);
        location.setPostalCode(pin);
        location.setCountry(country);


        token = new StringTokenizer(devices, ",");
        while (token.hasMoreElements()) {
            id = token.nextElement().toString();
            if (!id.equalsIgnoreCase("")) {
                Integer deviceId = new Integer(id);
                tmpDevice = this.getDeviceByID(deviceId);
                tmpDevice.setCustomer(customer);
                customerDevices.add(tmpDevice);
            }
       }
        
        token = new StringTokenizer(services, ",");
        while (token.hasMoreElements()) {
            id = token.nextElement().toString();
            if (!id.equalsIgnoreCase("")) {
                Integer serviceId = new Integer(id);
                tmpService = this.getServiceByID(serviceId);
                customerServices.add(tmpService);
            }
        }
    
        customer.setCustomerDevices(customerDevices);
        customer.setCustomerServices(customerServices);


        System.out.println((customer.getCustomerDevices().get(0)).getCustomer().getCustomerAccountNumber());
        List<Location> storedLocations = getLocation("all", site);
        List<Device> storedDevices = getDevice("all", site);
        List<Service> storedServices = getServices("all", site);
        List<Customer> storedCustomers = getCustomers("all", site);

        serviceCustomer(customer, site);

    }

    public Device getDeviceByID(Integer deviceID) throws Exception {
        String site = getClusterLocation();
        //List<Device> customerDevices = new ArrayList<Device>();
        List<Device> storedDevices = getDevice("all", site);
        Device d = null;
        for (int i = 0; i < storedDevices.size(); i++) {
            d = storedDevices.get(i);
            if (d.getDeviceId().intValue() == deviceID.intValue()) {
                break;
            } else {
                d = null;
            }
        }
        return d;
    }

    public Service getServiceByID(Integer serviceID) throws Exception {
        String site = getClusterLocation();
        //List<Device> customerDevices = new ArrayList<Device>();
        List<Service> storedServices = getServices("all", site);
        Service s = null;
        for (int i = 0; i < storedServices.size(); i++) {
            s = storedServices.get(i);
            if (s.getServiceId().intValue() == serviceID.intValue()) {
                break;
            } else {
                s = null;
            }
        }
        return s;
    }
    
    public static void serviceCustomer(Customer customer, String siteName) {
        //CacheFactory.ensureCluster();
        System.out.println("Service Customer Operation: " + customer.getCustomerAccountNumber() + ":" + customer.getCustomerFirstName());
        NamedCache persistenceCache = CacheFactory.getCache("Customer");
        NamedCache myCache = CacheFactory.getCache(siteName + "-Customer");

        //Customer temp = (Customer)myCache.get("54321");
        Customer tempCustomer =
            (Customer)myCache.get(customer.getCustomerAccountNumber());
        if (tempCustomer != null) {
            System.out.println("************** 1");
            customer.setCustomerPhone("555-1111");
            myCache.put(tempCustomer.getCustomerAccountNumber(), customer);
            System.out.println("Service Customer Update Operation Completed successfully");
        } else {
            System.out.println("************** 2");
            //myCache.remove(customer.getCustomerAccountNumber());
            System.out.println(customer.getCustomerDevices().get(0).getDeviceId());
            System.out.println(customer.getCustomerDevices().get(0).getCustomer().getCustomerFirstName());
            System.out.println(customer.getCustomerLocation().getAdress());
            myCache.put(customer.getCustomerAccountNumber(), customer);
            System.out.println("Service Customer Create Operation Completed successfully");
            //System.out.println("Customer Account in cache is " + ((Customer)myCache.get("54321")).getCustomerAccountNunmber());
        }
    }

    public static List<Device> generateDeviceList(List<Device> storedDevices) {
        List<Device> deviceList = new ArrayList<Device>();
        Integer[] randomNum = generateRandomArray(storedDevices.size());
        //for(Device device: storedDevices){
        //   System.out.println(device.getDeviceId());
        //}
        for (int i = 0; i < 2; i++) {
            Device device = storedDevices.get(randomNum[i]);
            deviceList.add(device);
        }

        return deviceList;
    }

    public static List<Location> generateLocationList(List<Location> storedLocations) {
        List<Location> locationsList = new ArrayList<Location>();
        Integer[] randomNum = generateRandomArray(storedLocations.size());
        //for(Device device: storedDevices){
        //   System.out.println(device.getDeviceId());
        //}
        for (int i = 0; i < 1; i++) {
            Location location = storedLocations.get(randomNum[i]);
            locationsList.add(location);
        }

        return locationsList;
    }

    public static List<Service> generateServiceList(List<Service> storedServices) {
        List<Service> servicesList = new ArrayList<Service>();
        Integer[] randomNum = generateRandomArray(storedServices.size());
        //for(Device device: storedDevices){
        //   System.out.println(device.getDeviceId());
        //}
        for (int i = 0; i < 4; i++) {
            Service service = storedServices.get(randomNum[i]);
            servicesList.add(service);
        }

        return servicesList;
    }

    public static Integer[] generateRandomArray(Integer arraySize) {
        //array to store N random integers (0 - N-1)
        Integer[] nums = new Integer[arraySize];

        // initialize each value at index i to the value i
        for (int i = 0; i < nums.length; ++i) {
            nums[i] = i;
        }

        Random randomGenerator = new Random();
        int randomIndex; // the randomly selected index each time through the loop
        int randomValue; // the value at nums[randomIndex] each time through the loop

        // randomize order of values
        for (int i = 0; i < nums.length; ++i) {
            // select a random index
            randomIndex = randomGenerator.nextInt(nums.length);

            // swap values
            randomValue = nums[randomIndex];
            nums[randomIndex] = nums[i];
            nums[i] = randomValue;
        }

        return nums;
    }

    public static List<Device> getDevice(String serialNumber,
                                         String siteName) {
        Set<Device> result = new HashSet<Device>();
        List<Device> devices = new ArrayList<Device>();
        NamedCache deviceCache = CacheFactory.getCache(siteName + "-Device");
        Filter deviceFilter;
        if (serialNumber.equalsIgnoreCase("all")) {
            deviceFilter =
                    new NotFilter(new EqualsFilter("getDeviceSerialNumber",
                                                   serialNumber));
        } else {
            deviceFilter =
                    new EqualsFilter("getDeviceSerialNumber", serialNumber);
        }
        result = deviceCache.entrySet(deviceFilter);
        for (Iterator iter = result.iterator(); iter.hasNext(); ) {
            Map.Entry<Integer, Device> entry =
                (Map.Entry<Integer, Device>)iter.next();
            devices.add(entry.getValue());
        }
        return devices;
    }

    public static Device createDevice(Sequence sequence, String make,
                                      String model, String serialNumber,
                                      String status, String type,
                                      String ipAddress, String macAddress) {
        Device device = new Device();
        device.setDeviceId(sequence.getMaxSequenceDevice());
        device.setDeviceMake(make);
        device.setDeviceModel(model);
        device.setDeviceSerialNumber(serialNumber);
        device.setDeviceStatus(status);
        device.setDeviceType(type);
        device.setIpAddress(ipAddress);
        device.setMacAddress(macAddress);
        return device;
    }

    public static void deleteDevice() {

    }

    public static List<Location> getLocation(String address, String siteName) {
        Set<Location> result = new HashSet<Location>();
        List<Location> location = new ArrayList<Location>();
        NamedCache locationCache =
            CacheFactory.getCache(siteName + "-Location");
        Filter locationFilter;
        if (address.equalsIgnoreCase("all")) {
            locationFilter =
                    new NotFilter(new EqualsFilter("getAdress", address));
        } else {
            locationFilter = new EqualsFilter("getAdress", address);
        }
        result = locationCache.entrySet(locationFilter);
        for (Iterator iter = result.iterator(); iter.hasNext(); ) {
            Map.Entry<Integer, Location> entry =
                (Map.Entry<Integer, Location>)iter.next();
            location.add(entry.getValue());
        }

        return location;
    }

    public static Location createLocation(Sequence sequence, String address,
                                          String city, String state,
                                          String postalCode, String country) {
        Location location = new Location();
        location.setLocationId(sequence.getMaxSequenceLocation());
        location.setAdress(address);
        location.setCity(city);
        location.setState(state);
        location.setPostalCode(postalCode);
        location.setCountry(country);
        return location;
    }

    public static void deleteLocation() {

    }
    

    public static List<Service> getServices(String serviceCode, String siteName) {
        Set<Service> result = new HashSet<Service>();
        List<Service> services = new ArrayList<Service>();
        NamedCache servicesCache =
            CacheFactory.getCache(siteName + "-Services");
        Filter servicesFilter;
        if (serviceCode.equalsIgnoreCase("all")) {
            servicesFilter = new NotFilter(new EqualsFilter("getServiceCode", serviceCode));
        } else {
            servicesFilter = new EqualsFilter("getServiceCode", serviceCode);
        }
        result = servicesCache.entrySet(servicesFilter);
        for (Iterator iter = result.iterator(); iter.hasNext(); ) {
            Map.Entry<Integer, Service> entry = (Map.Entry<Integer, Service>)iter.next();
            services.add(entry.getValue());
        }

        return services;
    }

    public static Service createService(Sequence sequence, String code, String description, String lob, String name, String rate) {
        Service service = new Service();
        service.setServiceId(sequence.getMaxSequenceService());
        service.setServiceCode(code);
        service.setServiceDescription(description);
        service.setServiceLOB(lob);
        service.setServiceName(name);
        service.setServiceRate(rate);
        return service;
    }

    public static void deleteService() {

    }

    public static List<Customer> getCustomers(String lastName,
                                              String siteName) {
        Set<Customer> result = new HashSet<Customer>();
        List<Customer> customer = new ArrayList<Customer>();
        NamedCache customerCache =
            CacheFactory.getCache(siteName + "-Customer");
        Filter customerFilter;
        if (lastName.equalsIgnoreCase("all")) {
            customerFilter =
                    new NotFilter(new EqualsFilter("getCustomerLastName",
                                                   lastName));
        } else {
            customerFilter = new EqualsFilter("getCustomerLastName", lastName);
        }
        result = customerCache.entrySet(customerFilter);
        for (Iterator iter = result.iterator(); iter.hasNext(); ) {
            Map.Entry<Integer, Customer> entry =
                (Map.Entry<Integer, Customer>)iter.next();
            customer.add(entry.getValue());
        }

        return customer;
    }

    public static Customer createCustomer(Sequence sequence, String firstName,
                                          String lastName, String phone,
                                          Location location,
                                          List<Device> devices,
                                          List<Service> services) {
        List<Device> customerDevices = new ArrayList<Device>();
        Customer customer = new Customer();
        Name name = new Name();
        customer.setCustomerId(sequence.getMaxSequenceCustomer());
        customer.setCustomerAccountNumber(sequence.getMaxSequenceCustomer());
        customer.setCustomerPhone(phone);
        name.setFirstName(firstName);
        name.setLastName(lastName);
        customer.setCustomerName(name);
        location.setCustomer(customer);
        location.setAdress("456 Chestnut st.");
        customer.setCustomerLocation(location);
        customer.setCustomerServices(services);

        for (Device device : devices) {
            device.setCustomer(customer);
            customerDevices.add(device);
        }
        customer.setCustomerDevices(customerDevices);

        return customer;
    }

    public static void removeCustomer(Customer customer, String siteName) {
        NamedCache persistenceCache = CacheFactory.getCache("Customer");
        NamedCache myCache = CacheFactory.getCache(siteName + "-Customer");
        //Customer temp = (Customer)myCache.get("54321");
        Customer tempCustomer =
            (Customer)persistenceCache.get(customer.getCustomerAccountNumber());
        if (tempCustomer != null) {
            myCache.remove(customer.getCustomerAccountNumber());
            System.out.println("Remove Customer Operation Completed successfully");
            //System.out.println("Customer Account in cache is " + ((Customer)myCache.get("54321")).getCustomerAccountNunmber());
        }
    }

    public static void loadGrid() {
        InvocationService service =
            (InvocationService)CacheFactory.getConfigurableCacheFactory().ensureService("InvocationService");
        service.query(new RemoteDistributedCacheLoader(), null);
        System.out.println("Cache Loading Complete");
        //Set memberSet = (Set) service.query(new RemoteInvocableTest(), null);
        //Map memberSet = service.query(new RemoteInvocableTest("Synchronous"), null);
        //InvocationObserver observer = new CacheLoaderObserver();
        //service.execute(new RemoteDistributedCacheLoader(), null, observer);
        //service.execute(new RemoteInvocableTest("Asynchronous"), null, observer);
    }

    public static String getClusterLocation() throws Exception {
        NamedCache gridEnvironemtnCache =
            CacheFactory.getCache("GridEnvironment");
        System.out.println("gridEnvironemtnCache: " + gridEnvironemtnCache);
        String clusterLocation =
            (String)gridEnvironemtnCache.get("ClusterLocation");
        System.out.println("clusterLocation: " + clusterLocation);
    
        if (!(clusterLocation.equalsIgnoreCase("East") ||
              clusterLocation.equalsIgnoreCase("West"))) {
            System.out.println("Could not determine location or unexpected location returned");
            throw new Exception("Could not determine location or unexpected location returned");
        }

        return clusterLocation;
    }

/*
    public static void persistCustomer(Customer customer) {
        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory("TelecomDB");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            String emQueryString =
                "SELECT C FROM Customer C WHERE C.customerAccountNumber = :accountNumber";
            Query emQuery = em.createQuery(emQueryString);
            emQuery.setParameter("accountNumber",
                                 customer.getCustomerAccountNumber());
            List<Customer> ctxManagedCustomer = emQuery.getResultList();
            if (ctxManagedCustomer.isEmpty()) {
                em.persist(customer);
            } else {
                System.out.println("Customer already exists....updating data");
                customer = ctxManagedCustomer.get(0);
                //Location location = customer.getCustomerLocatoin();
                //location.setState("CA");
                //location.setCustomer(customer);
                //customer.setCustomerLocation(location);
                customer.setCustomerPhone("555-1111");
                em.persist(customer);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            em.close();
        }
        emf.close();
    }*/
}
