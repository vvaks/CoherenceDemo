package com.oracle.datagrid.activeactive;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;



public class ScratchPad {
    private static Connection conn;
    
    public ScratchPad() {
        super();
    }

    public static void main(String[] args) {
        try {
            Context ctx = null;
            Hashtable ht = new Hashtable();
            ht.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
            //ht.put(Context.PROVIDER_URL, System.getProperty("weblogic.management.server").replace("http", "t3"));
            ht.put(Context.PROVIDER_URL, "t3://127.0.0.1:7001");
            ctx = new InitialContext(ht);
            DataSource dataSource = (DataSource) ctx.lookup("jdbc/Telecom");
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String query = "SELECT 1 as D from Dual";
                    ResultSet rs = stmt.executeQuery(query);
                    System.out.println("OK");
                    System.out.println(rs.findColumn("D"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}