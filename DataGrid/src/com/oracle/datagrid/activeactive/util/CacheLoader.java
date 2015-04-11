package com.oracle.datagrid.activeactive.util;


import com.oracle.datagrid.activeactive.entity.Customer;
import com.oracle.datagrid.activeactive.entity.Device;
import com.oracle.datagrid.activeactive.entity.Location;
import com.oracle.datagrid.activeactive.entity.Name;
import com.oracle.datagrid.activeactive.entity.Sequence;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Service;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;

//import com.tangosol.util.Filter;
//import com.tangosol.util.filter.EqualsFilter;


public class CacheLoader {
    private static final String INVOCATION_SERVICE_NAME = "InvocationService";
    private final String PERSISTENCE_QUEUE_CACHE_NAME = "Customer";
    private static final String GRID_ENVIRONMENT_NAME = "GridEnvironment";
    //private final String EAST_GRID_ENVIRONMENT_NAME = "GridEnvironmentEast";
    //private final String WEST_GRID_ENVIRONMENT_NAME = "GridEnvironmentWest";
    private final String CUSTOMER_EAST_CACHE_NAME = "DataGridEastReplicated";
    private final String CUSTOMER_WEST_CACHE_NAME = "DataGridWestReplicated";
    
    private InvocationService invocationService;
    private NamedCache SequenceCache;
    private NamedCache PersistenceCache;
    private NamedCache CustomerCache;
    private NamedCache RemoteCustomerCache;
    private NamedCache localGridEnvironmentCache;
    
    private Customer customer;
    private Device device;
    private Location location;
    private Name name;
    private Service service;
    private Sequence sequence;
    private Connection conn;
    private String dbUrl = "jdbc:oracle:thin:@192.168.56.1:1521:XE";
    private String dbUser = "TELECOM";
    private String dbPass = "letmein";
    private String dbClass = "oracle.jdbc.OracleDriver";
    private String clusterLocation;
    private Boolean isCascaded;
	
	public CacheLoader(){
		try{
			Class.forName(dbClass);
			conn = DriverManager.getConnection (dbUrl, dbUser, dbPass);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

		catch(SQLException e) {
		e.printStackTrace();
		}
	}
    public void loadCustomerSequences(){
            System.out.println("LOADING SEQUENCES");
            NamedCache SequenceCache = CacheFactory.getCache("Sequence");
            sequence = new Sequence();
            
            String query = "SELECT (SELECT MAX(CUSTOMER_ID) FROM CUSTOMER) AS CUSTOMERID, " +
                            "(SELECT MAX(DEVICE_ID) FROM DEVICE) AS DEVICEID, " +
                            "(SELECT MAX(LOCATION_ID) FROM LOCATION) AS LOCATIONID, " +
                            "(SELECT MAX(SERVICE_ID) FROM SERVICE) AS SERVICEID, " +
                            "FROM DUAL";    
            try{
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                            sequence.setMaxSequenceCustomer(rs.getInt("CUSTOMERID") + 1);
                            sequence.setMaxSequenceDevice(rs.getInt("DEVICEID") + 1);
                            sequence.setMaxSequenceLocation(rs.getInt("LOCATIONID") + 1);
                            sequence.setMaxSequenceService(rs.getInt("SERVICEID") + 1);
                            //sequence.displaySequences();
                    }
                    SequenceCache.put(1, sequence);
            } 
            catch(SQLException e) {
                    e.printStackTrace();
            }
    }
    
    public void loadCustomerCache() throws IOException {
            System.out.println("LOADING CUSTOMER CACHE");
            long customerKey;
            Map <Long, Customer>customerMap = new HashMap<Long, Customer>();
    //TODO CHANGE QUERY FOR CUSTOMER
            String query = "SELECT * FROM (SELECT PA.POLICYACTIONID, PA.CMMAC, PA.DEVICETYPE, PA.INCIDENTID, PA.POLICYID, PA.SUBSCRIBERID, PA.DATETIME, PA.PROJECTSPECIFICS, PA.EVENTOCCURRENCE, " +
    " A.ACTIONID, PORTBLOCKID, BNACK, DIRECTION, EXP_DATE, PORT, PROTOCOL, SESSIONID, NULL AS QOSID, NULL AS UPSTREAMSPEED, NULL AS DOWNSTREAMSPEED, NULL AS SUSPENDID, " +
    " NULL AS EMAILID, NULL AS TEMPLATEID, NULL AS BULLETINID, NULL AS ACKREQLEVEL, NULL AS NOTIFICATIONID, ar.ACTIONREFID, pr.PROJECTREFID, sr.STATUSREFID, ar.VALUE AS ACTIONREFVALUE, pr.VALUE PROJECTREFVALUE, sr.VALUE AS STATUSREFVALUE,'PORTBLOCK' AS ACTIONTYPE " +
    " FROM AGAREE.POLICY_ACTION pa, AGAREE.ACTION_DETAILS a,AGAREE.ACTION_REF ar, " +
            "               AGAREE.STATUS_REF sr,AGAREE.PORTBLOCK pb, AGAREE.PROJECT_REF pr where pa.policyactionid = a.policyactionid " +       
            "               and  a.actionid = pb.actiondetailsid  and a.actionrefid = ar.actionrefid and a.statusrefid = sr.statusrefid " +  
            "               and pr.projectrefid = pa.projectrefid) " +
    "UNION " +
    " SELECT * from (SELECT PA.POLICYACTIONID, PA.CMMAC, PA.DEVICETYPE, PA.INCIDENTID, PA.POLICYID, PA.SUBSCRIBERID, PA.DATETIME, PA.PROJECTSPECIFICS, PA.EVENTOCCURRENCE, " + 
    " A.ACTIONID, NULL AS PORTBLOCKID, BNACK, NULL AS DIRECTION, EXP_DATE, NULL AS PORT, NULL AS PROTOCOL, SESSIONID, QOSID, UPSTREAMSPEED, DOWNSTREAMSPEED, NULL AS SUSPENDID, " +
    " NULL AS EMAILID, NULL AS TEMPLATEID, NULL AS BULLETINID, NULL AS ACKREQLEVEL, NULL AS NOTIFICATIONID, ar.ACTIONREFID, pr.PROJECTREFID, sr.STATUSREFID, ar.VALUE AS ACTIONREFVALUE, pr.VALUE PROJECTREFVALUE, sr.VALUE AS STATUSREFVALUE, 'QOS' AS ACTIONTYPE " +
    " FROM  AGAREE.POLICY_ACTION pa , AGAREE.ACTION_DETAILS a,AGAREE.ACTION_REF ar, " +
            "               AGAREE.STATUS_REF sr, AGAREE.QOS qo, AGAREE.PROJECT_REF pr WHERE  pa.policyactionid = a.policyactionid " +      
            "               AND a.actionid = qo.actiondetailsid and a.actionrefid = ar.actionrefid " +      
            "               AND a.statusrefid = sr.statusrefid and pr.projectrefid = pa.projectrefid) " +
    " UNION " +
    " SELECT * from (SELECT PA.POLICYACTIONID, PA.CMMAC, PA.DEVICETYPE, PA.INCIDENTID, PA.POLICYID, PA.SUBSCRIBERID, PA.DATETIME, PA.PROJECTSPECIFICS, PA.EVENTOCCURRENCE, " + 
    " A.ACTIONID, NULL AS PORTBLOCKID, NULL AS BNACK, NULL AS DIRECTION, NULL AS EXP_DATE, NULL AS PORT, NULL AS PROTOCOL, SESSIONID, NULL AS QOSID, NULL AS UPSTREAMSPEED, " +
    " NULL AS DOWNSTREAMSPEED, SUSPENDID, NULL AS EMAILID, NULL AS TEMPLATEID, NULL AS BULLETINID, NULL AS ACKREQLEVEL, NULL AS NOTIFICATIONID, ar.ACTIONREFID, pr.PROJECTREFID, sr.STATUSREFID, ar.VALUE AS ACTIONREFVALUE, pr.VALUE PROJECTREFVALUE, sr.VALUE AS STATUSREFVALUE, 'SUSPEND' AS ACTIONTYPE " +
            "               FROM  AGAREE.POLICY_ACTION pa ,AGAREE.ACTION_DETAILS a, " +      
            "               AGAREE.ACTION_REF ar,AGAREE.STATUS_REF sr,AGAREE.SUSPEND su, AGAREE.PROJECT_REF pr " + 
            "               WHERE  pa.policyactionid = a.policyactionid  and  a.actionid = su.actiondetailsid " +
            "               AND a.actionrefid = ar.actionrefid and a.statusrefid = sr.statusrefid " +      
            "               AND pr.projectrefid = pa.projectrefid) " +
    " UNION " +
    " SELECT * from (SELECT PA.POLICYACTIONID, PA.CMMAC, PA.DEVICETYPE, PA.INCIDENTID, PA.POLICYID, PA.SUBSCRIBERID, PA.DATETIME, PA.PROJECTSPECIFICS, PA.EVENTOCCURRENCE, " + 
    " A.ACTIONID, NULL AS PORTBLOCKID, NULL AS BNACK, NULL AS DIRECTION, NULL AS EXP_DATE, NULL AS PORT, NULL AS PROTOCOL, NULL AS SESSIONID, NULL AS QOSID, NULL AS UPSTREAMSPEED, " + 
    " NULL AS DOWNSTREAMSPEED, NULL AS SUSPENDID, EMAILID, TEMPLATEID, NULL AS BULLETINID, NULL AS ACKREQLEVEL, NULL AS NOTIFICATIONID, ar.ACTIONREFID, pr.PROJECTREFID, sr.STATUSREFID, ar.VALUE AS ACTIONREFVALUE, pr.VALUE PROJECTREFVALUE, sr.VALUE AS STATUSREFVALUE, 'EMAIL' AS ACTIONTYPE " +
            "               FROM  AGAREE.POLICY_ACTION pa ,AGAREE.ACTION_DETAILS a, " +      
            "               AGAREE.ACTION_REF ar,AGAREE.STATUS_REF sr, AGAREE.EMAIL em, AGAREE.PROJECT_REF pr " + 
            "               WHERE  pa.policyactionid = a.policyactionid  and  a.actionid = em.actiondetailsid " +
            "               AND a.actionrefid = ar.actionrefid and a.statusrefid = sr.statusrefid " +      
            "               AND pr.projectrefid = pa.projectrefid) " +
    " UNION " +
    " SELECT * from (SELECT PA.POLICYACTIONID, PA.CMMAC, PA.DEVICETYPE, PA.INCIDENTID, PA.POLICYID, PA.SUBSCRIBERID, PA.DATETIME, PA.PROJECTSPECIFICS, PA.EVENTOCCURRENCE, " + 
    " A.ACTIONID, NULL AS PORTBLOCKID, NULL AS BNACK, NULL AS DIRECTION, NULL AS EXP_DATE, NULL AS PORT, NULL AS PROTOCOL, NULL AS SESSIONID, NULL AS QOSID, NULL AS UPSTREAMSPEED, " + 
    " NULL AS DOWNSTREAMSPEED, NULL AS SUSPENDID, NULL AS EMAILID, TEMPLATEID, BULLETINID, ACKREQLEVEL, NOTIFICATIONID, ar.ACTIONREFID, pr.PROJECTREFID, sr.STATUSREFID, ar.VALUE AS ACTIONREFVALUE, pr.VALUE PROJECTREFVALUE, sr.VALUE AS STATUSREFVALUE, 'BULLETIN' AS ACTIONTYPE " +
            "               FROM  AGAREE.POLICY_ACTION pa ,AGAREE.ACTION_DETAILS a, " +      
            "               AGAREE.ACTION_REF ar,AGAREE.STATUS_REF sr, AGAREE.BULLETIN bu, AGAREE.PROJECT_REF pr " + 
            "               WHERE  pa.policyactionid = a.policyactionid  and  a.actionid = bu.actiondetailsid " +
            "               AND a.actionrefid = ar.actionrefid and a.statusrefid = sr.statusrefid " +      
            "               AND pr.projectrefid = pa.projectrefid) ";
            try{
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
    // TODO WRITE LOADER QUERY AND LOAD CUSTOMER OBJECTS
                    while (rs.next()) {
                                                            
                            customerKey = customer.getCustomerId();
                            customerMap.put(customerKey, customer);
                    }
                    CustomerCache.putAll(customerMap);
            } 
            catch(SQLException e) {
                    e.printStackTrace();
            }
    }
    public void closeConnection() throws SQLException{
            this.conn.close();
    }
}