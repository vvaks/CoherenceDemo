package oracle.coherence.demo;

import com.oracle.datagrid.activeactive.Coherence;
import com.oracle.datagrid.activeactive.entity.Customer;
import com.oracle.datagrid.activeactive.entity.Device;
import com.oracle.datagrid.activeactive.entity.Service;

import com.oracle.xmlns.telecomservicesplatform.telecombuyflow.servicebuyflow.*;
import com.oracle.xmlns.soalab.servicebuyflow.servicebuyflow.*;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CoherenceDemoServlet extends HttpServlet {
    
    private static final String CONTENT_TYPE =
        "text/html; charset=windows-1252";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    private void doTask(HttpServletRequest request,
                        HttpServletResponse response) throws ServletException,
                                                             IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        Object requestType = request.getParameter("requestType");
        Coherence co = new Coherence();
        String site;
        String fname = "";
        String lname = "";
        String address = "";
        String city = "";
        String state = "";
        String pin = "";
        String country = "";
        String telephone = "";
        String devices = "";
        String services = "";
        String accountNo = "";
        String custID = null;
        List<String> countries = new ArrayList<String>();
        countries.add("USA");countries.add("Japan");countries.add("India");
        request.setAttribute("Countries", countries);
        System.out.println("Receiving Request for action: " + requestType);
        try {
            if (requestType != null && requestType.toString().equalsIgnoreCase("initial")) {
                site = co.getClusterLocation();
                //List<Device> customerDevices = new ArrayList<Device>();
                List<Device> storedDevices = co.getDevice("all", site);
                List<Service> storedServices = co.getServices("all", site);
                request.setAttribute("Devices", storedDevices);
                request.setAttribute("Services", storedServices); 
                request.setAttribute("accountNo", "New");
                request.setAttribute("custID", "New");
                request.setAttribute("action", "Create");
                request.getRequestDispatcher("/WEB-INF/coherencedemo.jsp").forward(request, response);

            } else if (requestType != null && requestType.toString().equalsIgnoreCase("create")) {
                System.out.println("Creating new Customer in Servlet");
                fname = request.getParameter("fname");
                lname = request.getParameter("lname");
                address = request.getParameter("address");
                city = request.getParameter("city");
                state = request.getParameter("state");
                pin = request.getParameter("pin");
                telephone = request.getParameter("telephone");
                country = request.getParameter("country");
                devices = request.getParameter("devicelist");
                services = request.getParameter("servicelist");
                System.out.println(fname + ":" + lname);
                System.out.println(country + ":" + city);
                System.out.println(devices + " : " + services);

                co.createCustomer(fname, lname, address, city, state, pin,telephone, country, devices, services);
                request.setAttribute("status", "Create Request Submitted Successfully");
                request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                
            }else if (requestType != null && requestType.toString().equalsIgnoreCase("update")) {
                custID = request.getParameter("custId");
                fname = request.getParameter("fname");
                lname = request.getParameter("lname");
                address = request.getParameter("address");
                city = request.getParameter("city");
                state = request.getParameter("state");
                pin = request.getParameter("pin");
                telephone = request.getParameter("telephone");
                country = request.getParameter("country");
                devices = request.getParameter("devicelist");
                services = request.getParameter("servicelist");
                System.out.println(fname + ":" + lname);
                System.out.println(devices + "::" + services);

                co.updateCustomer(fname, lname, address, city, state, pin, telephone, country, devices, services, custID);
                
                request.setAttribute("status", "Update Request Submitted Successfully");
                request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request,response);
                
            } else if (requestType != null && requestType.toString().equalsIgnoreCase("updateRequest")) {
                site = co.getClusterLocation();
                //List<Device> customerDevices = new ArrayList<Device>();
                List<Device> storedDevices = co.getDevice("all", site);
                List<Service> storedServices = co.getServices("all", site);
                
                String customerID = request.getParameter("customerID");
                Customer cust = co.getCustomerById(new Integer(customerID), site);
                System.out.println("Customer Account Number: " + cust.getCustomerAccountNumber());
                List<Device> customerDevices = new ArrayList();
                List<Service> customerServices = new ArrayList();
                if (cust != null) {
                    
                    if (cust.getCustomerAccountNumber() != null) {
                        accountNo = cust.getCustomerId().toString();
                    }
                    
                    if (cust.getCustomerId() != null) {
                        custID = cust.getCustomerAccountNumber().toString();
                    }
                    
                    if (cust.getCustomerFirstName() != null) {
                        fname = cust.getCustomerFirstName();
                    }
                    
                    if (cust.getCustomerLastName() != null) {
                        lname = cust.getCustomerLastName();
                    }
                    
                    if (cust.getCustomerPhone() != null) {
                        telephone = cust.getCustomerPhone();
                    }
                    
                    if (cust.getCustomerLocation() != null) {
                        if (cust.getCustomerLocation().getAdress()!=null) {
                            address = cust.getCustomerLocation().getAdress();
                        }
                        
                        if  (cust.getCustomerLocation().getCity() != null) {
                            city = cust.getCustomerLocation().getCity();
                        }
                        
                        if  (cust.getCustomerLocation().getState() != null) {
                            state = cust.getCustomerLocation().getState();
                        }
                        
                        if  (cust.getCustomerLocation().getCountry() != null) {
                            country = cust.getCustomerLocation().getCountry();
                        }
                        
                        if  (cust.getCustomerLocation().getPostalCode() != null) {
                            pin = cust.getCustomerLocation().getPostalCode();
                        }
                    }

                    customerDevices = cust.getCustomerDevices();
                    customerServices = cust.getCustomerServices();
                    for (int i = 0; storedDevices != null && i < storedDevices.size(); i++) {
                        for (int j = 0; customerDevices != null && j < customerDevices.size(); j++) {
                            if (storedDevices.get(i).getDeviceId().intValue() == customerDevices.get(j).getDeviceId().intValue()) {
                                storedDevices.get(i).setSelected("selected");
                            }
                        }
                    }
                    
                    for (int i = 0; storedServices != null && i < storedServices.size(); i++) {
                        for (int j = 0; customerServices != null && j < customerServices.size(); j++) {
                            if (storedServices.get(i).getServiceId().intValue() == customerServices.get(j).getServiceId().intValue()) {
                                storedServices.get(i).setSelected("selected");
                            }
                        }
                    }    
                }
                request.setAttribute("action", "Update");
                request.setAttribute("accountNo", accountNo);
                request.setAttribute("custID", custID);
                request.setAttribute("firstName", fname);
                request.setAttribute("lastName", lname);
                request.setAttribute("Devices", storedDevices);
                request.setAttribute("customerServices", customerServices);
                request.setAttribute("Services", storedServices);
                request.setAttribute("address", address);
                request.setAttribute("city", city);
                request.setAttribute("state", state);
                request.setAttribute("country", country);
                request.setAttribute("pinCode", pin);
                request.setAttribute("phone", telephone);
                request.getRequestDispatcher("/WEB-INF/coherencedemo.jsp").forward(request,response);

            } else if (requestType != null && requestType.toString().equalsIgnoreCase("search")) {
                List<Customer> searchResults = null;
                lname = request.getParameter("lname");

                if (lname != null) {
                    site = co.getClusterLocation();
                    searchResults = co.getCustomers(lname, site);
                } else {
                    searchResults = new ArrayList<Customer>();
                    lname = "";
                }

                request.setAttribute("searchresults", searchResults);
                request.setAttribute("lastname", lname);
                request.getRequestDispatcher("/WEB-INF/coherencedemosearch.jsp").forward(request, response);
            } 
            else if (requestType != null && requestType.toString().equalsIgnoreCase("buyservice")){
                String customerID = request.getParameter("customerId");
                String serviceID = request.getParameter("serviceId");
                
                site = co.getClusterLocation();
                List<Service> storedServices = co.getServices("all", site);
                
                Customer cust = co.getCustomerById(new Integer(customerID), site);
                List<Service> customerServices = cust.getCustomerServices();
                for (int i = 0; storedServices != null && i < storedServices.size(); i++) {
                    for (int j = 0; customerServices != null && j < customerServices.size(); j++) {
                        if (storedServices.get(i).getServiceId().intValue() == customerServices.get(j).getServiceId().intValue()) {
                            storedServices.get(i).setSelected("selected");
                        }
                    }
                }
                request.setAttribute("Services", storedServices);
                request.setAttribute("customerId", customerID);
                request.setAttribute("serviceId", serviceID);
                request.setAttribute("requestType", requestType);
            
                request.getRequestDispatcher("/WEB-INF/payment.jsp").forward(request, response);
            }
            else if (requestType != null && requestType.toString().equalsIgnoreCase("serviceBuyFlow")){
                String customerID = request.getParameter("accountNo");
                String serviceID = request.getParameter("service");
                String ccNo = request.getParameter("ccNo");
                fname = request.getParameter("fname");
                lname = request.getParameter("lname");
                String expire = request.getParameter("expire");
                String provider = request.getParameter("provider");
                
                com.oracle.xmlns.soalab.servicebuyflow.servicebuyflow.Process processRequest = new com.oracle.xmlns.soalab.servicebuyflow.servicebuyflow.Process();
                
                processRequest.setCustomerAccountNumber(Integer.parseInt(customerID));
                processRequest.setServiceId(Integer.parseInt(serviceID));
                processRequest.setCreditAccountNumber(Long.parseLong(ccNo));
                processRequest.setHolderFirstName(fname);
                processRequest.setHolderLastName(lname);
                processRequest.setExpireDate(expire);
                processRequest.setProvider(provider);
                
                ExecuteBindQSService executeBindQSService = new ExecuteBindQSService();
                ExecutePtt executePtt = executeBindQSService.getExecuteBindQSPort();
                
                com.oracle.xmlns.soalab.servicebuyflow.servicebuyflow.ProcessResponse paymentResponse = new com.oracle.xmlns.soalab.servicebuyflow.servicebuyflow.ProcessResponse();
                com.oracle.xmlns.soalab.servicebuyflow.servicebuyflow.ProcessFault paymentFault = new com.oracle.xmlns.soalab.servicebuyflow.servicebuyflow.ProcessFault();
                
                paymentResponse = executePtt.execute(processRequest);
                System.out.println(paymentResponse.getResult()); 
                
                if(paymentResponse.getResult() == null || paymentResponse.getResult().compareToIgnoreCase("Approved") == 0){
                    System.out.println("Payment Approved, Service Provisioned");
                    request.setAttribute("customerId", customerID);
                    request.setAttribute("requestType", "updateRequest");
                    request.setAttribute("action", "Update");
                    request.getRequestDispatcher("/WEB-INF/payment.jsp").forward(request, response);
                }
                else{
                    System.out.println("Customer already owns that product or Payment Rejected");
                    
                    site = co.getClusterLocation();
                    List<Service> storedServices = co.getServices("all", site);
                    
                    Customer cust = co.getCustomerById(new Integer(customerID), site);
                    List<Service> customerServices = cust.getCustomerServices();
                    for (int i = 0; storedServices != null && i < storedServices.size(); i++) {
                        for (int j = 0; customerServices != null && j < customerServices.size(); j++) {
                            if (storedServices.get(i).getServiceId().intValue() == customerServices.get(j).getServiceId().intValue()) {
                                storedServices.get(i).setSelected("selected");
                            }
                        }
                    }
                    request.setAttribute("Services", storedServices);
                    request.setAttribute("customerId", customerID);
                    request.setAttribute("serviceId", serviceID);
                    request.setAttribute("requestType", "buyservice");
                    request.getRequestDispatcher("/WEB-INF/payment.jsp").forward(request, response);
                }
            }    
            else if (requestType != null &&requestType.toString().equalsIgnoreCase("login")) {
                request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
            }
        }catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
       
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                           IOException {
        this.doTask(request, response);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException,
                                                            IOException {
        this.doTask(request, response);
    }
}
