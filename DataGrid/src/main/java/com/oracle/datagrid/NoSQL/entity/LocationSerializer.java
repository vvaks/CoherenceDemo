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
 * This class provides POF serialization of Location.
 */
public class LocationSerializer implements PofSerializer {
    final private static int VERSION_ID = 1;
    final private static int LOCATIONID = 1;
    final private static int ADDRESS = 2;
    final private static int CITY = 3;
    final private static int STATE = 4;
    final private static int POSTALCODE = 5;
    final private static int COUNTRY = 6;
    
    /**
     * Default constructor
     */
    public LocationSerializer() {}

    /**
     * Serialize a object.
     *
     * @param out The PofWriter to write to
     * @param o The Location object to write
     * @throws IOException if the object o is not a Location
     */
    @Override
    public void serialize(PofWriter out, Object o)
        throws IOException
    {
        if (o.getClass() == Location.class) {
            out.setVersionId(VERSION_ID);
            out.writeInt(LOCATIONID, ((Location)o).getLocationId());
            out.writeString(ADDRESS, ((Location)o).getAddress());
            out.writeString(CITY, ((Location)o).getCity());
            out.writeString(STATE, ((Location)o).getState());
            out.writeString(POSTALCODE, ((Location)o).getPostalCode());
            out.writeString(COUNTRY, ((Location)o).getCountry());
            out.writeRemainder(null);
        } else {
            throw new IOException(
                "Invalid type presented to Location Serializer: " + o.getClass());
        }
    }

    /**
     * Deserialize a Location object.
     *
     * @param in The PofReader to read from
     * @return  The Location object read from the input stream
     * @throws IOException if the stream does not contain the serialization
     *         of a Location object in an expected version.
     */
    @Override
    public Object deserialize(PofReader in)
        throws IOException
    {
        if (in.getVersionId() != VERSION_ID) {
            throw new IOException(
                "LocationSerializer encountered unexpected " +
                "version id: " + in.getVersionId());
        }

        final Integer locationId = in.readInt(LOCATIONID);
        final String address = in.readString(ADDRESS);
        final String city = in.readString(CITY);
        final String state = in.readString(STATE);
        final String postalCode = in.readString(POSTALCODE);
        final String country = in.readString(COUNTRY);
        in.readRemainder();

        final Location location = new Location();
        location.setLocationId(locationId);
        location.setAddress(address);
        location.setCity(city);
        location.setState(state);
        location.setPostalCode(postalCode);
        location.setCountry(country);
        return location;
    }
}