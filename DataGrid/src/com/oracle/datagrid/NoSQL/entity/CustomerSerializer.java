/*-
 *
 *  This file is part of Oracle NoSQL Database
 *  Copyright (C) 2011, 2014 Oracle and/or its affiliates.  All rights reserved.
 *
 *  Oracle NoSQL Database is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Affero General Public License
 *  as published by the Free Software Foundation, version 3.
 *
 *  Oracle NoSQL Database is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License in the LICENSE file along with Oracle NoSQL Database.  If not,
 *  see <http://www.gnu.org/licenses/>.
 *
 *  An active Oracle commercial licensing agreement for this product
 *  supercedes this license.
 *
 *  For more information please contact:
 *
 *  Vice President Legal, Development
 *  Oracle America, Inc.
 *  5OP-10
 *  500 Oracle Parkway
 *  Redwood Shores, CA 94065
 *
 *  or
 *
 *  berkeleydb-info_us@oracle.com
 *
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  EOF
 *
 */

package com.oracle.datagrid.NoSQL.entity;

import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides POF serialization of Customer.
 */
public class CustomerSerializer implements PofSerializer {
    final private static int VERSION_ID = 1;
    final private static int CUSTOMERID = 1;
    final private static int NAME = 2;
    final private static int PHONE = 3;
    final private static int ACCOUNTNUMBER = 4;
    final private static int LOCATION = 5;
    final private static int DEVICES = 6;
    final private static int SERVICES = 7;

    /**
     * Default constructor
     */
    public CustomerSerializer() {}

    /**
     * Serialize a object.
     *
     * @param out The PofWriter to write to
     * @param o The Customer object to write
     * @throws IOException if the object o is not a Customer
     */
    @Override
    public void serialize(PofWriter out, Object o)
        throws IOException
    {
        if (o.getClass() == Customer.class) {
            out.setVersionId(VERSION_ID);
            out.writeInt(CUSTOMERID, ((Customer)o).getCustomerId());
            out.writeObject(NAME, ((Customer)o).getCustomerName());
            out.writeString(PHONE, ((Customer)o).getCustomerPhone());
            out.writeInt(ACCOUNTNUMBER, ((Customer)o).getAccountNumber());
            out.writeObject(LOCATION, ((Customer)o).getCustomerLocation());
            out.writeObject(DEVICES, ((Customer)o).getCustomerDevices());
            out.writeObject(SERVICES, ((Customer)o).getCustomerServices());
            out.writeRemainder(null);
        } else {
            throw new IOException(
                "Invalid type presented to Customer Serializer: " + o.getClass());
        }
    }

    /**
     * Deserialize a Customer object.
     *
     * @param in The PofReader to read from
     * @return  The Customer object read from the input stream
     * @throws IOException if the stream does not contain the serialization
     *         of a Customer object in an expected version.
     */
    @Override
    public Object deserialize(PofReader in)
        throws IOException
    {
        if (in.getVersionId() != VERSION_ID) {
            throw new IOException(
                "CustomerSerializer encountered unexpected " +
                "version id: " + in.getVersionId());
        }

        final Integer customerId = in.readInt(CUSTOMERID);
        final Name customerName = (Name)in.readObject(NAME);
        final String customerPhone = in.readString(PHONE);
        final Integer accountNumber = in.readInt(ACCOUNTNUMBER);
        final Location location = (Location)in.readObject(LOCATION);
        final List<Device> devices = (List<Device>)in.readObject(DEVICES);
        final List<Service> services = (List<Service>)in.readObject(SERVICES);
        in.readRemainder();

        final Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName(customerName);
        customer.setCustomerPhone(customerPhone);
        customer.setAccountNumber(accountNumber);
        customer.setCustomerLocation(location);
        customer.setCustomerDevices(devices);
        customer.setCustomerServices(services);
        return customer;
    }
}