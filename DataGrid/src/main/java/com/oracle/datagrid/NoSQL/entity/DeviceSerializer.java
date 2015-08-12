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
 * This class provides POF serialization of Device.
 */
public class DeviceSerializer implements PofSerializer {
    final private static int VERSION_ID = 1;
    final private static int DEVICEID = 1;
    final private static int TYPE = 2;
    final private static int SERIALNUMBER = 3;
    final private static int STATUS = 4;
    final private static int MAKE = 5;
    final private static int MODEL = 6;
    final private static int IPADDRESS = 7;
    final private static int MACADDRESS = 8;
    /**
     * Default constructor
     */
    public DeviceSerializer() {}

    /**
     * Serialize a object.
     *
     * @param out The PofWriter to write to
     * @param o The Device object to write
     * @throws IOException if the object o is not a Device
     */
    @Override
    public void serialize(PofWriter out, Object o)
        throws IOException
    {
        if (o.getClass() == Device.class) {
            out.setVersionId(VERSION_ID);
            out.writeInt(DEVICEID, ((Device)o).getDeviceId());
            out.writeString(TYPE, ((Device)o).getDeviceType());
            out.writeString(SERIALNUMBER, ((Device)o).getDeviceSerialNumber());
            out.writeString(STATUS, ((Device)o).getDeviceStatus());
            out.writeString(MAKE, ((Device)o).getDeviceMake());
            out.writeString(MODEL, ((Device)o).getDeviceModel());
            out.writeObject(IPADDRESS, ((Device)o).getIpAddress());
            out.writeObject(MACADDRESS, ((Device)o).getMacAddress());
            out.writeRemainder(null);
        } else {
            throw new IOException(
                "Invalid type presented to Device Serializer: " + o.getClass());
        }
    }

    /**
     * Deserialize a Device object.
     *
     * @param in The PofReader to read from
     * @return  The Device object read from the input stream
     * @throws IOException if the stream does not contain the serialization
     *         of a Device object in an expected version.
     */
    @Override
    public Object deserialize(PofReader in)
        throws IOException
    {
        if (in.getVersionId() != VERSION_ID) {
            throw new IOException(
                "DeviceSerializer encountered unexpected " +
                "version id: " + in.getVersionId());
        }

        final Integer deviceId = in.readInt(DEVICEID);
        final String type = in.readString(TYPE);
        final String serialNumber = in.readString(SERIALNUMBER);
        final String status = in.readString(STATUS);
        final String make = in.readString(MAKE);
        final String model = in.readString(MODEL);
        final String ipAddress = in.readString(IPADDRESS);
        final String macAddress = in.readString(MACADDRESS);
        in.readRemainder();

        final Device device = new Device();
        device.setDeviceId(deviceId);
        device.setDeviceType(type);
        device.setDeviceSerialNumber(serialNumber);
        device.setDeviceStatus(status);
        device.setDeviceMake(make);
        device.setDeviceModel(model);
        device.setIpAddress(ipAddress);
        device.setMacAddress(macAddress);
        return device;
    }
}