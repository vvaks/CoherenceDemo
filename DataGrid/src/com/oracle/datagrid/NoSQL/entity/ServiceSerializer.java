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
 * This class provides POF serialization of Service.
 */
public class ServiceSerializer implements PofSerializer {
    final private static int VERSION_ID = 1;
    final private static int SERVICEID = 1;
    final private static int SERVICENAME = 2;
    final private static int DESCRIPTION = 3;
    final private static int LOB = 4;
    final private static int RATE = 5;
    final private static int CODE = 6;
    /**
     * Default constructor
     */
    public ServiceSerializer() {}

    /**
     * Serialize a object.
     *
     * @param out The PofWriter to write to
     * @param o The Service object to write
     * @throws IOException if the object o is not a Service
     */
    @Override
    public void serialize(PofWriter out, Object o)
        throws IOException
    {
        if (o.getClass() == Service.class) {
            out.setVersionId(VERSION_ID);
            out.writeInt(SERVICEID, ((Service)o).getServiceId());
            out.writeString(SERVICENAME, ((Service)o).getServiceName());
            out.writeString(DESCRIPTION, ((Service)o).getServiceDescription());
            out.writeString(LOB, ((Service)o).getServiceLOB());
            out.writeString(RATE, ((Service)o).getServiceRate());
            out.writeString(CODE, ((Service)o).getServiceCode());
            out.writeRemainder(null);
        } else {
            throw new IOException(
                "Invalid type presented to Service Serializer: " + o.getClass());
        }
    }

    /**
     * Deserialize a Service object.
     *
     * @param in The PofReader to read from
     * @return  The Service object read from the input stream
     * @throws IOException if the stream does not contain the serialization
     *         of a Service object in an expected version.
     */
    @Override
    public Object deserialize(PofReader in)
        throws IOException
    {
        if (in.getVersionId() != VERSION_ID) {
            throw new IOException(
                "ServiceSerializer encountered unexpected " +
                "version id: " + in.getVersionId());
        }

        final Integer serviceId = in.readInt(SERVICEID);
        final String serviceName = in.readString(SERVICENAME);
        final String description = in.readString(DESCRIPTION);
        final String serviceLOB = in.readString(LOB);
        final String rate = in.readString(RATE);
        final String code = in.readString(CODE);
        in.readRemainder();

        final Service service = new Service();
        service.setServiceId(serviceId);
        service.setServiceName(serviceName);
        service.setServiceDescription(description);
        service.setServiceLOB(serviceLOB);
        service.setServiceRate(rate);
        service.setServiceCode(code);
        return service;
    }
}